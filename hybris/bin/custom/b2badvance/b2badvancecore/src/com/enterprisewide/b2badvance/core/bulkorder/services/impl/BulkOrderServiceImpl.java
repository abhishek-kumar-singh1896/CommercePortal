/**
 *
 */
package com.enterprisewide.b2badvance.core.bulkorder.services.impl;

import com.enterprisewide.b2badvance.core.bulkorder.services.BulkOrderService;
import com.google.common.base.Preconditions;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService.Nothing;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import org.fest.util.Strings;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 */
public class BulkOrderServiceImpl implements BulkOrderService
{
	private ProductService productService;

	private CommerceStockService commerceStockService;
	private BaseStoreService baseStoreService;

	private ProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductSearchPageData<SolrSearchQueryData, SearchResultValueData>> productSearchService;

	private ImpersonationService impersonationService;

	private Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder;

	private BaseSiteService baseSiteService;

	private CommonI18NService commonI18NService;

	private final int PAGE_SIZE = 20;

	@Override
	public List<ProductModel> getProductsForQuery(final String productCode, final CatalogVersionModel catalogVersion)
	{
		return getProductsForQuery(productCode, catalogVersion, PAGE_SIZE);
	}

	@Override
	public List<ProductModel> getProductsForQuery(final String queryString, final int pageSize)
	{
		final CatalogVersionModel catalogVersionModel = getCatalogVersionForBaseSite();
		return getProductsForQuery(queryString, catalogVersionModel, pageSize);
	}

	private List<ProductModel> getProductsForQuery(final String queryString, final CatalogVersionModel catalogVersion,
                                                   final int pageSize)
	{
		Preconditions.checkNotNull(catalogVersion, "catalogversion should not be null");
		if (!Strings.isEmpty(queryString))
		{

			final ImpersonationContext context = new ImpersonationContext();
			final List<CatalogVersionModel> targetCatalogVersionModels = new ArrayList<CatalogVersionModel>();

			targetCatalogVersionModels.add(catalogVersion);
			context.setCatalogVersions(targetCatalogVersionModels);


			final ProductSearchPageData<SolrSearchQueryData, ProductModel> productSearchPageData = getImpersonationService()
					.executeInContext(context,
							new ImpersonationService.Executor<ProductSearchPageData<SolrSearchQueryData, ProductModel>, Nothing>()
							{
								@Override
								public ProductSearchPageData<SolrSearchQueryData, ProductModel> execute() throws Nothing
								{
									final PageableData pageableData = createPageableData(0, pageSize, null);

									final SearchStateData searchState = new SearchStateData();
									final SearchQueryData searchQueryData = new SearchQueryData();
									searchQueryData.setValue(queryString);
									searchState.setQuery(searchQueryData);
									final ProductSearchPageData<SolrSearchQueryData, SearchResultValueData> searchResult = getProductSearchService()
											.searchAgain(decodeState(searchState, null), pageableData);
									final ProductSearchPageData<SolrSearchQueryData, ProductModel> productResults = new ProductSearchPageData<SolrSearchQueryData, ProductModel>();
									productResults.setResults(buildProducts(searchResult.getResults(), catalogVersion));
									populateSearchPageData(searchResult, productResults);
									return productResults;
								}
							});
			return productSearchPageData.getResults();
		}
		return Collections.emptyList();
	}


	protected List<ProductModel> buildProducts(final List<SearchResultValueData> results,
                                               final CatalogVersionModel catalogVersionModel)
	{
		final List<ProductModel> products = new ArrayList<ProductModel>(results.size());

		for (final SearchResultValueData source : results)
		{
			final Map<String, Object> map = source.getValues();
			final ProductModel target = getProductService().getProductForCode(catalogVersionModel, (String) map.get("code"));
			products.add(target);
		}

		return products;
	}

	protected void populateSearchPageData(final ProductSearchPageData<SolrSearchQueryData, SearchResultValueData> source,
			final ProductSearchPageData<SolrSearchQueryData, ProductModel> target)
	{
		target.setCurrentQuery(source.getCurrentQuery());
		target.setPagination(source.getPagination());
		target.setFacets(source.getFacets());
		target.setBreadcrumbs(source.getBreadcrumbs());
		target.setSorts(source.getSorts());
		target.setSpellingSuggestion(source.getSpellingSuggestion());
	}

	protected <R, T extends Throwable> R executeInContext(final ImpersonationContext context,
			final ImpersonationService.Executor<R, T> wrapper) throws Throwable
	{
		return getImpersonationService().executeInContext(context, wrapper);
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		pageableData.setPageSize(pageSize);
		return pageableData;
	}

	protected SolrSearchQueryData decodeState(final SearchStateData searchState, final String categoryCode)
	{
		final SolrSearchQueryData searchQueryData = getSearchQueryDecoder().convert(searchState.getQuery());
		if (categoryCode != null)
		{
			searchQueryData.setCategoryCode(categoryCode);
		}
		return searchQueryData;
	}

	@Override
	public ProductModel getProductByCode(final String productCode, final CatalogVersionModel catalogVersion)
			throws IllegalArgumentException
	{
		try
		{
			return getProductService().getProductForCode(catalogVersion, productCode);
		}
		catch (final UnknownIdentifierException e)
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see au.com.ciber.facades.bulkorder.services.BulkOrderService#getStockForProduct(java.lang.String)
	 */
	@Override
	public String getStockForProduct(final String productCode, final CatalogVersionModel catalogVersion)
	{
		final ProductModel productModel = getProductByCode(productCode, catalogVersion);
		final Long stock = productModel != null ? getCommerceStockService().getStockLevelForProductAndBaseStore(productModel,
				getBaseStoreService().getCurrentBaseStore()) : null;
		return stock != null ? stock.toString() : "";
	}

	/**
	 * Getting catalog version model from BaseSiteService
	 *
	 * @return CatalogVersionModel, null if no current base site selected
	 */
	@Override
	public CatalogVersionModel getCatalogVersionForBaseSite()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		if (baseSite != null)
		{
			final List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs(baseSite);
			if (!productCatalogs.isEmpty())
			{
				final CatalogVersionModel catVerModel = productCatalogs.get(0).getActiveCatalogVersion();
				return catVerModel;
			}
		}
		return null;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the commerceStockService
	 */
	public CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	/**
	 * @param commerceStockService
	 *           the commerceStockService to set
	 */
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
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

	protected BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected Converter<SearchQueryData, SolrSearchQueryData> getSearchQueryDecoder()
	{
		return searchQueryDecoder;
	}

	@Required
	public void setSearchQueryDecoder(final Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder)
	{
		this.searchQueryDecoder = searchQueryDecoder;
	}

	protected ImpersonationService getImpersonationService()
	{
		return this.impersonationService;
	}

	@Required
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}

	protected ProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductSearchPageData<SolrSearchQueryData, SearchResultValueData>> getProductSearchService()
	{
		return productSearchService;
	}

	@Required
	public void setProductSearchService(
			final ProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductSearchPageData<SolrSearchQueryData, SearchResultValueData>> productSearchService)
	{
		this.productSearchService = productSearchService;
	}
}
