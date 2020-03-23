/**
 *
 */
package com.gallagher.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;

import com.gallagher.core.dtos.GallagherAccessToken;


/**
 * Gallagher facade for custom operations
 *
 * @author Abhishek Kumar
 */
public interface GallagherCustomerFacade extends CustomerFacade
{
	/**
	 * Updates customer by fetching the details from Keycloak and C4C
	 *
	 * @param token
	 *           to get the customer
	 * @param createIfNotExists
	 *           if user is not available then create a new User (B2C). For B2B only update will be performed
	 */
	void updateCommerceCustomer(final GallagherAccessToken token, final boolean createIfNotExists);
}
