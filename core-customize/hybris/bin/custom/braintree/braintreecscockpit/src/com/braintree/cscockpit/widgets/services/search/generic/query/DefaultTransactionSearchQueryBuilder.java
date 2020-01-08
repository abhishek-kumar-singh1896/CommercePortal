package com.braintree.cscockpit.widgets.services.search.generic.query;

import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;


public class DefaultTransactionSearchQueryBuilder extends AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextSearchCommand>
{
	@Override
	protected FlexibleSearchQuery buildFlexibleSearchQuery(final DefaultCsTextSearchCommand defaultCsTextSearchCommand)
	{
		final StringBuilder query = new StringBuilder(300);
		query.append("SELECT DISTINCT {o:pk}, {o:code} ");
		query.append("FROM {Order AS o }");

		return new FlexibleSearchQuery(query.toString());
	}

	public enum TextField
	{
		START_DATE, END_DATE, TRANSACTION_ID, TRANSACTION_STATUS, CUSTOMER_ID, CUSTOMER_EMAIL, RISK_DECISION;
	}

}
