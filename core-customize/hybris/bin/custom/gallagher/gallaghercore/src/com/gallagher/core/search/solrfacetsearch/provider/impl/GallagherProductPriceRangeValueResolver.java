/**
 *
 */
package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductPricesValueResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
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

import com.gallagher.core.search.solrfacetsearch.provider.GallagherQualifierProvider;


/**
 * @author ankituniyal
 *
 */
public class GallagherProductPriceRangeValueResolver extends ProductPricesValueResolver
{

	public static final String FIELDNAME_SEPARATOR = "_";

	private GallagherQualifierProvider salesAreaQualifierProvider;

	private GallagherQualifierProvider userPriceGroupQualifierProvider;

	private GallagherQualifierProvider customerGroupQualifierProvider;

	private CommercePriceService commercePriceService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resolve(final InputDocument document, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model) throws FieldValueProviderException
	{
		ServicesUtil.validateParameterNotNull("model", "model instance is null");
		if (CollectionUtils.isNotEmpty(model.getVariants()))
		{
			ServicesUtil.validateParameterNotNull("model", "model instance is null");
			final IndexedProperty indexProperty = indexedProperties.iterator().next();
			if (!salesAreaQualifierProvider.canApply(indexProperty) && !userPriceGroupQualifierProvider.canApply(indexProperty)
					&& !customerGroupQualifierProvider.canApply(indexProperty))
			{
				super.resolve(document, batchContext, indexedProperties, model);
				return;
			}
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
						addFieldValues(document, facetSearchConfig, resolverContext);
					}
				}

				final FacetSearchConfig facetSearchConfig1 = batchContext.getFacetSearchConfig();
				final IndexedType indexedType1 = batchContext.getIndexedType();

				final Collection<Qualifier> qualifiers = getQualifierProvider().getAvailableQualifiers(facetSearchConfig1,
						indexedType1);

				final Collection<Qualifier> salesAreaQualifiers = salesAreaQualifierProvider
						.getAvailableQualifiers(facetSearchConfig1, indexedType1);
				final Collection<Qualifier> upgQualifiers = userPriceGroupQualifierProvider.getAvailableQualifiers(facetSearchConfig1,
						indexedType1);
				final Collection<Qualifier> customerGroupQualifiers = customerGroupQualifierProvider
						.getAvailableQualifiers(facetSearchConfig1, indexedType1);

				for (final Qualifier qualifier : qualifiers)
				{
					salesAreaQualifierProvider.removeQualifier();
					String fieldQualifier = StringUtils.EMPTY;
					fieldQualifier = indexQualifierData(document, batchContext, indexedProperties, model, resolverContext,
							getQualifierProvider(), qualifier, fieldQualifier);

					processAdditionalPriceConditionQualifiers(document, batchContext, indexedProperties, model, resolverContext,
							fieldQualifier, salesAreaQualifiers, upgQualifiers, customerGroupQualifiers);
					salesAreaQualifierProvider.removeQualifier();
				}
			}
			finally
			{
				removeLocalSessionContext();
			}
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
	 *           the indexed properties
	 * @param model
	 *           the product mode
	 * @param resolverContext
	 *           the resolver context
	 * @param fieldQualifier
	 *           the field qualifier
	 * @param customerGroupQualifiers2
	 * @param upgQualifiers2
	 * @param salesAreaQualifiers2
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	private void processAdditionalPriceConditionQualifiers(final InputDocument document, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model,
			final ValueResolverContext resolverContext, final String fieldQualifier, final Collection<Qualifier> salesAreaQualifiers,
			final Collection<Qualifier> upgQualifiers, final Collection<Qualifier> customerGroupQualifiers)
			throws FieldValueProviderException
	{

		for (final Qualifier salesAreaQualifier : salesAreaQualifiers)
		{
			final String salesAreaFieldQualifier = indexQualifierData(document, batchContext, indexedProperties, model,
					resolverContext, salesAreaQualifierProvider, salesAreaQualifier, fieldQualifier);

			for (final Qualifier upgQualifier : upgQualifiers)
			{

				final String upgFieldQualifier = indexQualifierData(document, batchContext, indexedProperties, model, resolverContext,
						userPriceGroupQualifierProvider, upgQualifier, salesAreaFieldQualifier);

				for (final Qualifier customerGroupQualifier : customerGroupQualifiers)
				{

					indexQualifierData(document, batchContext, indexedProperties, model, resolverContext,
							customerGroupQualifierProvider, customerGroupQualifier, upgFieldQualifier);
				}
				customerGroupQualifierProvider.removeQualifier();
			}
			userPriceGroupQualifierProvider.removeQualifier();
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
		final Object qualifierData = loadQualifierData(batchContext, indexedProperties, model, qualifier);
		final String newFieldQualifier = getFieldQualifierName(qualifier, fieldQualifier);
		setValueResolverContext(resolverContext, qualifier, newFieldQualifier, qualifierData);
		processQualifierforIndex(document, indexedProperties, resolverContext, qualifierProvider);
		return newFieldQualifier;
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
	 * Method to load qualifier data.
	 *
	 * @param indexedProperties
	 *           the indexed properties collection
	 * @param product
	 *           the product model
	 * @return the price information list
	 */
	@Override
	protected List<PriceInformation> loadQualifierData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier)
	{
		final List<PriceInformation> priceInfoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(product.getVariants()))
		{
			final PriceInformation priceInfo = getCommercePriceService().getFromPriceForProduct(product);
			if (priceInfo != null)
			{
				priceInfoList.add(priceInfo);
			}
		}
		return priceInfoList;
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
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	private void processQualifierforIndex(final InputDocument document, final Collection<IndexedProperty> indexedProperties,
			final ValueResolverContext resolverContext, final QualifierProvider qualifierProvider) throws FieldValueProviderException
	{
		addFieldValues(document, indexedProperties.iterator().next(), resolverContext);
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
	 * Method to add the field values to input document.
	 *
	 * @param document
	 *           the input document
	 * @param indexedProperty
	 *           the indexed property
	 * @param resolverContext
	 *           the value resolver context
	 * @throws FieldValueProviderException
	 *            the field value provider exception
	 */
	protected void addFieldValues(final InputDocument document, final IndexedProperty indexedProperty,
			final ValueResolverContext<Object, List<PriceInformation>> resolverContext) throws FieldValueProviderException
	{

		final List<PriceInformation> priceInformations = resolverContext.getQualifierData();
		if (priceInformations != null)
		{
			final Double priceValue = getPriceValue(indexedProperty, priceInformations);
			if (priceValue != null)
			{
				document.addField(indexedProperty, priceValue, resolverContext.getFieldQualifier());
			}
		}
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
	 * @return the userPriceGroupQualifierProvider
	 */
	public GallagherQualifierProvider getUserPriceGroupQualifierProvider()
	{
		return userPriceGroupQualifierProvider;
	}

	/**
	 * @param userPriceGroupQualifierProvider
	 *           the userPriceGroupQualifierProvider to set
	 */
	public void setUserPriceGroupQualifierProvider(final GallagherQualifierProvider userPriceGroupQualifierProvider)
	{
		this.userPriceGroupQualifierProvider = userPriceGroupQualifierProvider;
	}

	/**
	 * @return the commercePriceService
	 */
	public CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	/**
	 * @param commercePriceService
	 *           the commercePriceService to set
	 */
	public void setCommercePriceService(final CommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	/**
	 * @return the customerGroupQualifierProvider
	 */
	public GallagherQualifierProvider getCustomerGroupQualifierProvider()
	{
		return customerGroupQualifierProvider;
	}

	/**
	 * @param customerGroupQualifierProvider
	 *           the customerGroupQualifierProvider to set
	 */
	public void setCustomerGroupQualifierProvider(final GallagherQualifierProvider customerGroupQualifierProvider)
	{
		this.customerGroupQualifierProvider = customerGroupQualifierProvider;
	}
}
