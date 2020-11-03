/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.pages;


import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller for a category page
 */
@Controller
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{


	public static final String PRODUCT_CUSTOM_PAGE = "category/searchListSimple";

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;

	@Resource(name = "cmsPreviewService")
	private CMSPreviewService cmsPreviewService;


	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String category(@PathVariable("categoryCode")
	final String categoryCode, // NOSONAR
			@RequestParam(value = "q", required = false)
			final String searchQuery, @RequestParam(value = "page", defaultValue = "0")
			final int page, @RequestParam(value = "show", defaultValue = "Page")
			final ShowMode showMode, @RequestParam(value = "sort", required = false)
			final String sortCode, final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException
	{
		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);
		final String redirection = checkRequestUrl(request, response, getCategoryModelUrlResolver().resolve(category));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}
		final MediaModel media = category.getPicture();

		model.addAttribute("url", "");
		if (null != media && media.getMime().startsWith("image"))
		{
			model.addAttribute("url", category.getPicture().getURL());
			final String mime = media.getMime();
		}

		final String resultPage = performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode, model,
				request, response);
		return resultPage;
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode")
	final String categoryCode, @RequestParam(value = "q", required = false)
	final String searchQuery, @RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final ShowMode showMode, @RequestParam(value = "sort", required = false)
	final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
	}

	@ResponseBody
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode")
	final String categoryCode, @RequestParam(value = "q", required = false)
	final String searchQuery, @RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final ShowMode showMode, @RequestParam(value = "sort", required = false)
	final String sortCode) throws UnsupportedEncodingException
	{
		return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
	}

	//	@RequestMapping(value = "/simple" + CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/simpleGridPage", method = RequestMethod.GET)
	public String home(@PathVariable("categoryCode")
	final String categoryCode, // NOSONAR
			@RequestParam(value = "q", required = false)
			final String searchQuery, @RequestParam(value = "page", defaultValue = "0")
			final int page, @RequestParam(value = "show", defaultValue = "Page")
			final ShowMode showMode, @RequestParam(value = "sort", required = false)
			final String sortCode, final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws UnsupportedEncodingException
	{
		final String pagereturned = performCustonSearchAndGetResultPage(categoryCode, searchQuery, page, showMode, sortCode, model,
				request, response);
		return pagereturned;
	}

	String performCustonSearchAndGetResultPage(final String categoryCode, final String searchQuery, final int page, // NOSONAR
			final ShowMode showMode, final String sortCode, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws UnsupportedEncodingException
	{

		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);


		final CategoryPageModel categoryPage = getCategoryPage(category);

		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, searchQuery, page, showMode,
				sortCode, categoryPage);

		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		try
		{
			categorySearch.doSearch();
			searchPageData = categorySearch.getSearchPageData();
		}
		catch (final ConversionException e) // NOSONAR
		{
			searchPageData = createEmptySearchResult(categoryCode);
		}

		final boolean showCategoriesOnly = categorySearch.isShowCategoriesOnly();

		storeCmsPageInModel(model, categorySearch.getCategoryPage());
		storeContinueUrl(request);

		populateModel(model, searchPageData, showMode);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, searchPageData));
		model.addAttribute("showCategoriesOnly", Boolean.valueOf(showCategoriesOnly));
		model.addAttribute("categoryName", category.getName());
		model.addAttribute("pageType", PageType.CATEGORY.name());
		model.addAttribute("userLocation", getCustomerLocationService().getUserLocation());

		updatePageTitle(category, model);

		final RequestContextData requestContextData = getRequestContextData(request);
		requestContextData.setCategory(category);
		requestContextData.setSearch(searchPageData);

		if (searchQuery != null)
		{
			model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_FOLLOW);
		}

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(
				category.getKeywords().stream().map(keywordModel -> keywordModel.getKeyword()).collect(Collectors.toSet()));
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);

		return PAGE_ROOT + PRODUCT_CUSTOM_PAGE;


	}

	/**
	 * @return the productSearchFacade
	 */
	@Override
	public ProductSearchFacade<ProductData> getProductSearchFacade()
	{
		return productSearchFacade;
	}


	/**
	 * @return the commerceCategoryService
	 */
	@Override
	public CommerceCategoryService getCommerceCategoryService()
	{
		System.out.println("commerceCategoryService = " + commerceCategoryService);
		return commerceCategoryService;
	}


	/**
	 * @return the searchBreadcrumbBuilder
	 */
	@Override
	public SearchBreadcrumbBuilder getSearchBreadcrumbBuilder()
	{
		return searchBreadcrumbBuilder;
	}


	/**
	 * @return the categoryModelUrlResolver
	 */
	@Override
	public UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}


	/**
	 * @return the customerLocationService
	 */
	@Override
	public CustomerLocationService getCustomerLocationService()
	{
		return customerLocationService;
	}

	/**
	 * @return the cmsPreviewService
	 */
	@Override
	public CMSPreviewService getCMSPreviewService()
	{
		return cmsPreviewService;
	}

}
