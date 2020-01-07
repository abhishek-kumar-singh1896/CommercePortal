package com.enterprisewide.b2badvance.core.invoice.dao.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.invoice.dao.B2BAdvanceInvoiceDao;
import com.enterprisewide.b2badvance.core.model.B2BAdvanceInvoiceModel;


/**
 * Default implementation of {@link B2BAdvanceInvoiceDao}.
 *
 * Dao to retrieve invoice related data
 */
public class B2BAdvanceInvoiceDaoImpl extends DefaultGenericDao<B2BAdvanceInvoiceModel> implements B2BAdvanceInvoiceDao
{

	private static final String SELECTCLAUSE = "SELECT {" + B2BAdvanceInvoiceModel.PK + "} FROM {"
			+ B2BAdvanceInvoiceModel._TYPECODE + " as invoice} ";

	private static final String FIND_INVOICES_FOR_USER = SELECTCLAUSE + "WHERE {" + B2BAdvanceInvoiceModel.CUSTOMER + "} = ?user";
	private static final String INVOICE = "invoice";

	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	public B2BAdvanceInvoiceDaoImpl()
	{
		super(B2BAdvanceInvoiceModel._TYPECODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public B2BAdvanceInvoiceModel getInvoice(final String invoiceNumber)
	{
		final List<B2BAdvanceInvoiceModel> models = this
				.find(Collections.singletonMap(B2BAdvanceInvoiceModel.INVOICENUMBER, invoiceNumber));
		return (models.iterator().hasNext() ? models.iterator().next() : null);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<B2BAdvanceInvoiceModel> getInvoicesForCurrentUser(final PaginationData pagination, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("user", user);
		final String query = FIND_INVOICES_FOR_USER;
		List<SortData> sortDataList = null;
		if (StringUtils.isEmpty(pagination.getSortCode()))
		{
			sortDataList = new ArrayList<>(1);
			sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.DUEDATE, false));
		}
		else
		{
			sortDataList = new ArrayList<>(1);
			switch (pagination.getSortCode())
			{

				case B2BAdvanceInvoiceModel.INVOICENUMBER:
					sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.INVOICENUMBER, true));
					break;
				case B2BAdvanceInvoiceModel.TOTALAMOUNT:
					sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.TOTALAMOUNT, true));
					break;
				default:
					sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.DUEDATE, false));
					break;
			}
		}

		final SearchPageData searchPageData = new SearchPageData();
		searchPageData.setSorts(sortDataList);
		searchPageData.setPagination(pagination);

		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(B2BAdvanceInvoiceModel.DUEDATE.toLowerCase(), INVOICE);
		sortCodeToQueryAlias.put(B2BAdvanceInvoiceModel.INVOICENUMBER.toLowerCase(), INVOICE);
		sortCodeToQueryAlias.put(B2BAdvanceInvoiceModel.TOTALAMOUNT.toLowerCase(), INVOICE);

		parameter.setFlexibleSearchQuery(new FlexibleSearchQuery(query, params));
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
		parameter.setSearchPageData(searchPageData);
		final SearchPageData<B2BAdvanceInvoiceModel> result = getPaginatedFlexibleSearchService().search(parameter);

		result.setSorts(invoiceSorts());
		result.getPagination().setSortCode(pagination.getSortCode());
		return result;
	}

	/**
	 * Creates sort attributes for invoices
	 *
	 * @return list of sort codes
	 */
	private List<SortData> invoiceSorts()
	{
		final List<SortData> sortDataList = new ArrayList<>(3);
		sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.DUEDATE, false));
		sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.INVOICENUMBER, true));
		sortDataList.add(PaginatedSearchUtils.createSortData(B2BAdvanceInvoiceModel.TOTALAMOUNT, true));
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
