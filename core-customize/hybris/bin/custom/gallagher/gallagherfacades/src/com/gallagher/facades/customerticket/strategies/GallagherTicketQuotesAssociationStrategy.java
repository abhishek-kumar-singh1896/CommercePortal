package com.gallagher.facades.customerticket.strategies;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerticketingfacades.data.TicketAssociatedData;
import de.hybris.platform.customerticketingfacades.strategies.TicketAssociationStrategies;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Custom strategy for ticket association for quotes.
 *
 * @author Alvin Yap
 */
public class GallagherTicketQuotesAssociationStrategy implements TicketAssociationStrategies
{

	private Converter<QuoteModel, TicketAssociatedData> ticketAssociationCoverter;

	@Override
	public Map<String, List<TicketAssociatedData>> getObjects(final UserModel currentUser)
	{
		final List<TicketAssociatedData> quoteList = new ArrayList<TicketAssociatedData>(
				Converters.convertAll(currentUser.getQuotes(), getTicketAssociationCoverter()));

		final Map<String, List<TicketAssociatedData>> quotes = new HashMap<String, List<TicketAssociatedData>>();
		quotes.put("Quote", quoteList);


		return quoteList.isEmpty() ? Collections.emptyMap() : quotes;
	}

	/**
	 * @return the ticketAssociationCoverter
	 */
	protected Converter<QuoteModel, TicketAssociatedData> getTicketAssociationCoverter()
	{
		return ticketAssociationCoverter;
	}

	/**
	 * @param ticketAssociationCoverter
	 *           the ticketAssociationCoverter to set
	 */
	@Required
	public void setTicketAssociationCoverter(final Converter<QuoteModel, TicketAssociatedData> ticketAssociationCoverter)
	{
		this.ticketAssociationCoverter = ticketAssociationCoverter;
	}
}
