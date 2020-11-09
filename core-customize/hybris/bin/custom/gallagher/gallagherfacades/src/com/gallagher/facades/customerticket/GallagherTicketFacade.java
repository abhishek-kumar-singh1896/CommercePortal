package com.gallagher.facades.customerticket;

import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;

import java.util.List;


/**
 * Custom implementation of {@link TicketFacade}
 *
 * @author Alvin Yap
 */
public interface GallagherTicketFacade extends TicketFacade
{

	/**
	 * @return a map with available ticket categories in string representation for the customer ticket creation as a key
	 *         and it's localizations as a value.
	 */
	List<TicketCategory> getTicketCategories();

}
