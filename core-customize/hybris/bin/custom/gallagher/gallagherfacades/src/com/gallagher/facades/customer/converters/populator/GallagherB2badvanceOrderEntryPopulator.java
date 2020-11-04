/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import com.enterprisewide.b2badvance.facades.populators.B2badvanceOrderEntryPopulator;


/**
 * @author gauravkamboj
 */
public class GallagherB2badvanceOrderEntryPopulator extends B2badvanceOrderEntryPopulator
{
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		target.setDeliveredQuantity(source.getDeliveredQuantity());
		target.setStatus(source.getStatus());
		super.populate(source, target);
	}
}
