/**
 *
 */
package com.gallagher.facades;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.Session;

import java.util.List;
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

	List<B2BUnitModel> getAllB2BUnits(CustomerModel customer);

	void updateBranchInSession(final Session session, final UserModel currentUser);

	/**
	 * @param currentCustomer
	 * @return
	 */
	List<B2BUnitData> getAllB2BData(CustomerModel currentCustomer);

}
