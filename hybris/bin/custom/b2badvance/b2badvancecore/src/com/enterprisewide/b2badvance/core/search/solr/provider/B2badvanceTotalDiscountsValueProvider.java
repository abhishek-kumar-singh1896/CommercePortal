/**
 *
 */
package com.enterprisewide.b2badvance.core.search.solr.provider;

import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 *
 */
public class B2badvanceTotalDiscountsValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{

	private FieldNameProvider fieldNameProvider;
	private SessionService sessionService;

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel product = (ProductModel) model;//this provider shall only be used with products
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();




		for (final CurrencyModel currencyModel : indexConfig.getCurrencies())
		{
			final List<DiscountData> discountRow = new ArrayList<DiscountData>();
			for (final DiscountRowModel promotionPriceRowModel : product.getEurope1Discounts())
			{


				final String sessionCurrencySymbol = currencyModel.getSymbol();
				if (promotionPriceRowModel.getCurrency() != null && promotionPriceRowModel.getCurrency().getSymbol() != null)
				{
					if (!promotionPriceRowModel.getCurrency().getSymbol().equalsIgnoreCase(sessionCurrencySymbol))
					{
						continue;
					}
				}

				final DiscountData discountData = new DiscountData();
				if (promotionPriceRowModel.getDiscount().getValue() != null)
				{


					for (final PriceRowModel priceRowModel : product.getEurope1Prices())
					{


						if (priceRowModel.getCurrency() != null && priceRowModel.getCurrency().getSymbol() != null)
						{
							if (!priceRowModel.getCurrency().getSymbol().equalsIgnoreCase(sessionCurrencySymbol))
							{
								discountData.setSameCurrency(false);
								continue;
							}
							else
							{
								discountData.setSameCurrency(true);
							}
						}

						Double discountPrice = promotionPriceRowModel.getDiscount().getValue();

						Double netPrice;
						if (discountPrice != null && priceRowModel.getPrice() != null)
						{

							if (promotionPriceRowModel.getAbsolute())
							{
								netPrice = priceRowModel.getPrice() - discountPrice;
							}
							else
							{
								netPrice = priceRowModel.getPrice() - (priceRowModel.getPrice() * (discountPrice / 100));
							}
							final String netProductValue = priceRowModel.getCurrency().getSymbol() + String.format("%.2f", netPrice);
							discountData.setValue(netProductValue);
							//break;
						}

					}
					if (discountData.isSameCurrency())
					{
						discountData.setDiscountString(promotionPriceRowModel.getDiscountString());
						discountRow.add(discountData);

						final String value = discountRow.get(0).getValue();

						final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
								currencyModel.getIsocode().toLowerCase());
						for (final String fieldName : fieldNames)
						{
							fieldValues.add(new FieldValue(fieldName, value));
						}
						break;
					}
				}
			}

		}





		return fieldValues;
	}





	/**
	 * @return the fieldNameProvider
	 */
	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
