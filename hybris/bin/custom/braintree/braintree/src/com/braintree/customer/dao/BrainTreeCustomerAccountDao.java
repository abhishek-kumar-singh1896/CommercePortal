/**
 *
 */
package com.braintree.customer.dao;

import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.braintree.model.BrainTreePaymentInfoModel;


public class BrainTreeCustomerAccountDao extends DefaultCustomerAccountDao
{
	private final static Logger LOG = Logger.getLogger(BrainTreeCustomerAccountDao.class);

	public BrainTreePaymentInfoModel findBrainTreePaymentInfoByCustomer(final CustomerModel customerModel, final String code)
	{
		ServicesUtil.validateParameterNotNull(customerModel, "Customer must not be null");
		final Map queryParams = new HashMap();
		queryParams.put("customer", customerModel);
		queryParams.put("duplicate", Boolean.FALSE);
		queryParams.put("pk", PK.parse(code));
		final SearchResult result = getFlexibleSearchService().search(
				"SELECT {pk} FROM {BrainTreePaymentInfo} WHERE {user} = ?customer AND {pk} = ?pk AND {duplicate} = ?duplicate",
				queryParams);
		return ((result.getCount() > 0) ? (BrainTreePaymentInfoModel) result.getResult().get(0) : null);
	}

	public List<BrainTreePaymentInfoModel> findBrainTreePaymentInfosByCustomer(final CustomerModel customerModel,
			final boolean saved, final String accountId)
	{
		final StringBuilder queryBySaved = new StringBuilder(
				"SELECT {pk} FROM {BrainTreePaymentInfo} WHERE {user} = ?customer AND {saved} = ?saved ");
		ServicesUtil.validateParameterNotNull(customerModel, "Customer must not be null");
		final Map queryParams = new HashMap();
		queryParams.put("customer", customerModel);
		if (saved)
		{
			queryParams.put("saved", Boolean.TRUE);
			if (accountId != null)
			{
				queryParams.put("accountId", accountId);
				queryBySaved.append("AND {merchantAccountIdForCurrentSite} = ?accountId ");
			}
			else
			{
				LOG.error("[Get Payment Infos ERROR] Merchant account ID is null! Search default payment infos... "
						+ "Please check configuration of property: braintree.merchant.account.ids");
			}

		}
		queryBySaved.append("AND {duplicate} = ?duplicate");
		queryParams.put("duplicate", Boolean.FALSE);
		final SearchResult result = getFlexibleSearchService().search(
				(saved) ? queryBySaved.toString()
						: "SELECT {pk} FROM {BrainTreePaymentInfo} WHERE {user} = ?customer AND {duplicate} = ?duplicate", queryParams);
		return result.getResult();
	}

	public CustomerModel findCustomerByBrainTreeCustomerId(final String customerId)
	{
		ServicesUtil.validateParameterNotNull(customerId, "Customer must not be null");
		final Map queryParams = new HashMap();
		queryParams.put("customerId", customerId);

		final SearchResult result = getFlexibleSearchService().search(
				"SELECT {pk} FROM {Customer} WHERE {braintreeCustomerId} = ?customerId", queryParams);
		return ((result.getCount() > 0) ? (CustomerModel) result.getResult().get(0) : null);
	}
}
