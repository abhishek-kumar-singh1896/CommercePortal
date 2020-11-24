/**
 *
 */
package com.gallagher.core.dao.impl;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gallagher.core.dao.GallagherProductProcessingDao;


/**
 * Implementation of GallagherProductProcessingDao
 *
 * @author shishirkant
 *
 */
public class GallagherProductProcessingDaoImpl implements GallagherProductProcessingDao
{

	@Resource(name = "flexibleSearchService")
	protected FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductModel> getProductsForConversion(final CatalogVersionModel catalogVersion, final Date lastStartTime)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(ProductModel.PK);
		stringBuilder.append("} from {").append(ProductModel._TYPECODE);
		stringBuilder.append("} where {").append(ProductModel.BASEPRODUCTCODE);
		stringBuilder.append("} IS NOT NULL AND {").append(ProductModel.CATALOGVERSION);
		stringBuilder.append("} = ?").append(ProductModel.CATALOGVERSION);

		queryParameter.put(ProductModel.CATALOGVERSION, catalogVersion);

		if (null != lastStartTime)
		{
			stringBuilder.append(" AND {").append(ProductModel.MODIFIEDTIME).append("} > ?").append(ProductModel.MODIFIEDTIME);
			queryParameter.put(ProductModel.MODIFIEDTIME, lastStartTime);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<ProductModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductModel> getVariantProductsForTransformation(final CatalogVersionModel catalogVersion,
			final Date lastStartTime)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(ProductModel.PK);
		stringBuilder.append("} from {").append(ProductModel._TYPECODE);
		stringBuilder.append("} where {").append(ProductModel.BASEPRODUCTCODE);
		stringBuilder.append("} IS NOT NULL AND {").append(ProductModel.APPROVALSTATUS);
		stringBuilder.append("} = ({{SELECT {PK} from {ArticleApprovalStatus} where {code} = ?")
				.append(ProductModel.APPROVALSTATUS);
		stringBuilder.append("}}) AND {").append(ProductModel.CATALOGVERSION);
		stringBuilder.append("} = ?").append(ProductModel.CATALOGVERSION);

		queryParameter.put(ProductModel.CATALOGVERSION, catalogVersion);
		queryParameter.put(ProductModel.APPROVALSTATUS, ArticleApprovalStatus.APPROVED.getCode());

		if (null != lastStartTime)
		{
			stringBuilder.append(" AND {").append(ProductModel.MODIFIEDTIME).append("} > ?").append(ProductModel.MODIFIEDTIME);
			queryParameter.put(ProductModel.MODIFIEDTIME, lastStartTime);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<ProductModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductModel> getBaseProductsForSync(final CatalogVersionModel catalogVersion, final Date lastStartTime)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(ProductModel.PK);
		stringBuilder.append("} from {").append(ProductModel._TYPECODE);
		stringBuilder.append("} where {").append(ProductModel.BASEPRODUCTCODE);
		stringBuilder.append("} IS NULL AND {").append(ProductModel.APPROVALSTATUS);
		stringBuilder.append("} = ({{SELECT {PK} from {ArticleApprovalStatus} where {code} = ?")
				.append(ProductModel.APPROVALSTATUS);
		stringBuilder.append("}}) AND {").append(ProductModel.CATALOGVERSION);
		stringBuilder.append("} = ?").append(ProductModel.CATALOGVERSION);

		queryParameter.put(ProductModel.CATALOGVERSION, catalogVersion);
		queryParameter.put(ProductModel.APPROVALSTATUS, ArticleApprovalStatus.APPROVED.getCode());

		if (null != lastStartTime)
		{
			stringBuilder.append(" AND {").append(ProductModel.MODIFIEDTIME).append("} > ?").append(ProductModel.MODIFIEDTIME);
			queryParameter.put(ProductModel.MODIFIEDTIME, lastStartTime);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<ProductModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductModel getBaseProductForProductReferenceSync(final CatalogVersionModel catalogVersion, final Date lastStartTime,
			final ProductModel product)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(ProductModel.PK);
		stringBuilder.append("} from {").append(SavedValuesModel._TYPECODE);
		stringBuilder.append("} where {").append(SavedValuesModel.MODIFIEDITEM);
		stringBuilder.append("} = ?").append(ProductModel.PK);
		queryParameter.put(ProductModel.PK, product.getPk());
		if (null != lastStartTime)
		{
			stringBuilder.append(" AND {").append(SavedValuesModel.MODIFIEDTIME).append("} > ?")
					.append(SavedValuesModel.MODIFIEDTIME);
			queryParameter.put(SavedValuesModel.MODIFIEDTIME, lastStartTime);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<SavedValuesModel> searchResult = flexibleSearchService.search(fQuery);
		for (final SavedValuesModel savedValueModel : searchResult.getResult())
		{
			final Set<SavedValueEntryModel> fr = savedValueModel.getSavedValuesEntries();
			for (final SavedValueEntryModel entryModel : fr)
			{
				if (StringUtils.isNotEmpty(entryModel.getModifiedAttribute())
						&& entryModel.getModifiedAttribute().equals("productReferences"))
				{
					return product;
				}
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductModel getBaseProductForRecommendationsSync(final CatalogVersionModel catalogVersion, final Date lastStartTime,
			final ProductModel product)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("Select {").append(ProductModel.PK);
		stringBuilder.append("} from {").append(SavedValuesModel._TYPECODE);
		stringBuilder.append("} where {").append(SavedValuesModel.MODIFIEDITEM);
		stringBuilder.append("} = ?").append(ProductModel.PK);
		queryParameter.put(ProductModel.PK, product.getPk());
		if (null != lastStartTime)
		{
			stringBuilder.append(" AND {").append(SavedValuesModel.MODIFIEDTIME).append("} > ?")
			.append(SavedValuesModel.MODIFIEDTIME);
			queryParameter.put(SavedValuesModel.MODIFIEDTIME, lastStartTime);
		}
		
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<SavedValuesModel> searchResult = flexibleSearchService.search(fQuery);
		for (final SavedValuesModel savedValueModel : searchResult.getResult())
		{
			final Set<SavedValueEntryModel> fr = savedValueModel.getSavedValuesEntries();
			for (final SavedValueEntryModel entryModel : fr)
			{
				if (StringUtils.isNotEmpty(entryModel.getModifiedAttribute())
						&& (entryModel.getModifiedAttribute().equals("recommendedProducts") || entryModel.getModifiedAttribute().equals("recommendedCategories")))
				{
					return product;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CatalogVersionModel> getAvailableCatalogVersionForCode(final String variantProductCode)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(GenericVariantProductModel.CATALOGVERSION);
		stringBuilder.append("} from {").append(GenericVariantProductModel._TYPECODE);
		stringBuilder.append("} where {").append(GenericVariantProductModel.CODE);
		stringBuilder.append("} = ?").append(GenericVariantProductModel.CODE);

		queryParameter.put(GenericVariantProductModel.CODE, variantProductCode);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<CatalogVersionModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CatalogVersionModel> getAvailableApprovedCatalogVersionForCode(final String variantProductCode)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(GenericVariantProductModel.CATALOGVERSION);
		stringBuilder.append("} from {").append(GenericVariantProductModel._TYPECODE);
		stringBuilder.append("} where {").append(GenericVariantProductModel.CODE);
		stringBuilder.append("} = ?").append(GenericVariantProductModel.CODE);
		stringBuilder.append(" AND {").append(GenericVariantProductModel.APPROVALSTATUS);
		stringBuilder.append("} = ({{SELECT {PK} from {ArticleApprovalStatus} where {code} = ?")
				.append(GenericVariantProductModel.APPROVALSTATUS);
		stringBuilder.append("}})");

		queryParameter.put(GenericVariantProductModel.CODE, variantProductCode);
		queryParameter.put(GenericVariantProductModel.APPROVALSTATUS, ArticleApprovalStatus.APPROVED.getCode());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<CatalogVersionModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CatalogVersionModel> getApprovedCatalogVersionForBaseProduct(final String baseProductCode)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(ProductModel.CATALOGVERSION);
		stringBuilder.append("} from {").append(ProductModel._TYPECODE);
		stringBuilder.append("} where {").append(ProductModel.CODE);
		stringBuilder.append("} = ?").append(ProductModel.CODE);
		stringBuilder.append(" AND {").append(ProductModel.APPROVALSTATUS);
		stringBuilder.append("} = ({{SELECT {PK} from {ArticleApprovalStatus} where {code} = ?")
				.append(GenericVariantProductModel.APPROVALSTATUS);
		stringBuilder.append("}})");

		queryParameter.put(ProductModel.CODE, baseProductCode);
		queryParameter.put(ProductModel.APPROVALSTATUS, ArticleApprovalStatus.APPROVED.getCode());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<CatalogVersionModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GenericVariantProductModel> getApprovedVariantProductsForBaseProduct(final ProductModel baseProduct,
			final CatalogVersionModel unapprovedForCatalogVersion)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(GenericVariantProductModel.PK);
		stringBuilder.append("} from {").append(GenericVariantProductModel._TYPECODE);
		stringBuilder.append("} where {").append(GenericVariantProductModel.BASEPRODUCT);
		stringBuilder.append("} = ?").append(GenericVariantProductModel.BASEPRODUCT);
		stringBuilder.append(" AND {").append(GenericVariantProductModel.APPROVALSTATUS);
		stringBuilder.append("} = ({{SELECT {PK} from {ArticleApprovalStatus} where {code} = ?")
				.append(GenericVariantProductModel.APPROVALSTATUS);
		stringBuilder.append("}})");

		queryParameter.put(GenericVariantProductModel.BASEPRODUCT, baseProduct);
		queryParameter.put(GenericVariantProductModel.APPROVALSTATUS, ArticleApprovalStatus.APPROVED.getCode());

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<GenericVariantProductModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MediaContainerModel getMediaContainerForQualifier(final String qualifier, final CatalogVersionModel catalogVersion)
	{
		final Map<String, Object> queryParameter = new HashMap<>();
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Select {").append(MediaContainerModel.PK);
		stringBuilder.append("} from {").append(MediaContainerModel._TYPECODE);
		stringBuilder.append("} where {").append(MediaContainerModel.QUALIFIER);
		stringBuilder.append("} = ?").append(MediaContainerModel.QUALIFIER);
		stringBuilder.append(" AND {").append(MediaContainerModel.CATALOGVERSION);
		stringBuilder.append("} = ?").append(MediaContainerModel.CATALOGVERSION);

		queryParameter.put(MediaContainerModel.CATALOGVERSION, catalogVersion);
		queryParameter.put(MediaContainerModel.QUALIFIER, qualifier);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(stringBuilder.toString());
		fQuery.addQueryParameters(queryParameter);
		final SearchResult<MediaContainerModel> searchResult = flexibleSearchService.search(fQuery);

		if (!CollectionUtils.isEmpty(searchResult.getResult()))
		{
			return searchResult.getResult().get(0);
		}

		return null;
	}
}
