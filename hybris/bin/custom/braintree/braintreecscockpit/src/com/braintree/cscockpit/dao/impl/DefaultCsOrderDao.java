package com.braintree.cscockpit.dao.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.braintree.cscockpit.dao.CsOrderDao;


public class DefaultCsOrderDao extends DefaultOrderDao implements CsOrderDao
{

	@Override
	public OrderModel findOrderByBraintreeTransactionID(String braintreeTransactionID)
	{
		final StringBuilder query = new StringBuilder(300);

		query.append("SELECT DISTINCT {o:pk}, {o.code} ");
		query.append("FROM {Order AS o} WHERE {o:PK} IN ");
		query.append("({{SELECT DISTINCT {pt.order} FROM {PaymentTransaction AS pt} WHERE {pt:PK} IN ");
		query.append("( {{SELECT DISTINCT {pte.paymentTransaction} FROM {PaymentTransactionEntry AS pte} WHERE {pte.requestId} = ?braintreeTransactionID}}) }})");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("braintreeTransactionID", braintreeTransactionID);
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), queryParams);

		final SearchResult searchResult = getFlexibleSearchService().search(searchQuery);
		List result = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(result))
		{
			return (OrderModel) result.get(0);
		}
		return null;
	}
}
