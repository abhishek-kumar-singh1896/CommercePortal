/**
 *
 */
package com.gallagher.core.services;

import com.gallagher.core.dtos.GallagherAccessToken;


/**
 * Gallagher customer service interface
 *
 * update customer in commerce and SCPI/C4C
 *
 * @author abhishek
 */
public interface GallagherCustomerService
{
	boolean updateCommerceCustomer(final GallagherAccessToken token);
}
