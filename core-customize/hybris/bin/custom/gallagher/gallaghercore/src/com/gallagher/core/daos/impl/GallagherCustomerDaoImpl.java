/**
 *
 */
package com.gallagher.core.daos.impl;


import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gallagher.core.daos.GallagherCustomerDao;


/**
 * Dao for fetching customer by keycloak GUID
 *
 * @author abhishek
 */
public class GallagherCustomerDaoImpl implements GallagherCustomerDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<CustomerModel> retrieveUserByKeycloakGUID(final String subjectId)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT {").append(CustomerModel.PK);
		queryBuilder.append("} from {").append(CustomerModel._TYPECODE);
		queryBuilder.append("} where {").append(CustomerModel.KEYCLOAKGUID);
		queryBuilder.append("} = ?").append(CustomerModel.KEYCLOAKGUID);

		final Map<String, String> queryParams = new HashMap<>(1);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryBuilder.toString());
		queryParams.put(CustomerModel.KEYCLOAKGUID, subjectId);
		fQuery.addQueryParameters(queryParams);

		final SearchResult<CustomerModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult();
	}

}
