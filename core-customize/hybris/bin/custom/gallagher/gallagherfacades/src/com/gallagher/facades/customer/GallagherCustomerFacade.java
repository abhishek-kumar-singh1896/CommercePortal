/**
 *
 */
package com.gallagher.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;

import com.gallagher.core.dtos.GallagherAccessToken;


/**
 * Gallagher facade for passing Access token
 *
 * to service layer from UI
 *
 * @author abhishek
 */
public interface GallagherCustomerFacade extends CustomerFacade
{
	void updateCommerceCustomer(final GallagherAccessToken token);
}
