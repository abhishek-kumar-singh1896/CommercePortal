package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.commercefacades.order.converters.populator.GroupOrderConsignmentEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.Map;

import org.springframework.util.CollectionUtils;


/**
 * Group Entry populator to set selling price
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceGroupOrderConsignmentEntryPopulator extends GroupOrderConsignmentEntryPopulator
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

	/**
	 * Overriding super class implementation to set deliveredQuantity and status for OrderEntryData
	 */
	@Override
	protected void consolidateGroupedOrderEntry(final Map<String, OrderEntryData> group)
	{
		for (final String productCode : group.keySet())
		{
			final OrderEntryData parentEntry = group.get(productCode);
			if (parentEntry.getEntries() != null)
			{
				final PriceData firstEntryTotalPrice = parentEntry.getEntries().get(0).getTotalPrice();
				final PriceRangeData priceRange = parentEntry.getProduct().getPriceRange();

				if (firstEntryTotalPrice != null)
				{
					priceRange.setMaxPrice(getMaxPrice(parentEntry, firstEntryTotalPrice));
					priceRange.setMinPrice(getMinPrice(parentEntry, firstEntryTotalPrice));
					parentEntry.setTotalPrice(getTotalPrice(parentEntry, firstEntryTotalPrice));
				}
				parentEntry.setQuantity(getTotalQuantity(parentEntry));
				parentEntry.setProductSpecificDetailsHeading(getProductSpecificDetailsHeading(parentEntry));
				parentEntry.setDeliveryinstruction(getDeliveryInstruction(parentEntry));
			}
		}
	}

	protected String getProductSpecificDetailsHeading(final OrderEntryData parentEntry)
	{
		String productSpecificDetailsHeading = null;
		if (!CollectionUtils.isEmpty(parentEntry.getEntries()))
		{
			productSpecificDetailsHeading = (parentEntry.getEntries().get(0).getProductSpecificDetailsHeading() != null
					? parentEntry.getEntries().get(0).getProductSpecificDetailsHeading().toString()
					: "");
		}
		return productSpecificDetailsHeading;
	}

	protected String getDeliveryInstruction(final OrderEntryData parentEntry)
	{
		String deliveryInstruction = null;
		if (!CollectionUtils.isEmpty(parentEntry.getEntries()))
		{
			deliveryInstruction = (parentEntry.getEntries().get(0).getDeliveryinstruction() != null
					? parentEntry.getEntries().get(0).getDeliveryinstruction().toString()
					: "");
		}
		return deliveryInstruction;
	}

}
