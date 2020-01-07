package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.acceleratorfacades.order.populators.AcceleratorGroupOrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;


/**
 * Group Entry populator to set selling price
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceGroupOrderEntryPopulator extends AcceleratorGroupOrderEntryPopulator
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OrderEntryData createGroupedOrderEntry(final OrderEntryData firstEntry)
	{
		final OrderEntryData groupedEntry = super.createGroupedOrderEntry(firstEntry);
		groupedEntry.setSellingPrice(firstEntry.getSellingPrice());
		return groupedEntry;
	}
}
