/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.services.search.impl;

import de.hybris.platform.cscockpit.model.data.impl.DefaultDataObject;
import de.hybris.platform.cscockpit.services.search.CsSearchCommand;
import de.hybris.platform.cscockpit.services.search.CsSearchResult;
import de.hybris.platform.cscockpit.services.search.CsSearchService;
import de.hybris.platform.cscockpit.services.search.Pageable;
import de.hybris.platform.cscockpit.services.search.SearchException;
import de.hybris.platform.cscockpit.services.search.impl.AbstractCsSearchService;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsSearchResult;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.services.search.generic.query.DefaultTransactionSearchQueryBuilder;
import com.braintreegateway.Customer;


public class BraintreeCustomerCsSearchService extends AbstractCsSearchService implements CsSearchService
{
	private CsBrainTreeFacade csBrainTreeFacade;

	@Override
	public CsSearchResult search(final CsSearchCommand searchCommand, final Pageable pageable) throws SearchException
	{
		final DefaultCsSearchResult searchResult = new DefaultCsSearchResult();
		searchResult.setSearchCommand(searchCommand);
		searchResult.setPageable(pageable);
		final List items = searchCustomerEntries(searchCommand);
		searchResult.setTotalResultCount(items.size());
		searchResult.setResult(createResultMetaData(limitByPage(items, pageable), searchResult));

		return searchResult;
	}

	@Override
	protected List createResultMetaData(final List items, final Object providerSearchResult)
	{
		final ArrayList metaItems = new ArrayList(items.size());
		final Iterator it = items.iterator();

		while (it.hasNext())
		{
			final Customer item = (Customer) it.next();
			metaItems.add(new DefaultDataObject(item));
		}

		populateMetaData(metaItems, providerSearchResult);
		return metaItems;
	}

	private List<Customer> searchCustomerEntries(final CsSearchCommand searchCommand) throws SearchException
	{
		final String customer = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID);
		final String customerEmail = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL);

		return getCsBrainTreeFacade().findCustomers(customer, customerEmail);
	}

	private List limitByPage(final List items, final Pageable pageable)
	{
		final int start = pageable.getPageNumber() * pageable.getPageSize();
		final int count = pageable.getPageSize();
		final int end = items.size() > start + count ? start + count : items.size();

		return items.subList(start, end);
	}

	private String getInputString(final CsSearchCommand searchCommand,
			final DefaultTransactionSearchQueryBuilder.TextField textField)
	{
		String fieldValue = null;
		if (searchCommand instanceof DefaultCsTextSearchCommand)
		{
			fieldValue = ((DefaultCsTextSearchCommand) searchCommand).getText(textField);
		}
		return fieldValue;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}
}
