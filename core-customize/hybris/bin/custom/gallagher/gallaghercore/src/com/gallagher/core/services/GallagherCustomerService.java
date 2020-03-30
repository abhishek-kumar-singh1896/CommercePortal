/**
 *
 */
package com.gallagher.core.services;

import com.gallagher.core.dtos.GallagherAccessToken;


/**
 *
 * Gallagher customer service for custom operations
 *
 * @author Abhishek Kumar
 */
public interface GallagherCustomerService
{
	/**
	 * Updates customer by fetching the details from Keycloak and C4C
	 *
	 * @param token
	 *           to get the customer
	 * @param createIfNotExists
	 *           if user is not available then create a new User (B2C). For B2B only update will be performed
	 */
	boolean updateCommerceCustomer(final GallagherAccessToken token, final boolean createIfNotExists);
}
