/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SiteChannel;

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
	 * @param channel
	 *           if user is not available then create a new User (B2C). For B2B only update will be performed
	 */
	boolean updateCommerceCustomer(final GallagherAccessToken token, final SiteChannel channel);

	void changeUid(final String newUid) throws DuplicateUidException;
}
