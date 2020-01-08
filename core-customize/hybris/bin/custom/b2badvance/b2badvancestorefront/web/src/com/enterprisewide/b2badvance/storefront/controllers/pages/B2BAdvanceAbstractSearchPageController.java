/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.storefront.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;


/**
 * B2B Advance Abstract Search page controller to provide various features with advanced hybris components like
 * PaginationData instead of PageableData etc.
 *
 * @author Enterprise Wide
 */
public abstract class B2BAdvanceAbstractSearchPageController extends AbstractSearchPageController
{
	public static final int MAX_PAGE_LIMIT = 100; // should be configured
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "pagination.number.results.count";
	private static final Logger LOG = Logger.getLogger(B2BAdvanceAbstractSearchPageController.class);
	private static final String FACET_SEPARATOR = ":";

	public enum ShowMode
	{
		// Constant names cannot be changed due to their usage in dependant extensions, thus nosonar
		Page, // NOSONAR
		All // NOSONAR
	}

	protected PaginationData createPaginationData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{

		PaginatedSearchUtils.createPaginationData(ShowMode.All == showMode ? 100 : 5, pageNumber, false);

		final PaginationData paginationData = new PaginationData();
		paginationData.setCurrentPage(pageNumber);
		paginationData.setSortCode(sortCode);
		paginationData.setNeedsTotal(true);
		if (ShowMode.All == showMode)
		{
			paginationData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			paginationData.setPageSize(pageSize);
		}
		return paginationData;
	}

	protected PaginationData createEmptyPaginationData()
	{
		final PaginationData paginationData = new PaginationData();
		paginationData.setCurrentPage(0);
		paginationData.setNumberOfPages(0);
		paginationData.setPageSize(1);
		paginationData.setTotalNumberOfResults(0);
		return paginationData;
	}


	/**
	 * Special case, when total number of results > {@link #MAX_PAGE_LIMIT}
	 */
	protected boolean isShowAllAllowed(final SearchPageData<?> searchPageData)
	{
		return searchPageData.getPagination().getNumberOfPages() > 1
				&& searchPageData.getPagination().getTotalNumberOfResults() < MAX_PAGE_LIMIT;
	}

	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT, 5);

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}

	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All && //
				searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf(showMode == ShowMode.All && (searchPageData.getPagination().getNumberOfPages() > 1
				|| searchPageData.getPagination().getPageSize() == getMaxSearchPageSize()));
	}
}