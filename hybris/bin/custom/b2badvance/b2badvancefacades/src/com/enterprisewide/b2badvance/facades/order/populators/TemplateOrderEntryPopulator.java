/**
 *
 */
package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Arrays;
import java.util.Collection;


/**
 * @author Enterprise Wide
 *
 */
public class TemplateOrderEntryPopulator extends OrderEntryPopulator
{
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	/**
	 * @return the productConfiguredPopulator
	 */
	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	/**
	 * @return the productConfiguredPopulator
	 */
	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

	@Override
	protected void addProduct(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		final Collection<ProductOption> options = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.URL,
				ProductOption.STOCK, ProductOption.VARIANT_MATRIX_BASE, ProductOption.VARIANT_MATRIX_URL,
				ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.CATEGORIES);
		entry.setProduct(getProductConverter().convert(orderEntry.getProduct()));
		if (options != null)
		{
			getProductConfiguredPopulator().populate(orderEntry.getProduct(), entry.getProduct(), options);
		}
	}
}
