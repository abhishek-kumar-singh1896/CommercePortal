/**
 *
 */
package com.gallagher.core.dao.impl;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
}
