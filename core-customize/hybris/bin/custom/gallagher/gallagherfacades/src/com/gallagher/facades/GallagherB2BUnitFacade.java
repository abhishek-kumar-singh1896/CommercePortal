/**
 *
 */
package com.gallagher.facades;

import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Map;


/**
 * @author anujmanocha
 *
 */
public interface GallagherB2BUnitFacade extends B2BUnitFacade
{
	SearchPageData<CustomerData> getPagedTechniciansForUnit(final PageableData pageableData, final String unitUid);

	/**
	 * Returns the Map of all active units of organization.
	 *
	 * @return unit map where key is code and value is name
	 */
	Map<String, String> getActiveUnitsOfOrganizationMap();

}
