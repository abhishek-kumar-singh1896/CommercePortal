/**
 *
 */
package com.gallagher.core.services;

import java.util.List;

import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.dtos.GallagherRegisteredProductDto;


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



	/**
	 * Gets list of registered products for logged in user from C4C
	 */
	List<GallagherRegisteredProductDto> getRegisteredProductsFromC4C();
}
