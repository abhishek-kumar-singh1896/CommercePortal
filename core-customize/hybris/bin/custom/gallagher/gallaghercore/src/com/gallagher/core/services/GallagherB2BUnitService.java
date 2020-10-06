/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;


/**
 * @author ankituniyal
 *
 */
public interface GallagherB2BUnitService extends B2BUnitService<B2BUnitModel, B2BCustomerModel>
{

	List<B2BUnitModel> getAllB2BUnits(CustomerModel customer);

	/**
	 * @param defaultB2BUnit
	 * @return
	 */
	Pair<BaseSiteModel, BaseStoreModel> getBaseSiteAndStoreForUnit(B2BUnitModel defaultB2BUnit);
}
