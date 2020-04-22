/**
 *
 */
package com.gallagher.core.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.RecalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.impl.DefaultExternalTaxesService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;

import org.springframework.util.Assert;


/**
 * Extending DefaultExternalTaxesService for customization to determine if the 3rd party call is necessary and apply the
 * results to the cart.
 *
 * @author shishirkant
 */
public class GallagherExternalTaxesService extends DefaultExternalTaxesService
{
	@Override
	public boolean calculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (getDecideExternalTaxesStrategy().shouldCalculateExternalTaxes(abstractOrder))
		{
			if (getRecalculateExternalTaxesStrategy().recalculate(abstractOrder))
			{
				final ExternalTaxDocument exTaxDocument = getCalculateExternalTaxesStrategy().calculateExternalTaxes(abstractOrder);
				Assert.notNull(exTaxDocument, "ExternalTaxDocument should not be null");
				// check if external tax calculation was successful
				if (!exTaxDocument.getAllTaxes().isEmpty())
				{
					getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, exTaxDocument);
					getSessionService().setAttribute(SESSION_EXTERNAL_TAX_DOCUMENT, exTaxDocument);
					saveOrder(abstractOrder);
					return true;
				}
				else
				{
					// the external tax calculation failed
					getSessionService().removeAttribute(RecalculateExternalTaxesStrategy.SESSION_ATTIR_ORDER_RECALCULATION_HASH);
					clearSessionTaxDocument();
					clearTaxValues(abstractOrder);
					saveOrder(abstractOrder);
				}
			}
			else
			{
				// get the cached tax document
				getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, getSessionExternalTaxDocument(abstractOrder));
				saveOrder(abstractOrder);
				return true;
			}
		}
		return false;
	}
}
