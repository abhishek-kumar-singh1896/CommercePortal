package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resolver for SKUs of Gallagher product
 *
 * @author Vikram Bishnoi
 *
 */
public class GallagherVariantAttributeValueResolver extends AbstractValueResolver<ProductModel, List<String>, List<String>>
{
	private ModelService modelService;

	private String variantField;



	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<List<String>, List<String>> resolverContext) throws FieldValueProviderException
	{
		if (resolverContext.getFieldQualifier() != null)
		{
			document.addField(indexedProperty, resolverContext.getQualifierData(), resolverContext.getFieldQualifier());
		}
		else
		{
			document.addField(indexedProperty, resolverContext.getData());
		}
	}

	@Override
	public List<String> loadQualifierData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model, final Qualifier qualifier)
			throws FieldValueProviderException
	{
		return getVariantData(model);
	}

	private List<String> getVariantData(final ProductModel model)
	{
		final List<String> qualifierData = new ArrayList<>();
		final Collection<VariantProductModel> variants = model.getVariants();
		if (CollectionUtils.isNotEmpty(variants))
		{
			for (final VariantProductModel variant : variants)
			{
				qualifierData.add(getModelService().getAttributeValue(variant, getVariantField()));
			}
		}

		return qualifierData;
	}

	@Override
	public List<String> loadData(final IndexerBatchContext batchContext, final Collection<IndexedProperty> indexedProperties,
			final ProductModel product)
	{
		return getVariantData(product);
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public String getVariantField()
	{
		return variantField;
	}

	public void setVariantField(final String variantField)
	{
		this.variantField = variantField;
	}
}
