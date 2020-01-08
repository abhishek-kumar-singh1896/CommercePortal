/**
 *
 */
package com.braintree.cscockpit.widgets.services.search.impl;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cscockpit.model.data.impl.DefaultDataObject;
import de.hybris.platform.cscockpit.services.search.CsSearchCommand;
import de.hybris.platform.cscockpit.services.search.CsSearchResult;
import de.hybris.platform.cscockpit.services.search.CsSearchService;
import de.hybris.platform.cscockpit.services.search.Pageable;
import de.hybris.platform.cscockpit.services.search.SearchException;
import de.hybris.platform.cscockpit.services.search.impl.AbstractCsSearchService;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsSearchResult;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.jalo.JaloSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.services.search.generic.query.DefaultTransactionSearchQueryBuilder;
import com.braintree.hybris.data.BraintreeTransactionEntryData;


public class BraintreeTransactionCsSearchService extends AbstractCsSearchService implements CsSearchService
{

	private CsBrainTreeFacade csBrainTreeFacade;

	@Override
	public CsSearchResult search(final CsSearchCommand searchCommand, final Pageable pageable) throws SearchException
	{
		final DefaultCsSearchResult searchResult = new DefaultCsSearchResult();
		searchResult.setSearchCommand(searchCommand);
		searchResult.setPageable(pageable);
		final List items = searchTransactionEntries(searchCommand, pageable);
		searchResult.setTotalResultCount(items.size());
		searchResult.setResult(createResultMetaData(limitByPage(items, pageable), searchResult));

		return searchResult;
	}

	private List limitByPage(final List items, final Pageable pageable)
	{
		final int start = pageable.getPageNumber() * pageable.getPageSize();
		final int count = pageable.getPageSize();
		final int end = items.size() > start + count ? start + count : items.size();

		return items.subList(start, end);
	}

	@Override
	protected List createResultMetaData(final List items, final Object providerSearchResult)
	{
		final ArrayList metaItems = new ArrayList(items.size());
		final Iterator it = items.iterator();

		while (it.hasNext())
		{
			final BraintreeTransactionEntryData item = (BraintreeTransactionEntryData) it.next();
			metaItems.add(new DefaultDataObject(item));
		}

		populateMetaData(metaItems, providerSearchResult);
		return metaItems;
	}

	private List<BraintreeTransactionEntryData> searchTransactionEntries(final CsSearchCommand searchCommand,
			final Pageable pageable) throws SearchException
	{
		final TimeZone userTimeZone = JaloSession.getCurrentSession().getSessionContext().getTimeZone();
		final Calendar now = Calendar.getInstance(userTimeZone);
		final Calendar defaultStartDate = Calendar.getInstance(userTimeZone);
		defaultStartDate.set(Calendar.DATE, -1);

		final Calendar startDate = getInputDate(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.START_DATE,
				defaultStartDate);
		final Calendar endDate = getInputDate(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.END_DATE, now);

		final String customer = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID);
		final String customerEmail = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL);
		final String transactionId = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_ID);
		final String transactionStatus = getInputString(searchCommand,
				DefaultTransactionSearchQueryBuilder.TextField.TRANSACTION_STATUS);
		final String riskDecision = getInputString(searchCommand, DefaultTransactionSearchQueryBuilder.TextField.RISK_DECISION);

		return getCsBrainTreeFacade().findBrainTreeTransactions(startDate, endDate, customer, customerEmail, transactionId,
				transactionStatus, riskDecision);
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

	private Calendar getInputDate(final CsSearchCommand searchCommand,
			final DefaultTransactionSearchQueryBuilder.TextField fieldName, final Calendar defaultDate) throws SearchException
	{
		Calendar inputDate = defaultDate;
		if (searchCommand instanceof DefaultCsTextSearchCommand)
		{
			try
			{
				final TimeZone userTimeZone = JaloSession.getCurrentSession().getSessionContext().getTimeZone();
				final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();

				final String fieldValue = ((DefaultCsTextSearchCommand) searchCommand).getText(fieldName);

				final DateFormat dateFormat = new SimpleDateFormat(BraintreeConstants.TRANSACTION_SEARCH_DATE_FORMAT,
						userLocale);

				if (StringUtils.isNotBlank(fieldValue))
				{
					final Date date = dateFormat.parse(fieldValue);
					inputDate = Calendar.getInstance(userTimeZone);
					inputDate.setTime(date);
				}
			}
			catch (final ParseException e)
			{
				throw new SearchException(e);
			}
		}
		return inputDate;
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
