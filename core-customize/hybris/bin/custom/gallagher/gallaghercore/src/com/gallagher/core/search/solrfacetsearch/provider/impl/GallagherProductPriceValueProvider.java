package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductPriceRangeValueProvider;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductPriceRangeValueProvider.PriceRangeComparator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ProductPriceValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Price Provider to display price for the products received from Solr
 *
 * @author Vikram Bishnoi
 *
 */
public class GallagherProductPriceValueProvider extends ProductPriceValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private PriceService priceService;
	private ProductPriceRangeValueProvider productPriceRangeValueProvider;

	@Override
	protected void addFieldValues(final Collection<FieldValue> fieldValues, final ProductModel product,
			final IndexedProperty indexedProperty, final String currency) throws FieldValueProviderException
	{
		final List<PriceInformation> prices = this.priceService.getPriceInformationsForProduct(product);
		if (CollectionUtils.isEmpty(prices))
		{
			final PriceRowModel minPrice = getLowestPrice(product);
			if (minPrice != null)
			{
				addPriceValue(fieldValues, indexedProperty, currency, getLowestPrice(product).getPrice());
			}
		}
		else
		{
			final Double value = prices.get(0).getPriceValue().getValue();
			addPriceValue(fieldValues, indexedProperty, currency, value);
		}
	}


	/**
	 * Adds the price as solr field
	 *
	 * @param fieldValues
	 *           to be updated
	 * @param indexedProperty
	 *           to be indexed
	 * @param currency
	 *           for the price
	 * @param value
	 *           the price
	 * @throws FieldValueProviderException
	 *            if there is any exception
	 */
	private void addPriceValue(final Collection<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final String currency, final Double value) throws FieldValueProviderException
	{
		final List<String> rangeNameList = this.getRangeNameList(indexedProperty, value, currency);
		final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
				currency == null ? null : currency.toLowerCase(Locale.ROOT));
		final Iterator var10 = fieldNames.iterator();

		while (true)
		{
			while (var10.hasNext())
			{
				final String fieldName = (String) var10.next();
				if (rangeNameList.isEmpty())
				{
					fieldValues.add(new FieldValue(fieldName, value));
				}
				else
				{
					final Iterator var12 = rangeNameList.iterator();

					while (var12.hasNext())
					{
						final String rangeName = (String) var12.next();
						fieldValues.add(new FieldValue(fieldName, rangeName == null ? value : rangeName));
					}
				}
			}

			return;
		}
	}

	/**
	 * Returns the lowest price from the variant prices
	 *
	 * @param product
	 *           to get the price
	 * @return lowest price
	 */
	private PriceRowModel getLowestPrice(final ProductModel product)
	{
		PriceRowModel lowest = null;
		// make sure you have the baseProduct because variantProducts won't have other variants
		final ProductModel baseProduct = productPriceRangeValueProvider.getBaseProduct(product);

		final Collection<VariantProductModel> variants = baseProduct.getVariants();
		if (CollectionUtils.isNotEmpty(variants))
		{
			final List<PriceRowModel> allPricesInfos = new ArrayList<PriceRowModel>();

			// collect all price infos
			for (final VariantProductModel variant : variants)
			{
				allPricesInfos.addAll(variant.getEurope1Prices());
			}

			if (!allPricesInfos.isEmpty())
			{
				Collections.sort(allPricesInfos, PriceRangeComparator.INSTANCE);
				lowest = allPricesInfos.get(0);
			}
		}

		return lowest;
	}


	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	public PriceService getPriceService()
	{
		return priceService;
	}

	@Override
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	public ProductPriceRangeValueProvider getProductPriceRangeValueProvider()
	{
		return productPriceRangeValueProvider;
	}

	public void setProductPriceRangeValueProvider(final ProductPriceRangeValueProvider productPriceRangeValueProvider)
	{
		this.productPriceRangeValueProvider = productPriceRangeValueProvider;
	}
}
