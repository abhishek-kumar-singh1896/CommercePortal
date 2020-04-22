/**
 *
 */
package com.gallagher.facades;

import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * @author anujmanocha
 *
 */
public interface GallagherB2BUnitFacade extends B2BUnitFacade
{
	SearchPageData<CustomerData> getPagedTechniciansForUnit(final PageableData pageableData, final String unitUid);

}
