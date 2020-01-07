package com.braintree.cscockpit.widgets.services.search.meta.impl;

import de.hybris.platform.cscockpit.services.search.generic.CsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.meta.PostSearchMetaProcessor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class BraintreeCustomerPostProcessor implements PostSearchMetaProcessor
{
	private FlexibleSearchService flexibleSearchService;
	private CsFlexibleSearchQueryBuilder flexibleSearchQueryBuilder;

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the flexibleSearchQueryBuilder
	 */
	public CsFlexibleSearchQueryBuilder getFlexibleSearchQueryBuilder()
	{
		return flexibleSearchQueryBuilder;
	}

	/**
	 * @param flexibleSearchQueryBuilder
	 *           the flexibleSearchQueryBuilder to set
	 */
	@Required
	public void setFlexibleSearchQueryBuilder(final CsFlexibleSearchQueryBuilder flexibleSearchQueryBuilder)
	{
		this.flexibleSearchQueryBuilder = flexibleSearchQueryBuilder;
	}

	@Override
	public void populateMetaData(final List metaItems, final Object searchResult)
	{
		/*
		 * for (final Iterator it = metaItems.iterator(); it.hasNext();) { final DefaultDataObject metaItem =
		 * (DefaultDataObject) it.next(); final BraintreeCustomerEntryData customerEntry = (BraintreeCustomerEntryData)
		 * metaItem.getItem(); final ItemModel orderItem = (ItemModel) searchOrderByCustomer(customerEntry); if (orderItem
		 * != null) { metaItem.getMetaData().register(orderItem, ItemModel.class); } }
		 */
	}

	/*
	 * protected Object searchOrderByCutomer(final BraintreeTransactionEntryData entry) { final StringBuilder query = new
	 * StringBuilder(300);
	 * 
	 * query.append("SELECT DISTINCT {o:pk}, {o.code} "); query.append("FROM {Order AS o} WHERE {o:PK} IN " +
	 * "({{SELECT DISTINCT {pt.order} FROM {PaymentTransaction AS pt} WHERE {pt:PK} IN " +
	 * "( {{SELECT DISTINCT {pte.paymentTransaction} FROM {PaymentTransactionEntry AS pte} WHERE {pte.requestId} = ?requestId}}) }})"
	 * );
	 * 
	 * final Map<String, Object> queryParams = new HashMap<String, Object>(); queryParams.put("requestId",
	 * entry.getId()); final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), queryParams);
	 * 
	 * final SearchResult searchResult = getFlexibleSearchService().search(searchQuery); final List items =
	 * searchResult.getResult();
	 * 
	 * if (items != null && !items.isEmpty()) { return items.get(0); }
	 * 
	 * return null; }
	 */

}
