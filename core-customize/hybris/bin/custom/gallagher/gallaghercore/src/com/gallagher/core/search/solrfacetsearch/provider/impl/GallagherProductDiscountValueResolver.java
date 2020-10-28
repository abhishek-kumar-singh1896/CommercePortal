/**
 *
 */
package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductPricesValueResolver;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.DiscountInformation;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.order.DiscountService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.impl.DefaultDiscountValueInfoCriteria;
import de.hybris.platform.product.BaseCriteria;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gallagher.core.search.solrfacetsearch.provider.GallagherQualifierProvider;



/**
 * @author ankituniyal
 *
 */
public class GallagherProductDiscountValueResolver extends ProductPricesValueResolver
{

	private static final Logger LOG = Logger.getLogger(GallagherProductDiscountValueResolver.class);

	public static final String FIELDNAME_SEPARATOR = "_";

	private GallagherQualifierProvider salesAreaQualifierProvider;

	private DiscountService discountService;

	private CommonI18NService commonI18NService;

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void resolve(final InputDocument document, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model) throws FieldValueProviderException
	{
		ServicesUtil.validateParameterNotNull("model", "model instance is null");

		try
		{
			createLocalSessionContext();
			final Object data = loadData(batchContext, indexedProperties, model);
			final ValueResolverContext resolverContext = new ValueResolverContext();
			resolverContext.setData(data);

			final Iterator indexedType = indexedProperties.iterator();

			while (indexedType.hasNext())
			{
				final IndexedProperty facetSearchConfig = (IndexedProperty) indexedType.next();
				if (!getQualifierProvider().canApply(facetSearchConfig))
				{
					addFieldValues(document, facetSearchConfig, resolverContext, model, indexedProperties);
				}
			}

			final FacetSearchConfig facetSearchConfig1 = batchContext.getFacetSearchConfig();
			final IndexedType indexedType1 = batchContext.getIndexedType();

			final Collection<Qualifier> qualifiers = getQualifierProvider().getAvailableQualifiers(facetSearchConfig1,
					indexedType1);

			for (final Qualifier qualifier : qualifiers)
			{
				salesAreaQualifierProvider.removeQualifier();
				String fieldQualifier = StringUtils.EMPTY;
				fieldQualifier = indexQualifierData(document, batchContext, indexedProperties, model, resolverContext,
						getQualifierProvider(), qualifier, fieldQualifier);

				processAdditionalPriceConditionQualifiers(document, batchContext, indexedProperties, model, resolverContext,
						fieldQualifier);
				salesAreaQualifierProvider.removeQualifier();
			}
		}
		finally
		{
			removeLocalSessionContext();
		}
	}

	/**
	 * @param document
	 * @param batchContext
	 * @param indexedProperties
	 * @param model
	 * @param resolverContext
	 * @param fieldQualifier
	 */
	/**
	 * Method to process the additional price condition qualifiers.
	 *
	 * @param document
	 *           the input document
	 * @param batchContext
	 *           the batch context
	 * @param indexedProperties
	 *           the indexed properties
	 * @param model
	 *           the product mode
	 * @param resolverContext
	 *           the resolver context
	 * @param fieldQualifier
	 *           the field qualifier
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	private void processAdditionalPriceConditionQualifiers(final InputDocument document, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model,
			final ValueResolverContext resolverContext, final String fieldQualifier) throws FieldValueProviderException
	{
		final FacetSearchConfig facetSearchConfig1 = batchContext.getFacetSearchConfig();
		final IndexedType indexedType1 = batchContext.getIndexedType();
		final Collection<Qualifier> salesAreaQualifiers = salesAreaQualifierProvider.getAvailableQualifiers(facetSearchConfig1,
				indexedType1);

		for (final Qualifier salesAreaQualifier : salesAreaQualifiers)
		{
			indexQualifierData(document, batchContext, indexedProperties,
					model,
					resolverContext, salesAreaQualifierProvider, salesAreaQualifier, fieldQualifier);
		}
	}

	/**
	 * Method to process the additional price condition qualifiers.
	 *
	 * @param document
	 *           the input document
	 * @param batchContext
	 *           the batch context
	 * @param indexedProperties
	 *           the indexed properties collection
	 * @param model
	 *           the product mode
	 * @param resolverContext
	 *           the resolver context
	 * @param qualifierProvider
	 *           the qualifier provider
	 * @param qualifier
	 *           the qualifier
	 * @param fieldQualifier
	 *           the field qualifier
	 * @return the new field qualifier string
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	private String indexQualifierData(final InputDocument document, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model,
			final ValueResolverContext resolverContext, final QualifierProvider qualifierProvider, final Qualifier qualifier,
			final String fieldQualifier) throws FieldValueProviderException
	{
		applyQualifier(qualifierProvider, qualifier);
		final List<DiscountInformation> qualifierData = loadQualifierDataForDiscount(batchContext, indexedProperties, model,
				qualifier);
		final String newFieldQualifier = getFieldQualifierName(qualifier, fieldQualifier);
		setValueResolverContext(resolverContext, qualifier, newFieldQualifier, qualifierData);
		processQualifierforIndex(document, indexedProperties, resolverContext, qualifierProvider, model);
		return newFieldQualifier;
	}

	/**
	 * Method to load qualifier data.
	 *
	 * @param indexedProperties
	 *           the indexed properties collection
	 * @param product
	 *           the product model
	 * @return the price information list
	 */
	protected List<DiscountInformation> loadQualifierDataForDiscount(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier)
	{
		List<DiscountInformation> discountInformations = new ArrayList<>();

		if (CollectionUtils.isEmpty(product.getVariants()))
		{
			try
			{
				final BaseCriteria pCriteria = DefaultDiscountValueInfoCriteria.buildForInfo().withProduct(product).build();

				discountInformations = getDiscountService().getDiscountInformation(pCriteria);
			}
			catch (final CalculationException e)
			{
				LOG.error("Error while calculating discount " + e.getMessage());
			}
		}
		return discountInformations;
	}

	/**
	 * Method to process the qualifier for index.
	 *
	 * @param document
	 *           the input document
	 * @param indexedProperties
	 *           the indexed properties collection
	 * @param resolverContext
	 *           the resolver context
	 * @param qualifierProvider
	 *           the qualifier provider
	 * @param product
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	private void processQualifierforIndex(final InputDocument document, final Collection<IndexedProperty> indexedProperties,
			final ValueResolverContext resolverContext, final QualifierProvider qualifierProvider, final ProductModel product)
			throws FieldValueProviderException
	{
		for (final IndexedProperty indexedProperty : indexedProperties)
		{
			if (qualifierProvider.canApply(indexedProperty))
			{
				addFieldValues(document, indexedProperty, resolverContext, product, indexedProperties);
			}
		}
	}

	/**
	 * Method to add the field values to input document.
	 *
	 * @param document
	 *           the input document
	 * @param indexedProperty
	 *           the indexed property
	 * @param resolverContext
	 *           the value resolver context
	 * @param product
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	protected void addFieldValues(final InputDocument document, final IndexedProperty indexedProperty,
			final ValueResolverContext<Object, List<DiscountInformation>> resolverContext, final ProductModel product,
			final Collection<IndexedProperty> indexedProperties)
			throws FieldValueProviderException
	{
		final List<DiscountInformation> discountInformations = resolverContext.getQualifierData();
		if (CollectionUtils.isNotEmpty(discountInformations))
		{
			final String discountValue = getDiscountValue(indexedProperty, discountInformations, product, indexedProperties);
			if (StringUtils.isNotBlank(discountValue))
			{
				document.addField(indexedProperty, discountValue, resolverContext.getFieldQualifier());
			}
		}
	}

	/**
	 * @param indexedProperty
	 * @param discountInformations
	 * @param productModel
	 * @param indexedProperties
	 * @return
	 */
	private String getDiscountValue(final IndexedProperty indexedProperty, final List<DiscountInformation> discountInformations,
			final ProductModel productModel, final Collection<IndexedProperty> indexedProperties)
	{

		final List<PriceInformation> priceInfos = loadPriceInformations(indexedProperties, productModel);
		final DiscountInformation discInfo = discountInformations.get(0);
		final String discCurrencyIso = discInfo.getDiscountValue().getCurrencyIsoCode();

		if (CollectionUtils.isNotEmpty(priceInfos))
		{

			final PriceInformation priceInfo = priceInfos.get(0);

			final DiscountData discountData = new DiscountData();
			final String priceCurrencyIso = priceInfo.getValue().getCurrencyIso();
			final boolean isSameCurrency = priceCurrencyIso != discCurrencyIso ? false : true;

			discountData.setSameCurrency(isSameCurrency);
			final CurrencyModel currencyModel = commonI18NService.getCurrency(priceCurrencyIso);

			final double netPrice = getNetPrice(discInfo, priceInfo);

			final String netProductValue = currencyModel.getSymbol() + String.format("%.2f", netPrice);
			return netProductValue;
		}
		return null;
	}


	/**
	 * Method to set the value resolver context as per qualifier, field qualifier and data values.
	 *
	 * @param resolverContext
	 *           the value resolver context
	 * @param qualifier
	 *           the qualifier
	 * @param fieldQualifier
	 *           the field qualifier
	 * @param qualifierData
	 *           the qualifier data
	 */
	private void setValueResolverContext(final ValueResolverContext resolverContext, final Qualifier qualifier,
			final String fieldQualifier, final Object qualifierData)
	{
		resolverContext.setQualifier(qualifier);
		resolverContext.setFieldQualifier(fieldQualifier);
		resolverContext.setQualifierData(qualifierData);
	}


	/**
	 * Method to get field qualifier name
	 *
	 * @param qualifier
	 *           the qualifier
	 * @param fieldQualifier
	 *           the field qualifier
	 * @return the new field qualifier name
	 */
	private String getFieldQualifierName(final Qualifier qualifier, final String fieldQualifier)
	{
		final StringBuilder newFieldQualifier = new StringBuilder(200);
		if (StringUtils.isNotBlank(fieldQualifier))
		{
			newFieldQualifier.append(fieldQualifier).append(FIELDNAME_SEPARATOR);
		}
		newFieldQualifier.append(qualifier.toFieldQualifier());
		return newFieldQualifier.toString();
	}

	/**
	 * Method to apply the qualifier to the specified qualifier provider
	 *
	 * @param qualifierProvider
	 *           the qualifier provider
	 * @param qualifier
	 *           the qualifier
	 */
	private void applyQualifier(final QualifierProvider qualifierProvider, final Qualifier qualifier)
	{
		qualifierProvider.applyQualifier(qualifier);
	}

	/**
	 * @param priceInfo
	 * @param discInfo
	 * @return
	 */
	private double getNetPrice(final DiscountInformation discInfo, final PriceInformation priceInfo)
	{
		double netPrice;
		if (discInfo.getDiscountValue().isAbsolute())
		{

			netPrice = priceInfo.getPriceValue().getValue() - discInfo.getDiscountValue().getValue();
		}
		else
		{
			netPrice = priceInfo.getPriceValue().getValue()
					- (priceInfo.getPriceValue().getValue() * (discInfo.getDiscountValue().getValue() / 100));
		}

		return netPrice;
	}

	/**
	 * The static class ValueResolverContext. Holds the qualifier data and field qualifier to be indexed.
	 *
	 * @param <T>
	 * @param <U>
	 */
	protected static final class ValueResolverContext<T, U>
	{
		private T data;
		private U qualifierData;
		private Qualifier qualifier;
		private String fieldQualifier;

		public T getData()
		{
			return this.data;
		}

		public void setData(final T data)
		{
			this.data = data;
		}

		public U getQualifierData()
		{
			return this.qualifierData;
		}

		public void setQualifierData(final U qualifierData)
		{
			this.qualifierData = qualifierData;
		}

		public Qualifier getQualifier()
		{
			return this.qualifier;
		}

		public void setQualifier(final Qualifier qualifier)
		{
			this.qualifier = qualifier;
		}

		public String getFieldQualifier()
		{
			return this.fieldQualifier;
		}

		public void setFieldQualifier(final String fieldQualifier)
		{
			this.fieldQualifier = fieldQualifier;
		}
	}

	/**
	 * @return the salesAreaQualifierProvider
	 */
	public GallagherQualifierProvider getSalesAreaQualifierProvider()
	{
		return salesAreaQualifierProvider;
	}


	/**
	 * @param salesAreaQualifierProvider
	 *           the salesAreaQualifierProvider to set
	 */
	public void setSalesAreaQualifierProvider(final GallagherQualifierProvider salesAreaQualifierProvider)
	{
		this.salesAreaQualifierProvider = salesAreaQualifierProvider;
	}

	/**
	 * @return the discountService
	 */
	public DiscountService getDiscountService()
	{
		return discountService;
	}

	/**
	 * @param discountService
	 *           the discountService to set
	 */
	public void setDiscountService(final DiscountService discountService)
	{
		this.discountService = discountService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

}
