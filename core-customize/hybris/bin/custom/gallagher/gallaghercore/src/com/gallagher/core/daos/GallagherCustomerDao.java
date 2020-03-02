/**
 *
 */
package com.gallagher.core.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;


/**
 * Dao for fetching customer by keycloak GUID
 *
 * @author abhishek
 */
public interface GallagherCustomerDao
{
	List<CustomerModel> retrieveUserByKeycloakGUID(final String subjectId);
}
