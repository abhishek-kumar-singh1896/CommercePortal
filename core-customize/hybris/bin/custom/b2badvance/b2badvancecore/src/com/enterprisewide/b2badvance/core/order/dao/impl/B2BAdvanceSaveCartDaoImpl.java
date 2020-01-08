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
package com.enterprisewide.b2badvance.core.order.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.impl.DefaultSaveCartDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.order.dao.B2BAdvanceSaveCartDao;


/**
 * B2B Advance dao implementation for handling the saved cart feature
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSaveCartDaoImpl extends DefaultSaveCartDao implements B2BAdvanceSaveCartDao
{
	protected final static String SELECTCLAUSE = "SELECT {" + CartModel.PK + "} FROM {" + CartModel._TYPECODE + " as cart} ";

	protected final static String SAVED_CARTS_CLAUSE = "{" + CartModel.SAVETIME + "} IS NOT NULL and {orderTemplate}=0";

	protected final static String ORDER_TEMPLATES_CLAUSE = "{" + CartModel.SAVETIME + "} IS NOT NULL and {orderTemplate}=1";

	protected final static String FIND_ORDER_TEMPLATES_FOR_USER = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND "
			+ ORDER_TEMPLATES_CLAUSE + " ";

	protected final static String FIND_ORDER_TEMPLATES_FOR_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER
			+ "} = ?user AND {" + CartModel.SITE + "} = ?site AND " + ORDER_TEMPLATES_CLAUSE + " ";

	protected final static String ORDER_TEMPLATES_TOTAL_FOR_USER_AND_SITE = "SELECT COUNT({" + CartModel.PK + "}) FROM {"
			+ CartModel._TYPECODE + "} WHERE {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE + "} = ?site AND "
			+ ORDER_TEMPLATES_CLAUSE;

	protected final static String ORDER_TEMPLATES_TOTAL_FOR_USER = "SELECT COUNT({" + CartModel.PK + "}) FROM {"
			+ CartModel._TYPECODE + "} WHERE {" + CartModel.USER + "} = ?user AND " + ORDER_TEMPLATES_CLAUSE;

	protected final static String FIND_SAVED_CARTS_FOR_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER
			+ "} = ?user AND {" + CartModel.SITE + "} = ?site AND " + SAVED_CARTS_CLAUSE + " ";

	protected final static String FIND_SAVED_CARTS_FOR_USER = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND "
			+ SAVED_CARTS_CLAUSE + " ";

	protected final static String FIND_EXPIRED_SAVED_CARTS_FOR_SITE = SELECTCLAUSE + "WHERE {" + CartModel.SITE + "} = ?site AND "
			+ SAVED_CARTS_CLAUSE + " AND {" + CartModel.EXPIRATIONTIME + "} <= ?currentDate " + ORDERBYCLAUSE;

	protected final static String FIND_SAVED_CARTS_FOR_SITE_AND_USER_WITH_STATUS = "SELECT {" + CartModel.PK + "} FROM {"
			+ CartModel._TYPECODE + "}, {" + OrderStatus._TYPECODE + "} " + "WHERE {" + CartModel._TYPECODE + "." + CartModel.STATUS
			+ "} = {" + OrderStatus._TYPECODE + ".pk} AND {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE + "} = ?site AND "
			+ SAVED_CARTS_CLAUSE + " AND {OrderStatus.CODE} in (?orderStatus) ";

	protected final static String FIND_SAVED_CARTS_FOR_USER_WITH_STATUS = "SELECT {" + CartModel.PK + "} FROM {"
			+ CartModel._TYPECODE + "}, {" + OrderStatus._TYPECODE + "} " + "WHERE {" + CartModel._TYPECODE + "." + CartModel.STATUS
			+ "} = {" + OrderStatus._TYPECODE + ".pk} AND {" + CartModel.USER + "} = ?user AND " + SAVED_CARTS_CLAUSE
			+ " AND {OrderStatus.CODE} in (?orderStatus) ";

	protected final static String SAVED_CARTS_TOTAL_FOR_USER_AND_SITE = "SELECT COUNT({" + CartModel.PK + "}) FROM {"
			+ CartModel._TYPECODE + "} WHERE {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE + "} = ?site AND "
			+ SAVED_CARTS_CLAUSE;

	protected final static String SAVED_CARTS_TOTAL_FOR_USER = "SELECT COUNT({" + CartModel.PK + "}) FROM {" + CartModel._TYPECODE
			+ "} WHERE {" + CartModel.USER + "} = ?user AND " + SAVED_CARTS_CLAUSE;

	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CartModel> getSavedCartsForRemovalForSite(final BaseSiteModel site)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("currentDate", new Date());


		return doSearch(FIND_EXPIRED_SAVED_CARTS_FOR_SITE, params, CartModel.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getSavedCartsCountForSiteAndUser(final BaseSiteModel baseSite, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);

		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			query = SAVED_CARTS_TOTAL_FOR_USER_AND_SITE;
		}
		else
		{
			query = SAVED_CARTS_TOTAL_FOR_USER;
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		fQuery.setResultClassList(Collections.singletonList(Integer.class));

		final SearchResult<Integer> searchResult = search(fQuery);

		return searchResult.getResult().get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	/** @Override
	public de.hybris.platform.commerceservices.search.pagedata.SearchPageData<CartModel> getSavedCartsForSiteAndUser(
			final PageableData pageableData, final BaseSiteModel baseSite, final UserModel user, final List<OrderStatus> orderStatus)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);

		final String orderStatusList = formatOrderStatusList(orderStatus);
		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			if (StringUtils.isNotBlank(orderStatusList))
			{
				params.put("orderStatus", orderStatusList);
				query = FIND_SAVED_CARTS_FOR_SITE_AND_USER_WITH_STATUS;
			}
			else
			{
				query = FIND_SAVED_CARTS_FOR_USER_AND_SITE;
			}
		}
		else
		{
			if (StringUtils.isNotBlank(orderStatusList))
			{
				params.put("orderStatus", orderStatusList);
				query = FIND_SAVED_CARTS_FOR_USER_WITH_STATUS;
			}
			else
			{
				query = FIND_SAVED_CARTS_FOR_USER;
			}
		}

		final List<SortQueryData> sortQueries = Arrays.asList(
				createSortQueryData(SORT_CODE_BY_DATE_MODIFIED, query + ORDERBYCLAUSE),
				createSortQueryData(SORT_CODE_BY_DATE_SAVED, query + SORT_SAVED_CARTS_BY_DATE_SAVED),
				createSortQueryData(SORT_CODE_BY_NAME, query + SORT_SAVED_CARTS_BY_NAME),
				createSortQueryData(SORT_CODE_BY_CODE, query + SORT_SAVED_CARTS_BY_CODE),
				createSortQueryData(SORT_CODE_BY_TOTAL, query + SORT_SAVED_CARTS_BY_TOTAL));

		return getPagedFlexibleSearchService().search(sortQueries, SORT_CODE_BY_DATE_MODIFIED, params, pageableData);
	}**/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOrderTemplatesCountForSiteAndUser(final BaseSiteModel baseSite, final UserModel user)
	{

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);

		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			query = ORDER_TEMPLATES_TOTAL_FOR_USER_AND_SITE;
		}
		else
		{
			query = ORDER_TEMPLATES_TOTAL_FOR_USER;
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(params);
		fQuery.setResultClassList(Collections.singletonList(Integer.class));

		final SearchResult<Integer> searchResult = search(fQuery);

		return searchResult.getResult().get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<CartModel> getOrderTemplatesForSiteAndUser(final PaginationData pagination, final BaseSiteModel baseSite,
			final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);

		final String query;
		if (baseSite != null)
		{
			params.put("site", baseSite);
			query = FIND_ORDER_TEMPLATES_FOR_USER_AND_SITE;
		}
		else
		{
			query = FIND_ORDER_TEMPLATES_FOR_USER;
		}

		List<SortData> sortDataList = null;
		if (StringUtils.isEmpty(pagination.getSortCode()))
		{
			sortDataList = new ArrayList<>(1);
			sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.MODIFIEDTIME, false));
		}
		else
		{
			sortDataList = new ArrayList<>(2);
			switch (pagination.getSortCode())
			{
				case CartModel.MODIFIEDTIME:
					sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.MODIFIEDTIME, false));
					break;
				case CartModel.SAVETIME:
					sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.SAVETIME, false));
					break;
				case CartModel.NAME:
					sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.NAME, true));
					break;
				case CartModel.CODE:
					sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.CODE, true));
					break;
				case CartModel.TOTALPRICE:
					sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.TOTALPRICE, true));
					break;
			}
		}

		final SearchPageData searchPageData = new SearchPageData();
		searchPageData.setSorts(sortDataList);
		searchPageData.setPagination(pagination);

		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(CartModel.MODIFIEDTIME.toLowerCase(), "cart");
		sortCodeToQueryAlias.put(CartModel.SAVETIME.toLowerCase(), "cart");
		sortCodeToQueryAlias.put(CartModel.NAME.toLowerCase(), "cart");
		sortCodeToQueryAlias.put(CartModel.CODE.toLowerCase(), "cart");
		sortCodeToQueryAlias.put(CartModel.TOTALPRICE.toLowerCase(), "cart");

		parameter.setFlexibleSearchQuery(new FlexibleSearchQuery(query, params));
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
		parameter.setSearchPageData(searchPageData);
		final SearchPageData<CartModel> result = getPaginatedFlexibleSearchService().search(parameter);

		result.setSorts(createTemplateSorts());
		result.getPagination().setSortCode(pagination.getSortCode());
		return result;
	}

	/**
	 * Creates sort attributes for templates
	 *
	 * @return list of sort codes
	 */
	private List<SortData> createTemplateSorts()
	{
		final List<SortData> sortDataList = new ArrayList<>(5);
		sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.MODIFIEDTIME, false));
		sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.SAVETIME, false));
		sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.NAME, true));
		sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.CODE, true));
		sortDataList.add(PaginatedSearchUtils.createSortData(CartModel.TOTALPRICE, true));
		return sortDataList;
	}

	protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
	{
		return paginatedFlexibleSearchService;
	}

	@Required
	public void setPaginatedFlexibleSearchService(final PaginatedFlexibleSearchService paginatedFlexibleSearchService)
	{
		this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
	}
}
