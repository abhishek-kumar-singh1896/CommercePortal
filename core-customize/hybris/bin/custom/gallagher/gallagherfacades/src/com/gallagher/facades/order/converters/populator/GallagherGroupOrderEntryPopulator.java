/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.commercefacades.order.converters.populator.GroupOrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import com.gallagher.core.enums.OrderEntryItemStatus;


/**
 *
 */
public class GallagherGroupOrderEntryPopulator extends GroupOrderEntryPopulator<AbstractOrderModel, AbstractOrderData>
{

	@Resource(name = "productService")
	private ProductService productService;

	@Override
	protected ProductData createBaseProduct(final ProductData variant)
	{
		final ProductData productData = new ProductData();

		productData.setUrl(variant.getUrl());
		productData.setPurchasable(variant.getPurchasable());
		productData.setMultidimensional(Boolean.TRUE);
		productData.setImages(variant.getImages());

		final ProductModel productModel = productService.getProductForCode(variant.getBaseProduct());
		productData.setCode(productModel.getCode());
		productData.setName(productModel.getMarketingDescription());
		productData.setDescription(productModel.getDescription());

		productData.setPriceRange(new PriceRangeData());

		return productData;
	}

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
				parentEntry.setDeliveredQuantity(getDeliveredQuantity(parentEntry));
				parentEntry.setStatus(getOrderEntryStatus(parentEntry));
			}
		}
	}

	protected Long getDeliveredQuantity(final OrderEntryData parentEntry)
	{
		long deliveredQuantity = 0;
		if (!CollectionUtils.isEmpty(parentEntry.getEntries()))
		{
			deliveredQuantity = (parentEntry.getEntries().get(0).getDeliveredQuantity() != null
					? parentEntry.getEntries().get(0).getDeliveredQuantity().longValue()
					: 0);
		}
		return Long.valueOf(deliveredQuantity);
	}

	protected OrderEntryItemStatus getOrderEntryStatus(final OrderEntryData parentEntry)
	{
		if (!CollectionUtils.isEmpty(parentEntry.getEntries()))
		{
			return parentEntry.getEntries().get(0).getStatus() != null ? parentEntry.getEntries().get(0).getStatus() : null;
		}
		return null;
	}

}
