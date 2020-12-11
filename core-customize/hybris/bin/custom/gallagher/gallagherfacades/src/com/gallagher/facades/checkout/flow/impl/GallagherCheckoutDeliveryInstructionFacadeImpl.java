/**
 *
 */
package com.gallagher.facades.checkout.flow.impl;

import de.hybris.platform.commercefacades.order.data.DeliveryInstructionData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gallagher.facades.checkout.flow.GallagherCheckoutDeliveryInstructionFacade;


/**
 * @author abhinavgupta03
 *
 */
public class GallagherCheckoutDeliveryInstructionFacadeImpl extends DefaultCheckoutFacade
		implements GallagherCheckoutDeliveryInstructionFacade
{

	@Override
	public void setDeliveryInstructions(final String deliveryInstruction, final String productSpecificDetailsHeading,
			final Integer entryNumber, final String productSpecificDetailsSubHeading)
	{
		final CartModel cartModel = getCart();
		final List<AbstractOrderEntryModel> cartEntries = cartModel.getEntries();
		for (final AbstractOrderEntryModel cartEntry : cartEntries)
		{
			if (cartEntry.getEntryNumber().equals(entryNumber))
			{
				cartEntry.setDeliveryInstruction(deliveryInstruction);
				if (null != productSpecificDetailsHeading)
				{
					cartEntry.setProductSpecificDetailsHeading(productSpecificDetailsHeading);
				}
				if (null != productSpecificDetailsSubHeading)
				{
					cartEntry.setProductSpecificDetailsSubHeading(productSpecificDetailsSubHeading);
				}
				break;
			}
		}
		getModelService().saveAll(cartModel.getEntries());
		getModelService().save(cartModel);
		getModelService().refresh(cartModel);
	}

	@Override
	public void populateDeliveryInstruction(final DeliveryInstructionData deliveryInstrutionsData)
	{
		final CartModel cartModel = getCart();
		final Integer entryNumber = deliveryInstrutionsData.getEntryNumber();
		final List<AbstractOrderEntryModel> cartEntries = cartModel.getEntries();
		for (final AbstractOrderEntryModel cartEntry : cartEntries)
		{
			if (cartEntry.getEntryNumber().equals(entryNumber))
			{
				if (StringUtils.isNotEmpty(cartEntry.getDeliveryInstruction()))
				{
					deliveryInstrutionsData.setDeliveryinstruction(cartEntry.getDeliveryInstruction());
				}

				if (StringUtils.isNotEmpty(cartEntry.getProductSpecificDetailsHeading()))
				{
					deliveryInstrutionsData.setProductSpecificDetailsHeading(cartEntry.getProductSpecificDetailsHeading());
				}
				else
				{
					deliveryInstrutionsData
							.setProductSpecificDetailsHeading(cartEntry.getProduct().getProductSpecificDetailsHeading());
				}

				if (StringUtils.isNotEmpty(cartEntry.getProductSpecificDetailsSubHeading()))
				{
					deliveryInstrutionsData.setProductSpecificDetailsSubHeading(cartEntry.getProductSpecificDetailsSubHeading());
				}
				else
				{
					deliveryInstrutionsData
							.setProductSpecificDetailsSubHeading(cartEntry.getProduct().getProductSpecificDetailsSubHeading());
				}

			}
		}
	}
}
