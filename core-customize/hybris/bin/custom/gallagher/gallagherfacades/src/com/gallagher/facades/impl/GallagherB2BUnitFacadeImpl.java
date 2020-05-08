/**
 *
 */
package com.gallagher.facades.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUnitFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import com.gallagher.facades.GallagherB2BUnitFacade;


/**
 * @author anujmanocha
 *
 */
public class GallagherB2BUnitFacadeImpl extends DefaultB2BUnitFacade implements GallagherB2BUnitFacade
{


	public SearchPageData<CustomerData> getPagedTechniciansForUnit(final PageableData pageableData, final String unitUid)
	{
		final SearchPageData<CustomerData> searchPageData = this.getPagedUserDataForUnit(pageableData, unitUid);
		// update the results with users that already have been selected.
		final B2BUnitData unit = this.getUnitForUid(unitUid);
		validateParameterNotNull(unit, String.format("No unit found for uid %s", unitUid));
		for (final CustomerData userData : searchPageData.getResults())
		{
			userData.setSelected(CollectionUtils.find(unit.getTechnicians(),
					new BeanPropertyValueEqualsPredicate(B2BCustomerModel.UID, userData.getUid())) != null);
		}

		return searchPageData;
	}

	@Override
	public List<String> getAllActiveUnitsOfOrganization()
	{
		final B2BCustomerModel currentUser = (B2BCustomerModel) this.getUserService().getCurrentUser();
		final Set<B2BUnitModel> units = getB2BUnitService().getAllUnitsOfOrganization(currentUser);
		final List<String> b2BUnitList = new ArrayList<String>(units.size());
		for (final B2BUnitModel b2BUnitModel : units)
		{
			if (Boolean.TRUE.equals(b2BUnitModel.getActive()))
			{
				b2BUnitList.add(b2BUnitModel.getName());
			}
		}
		return b2BUnitList;
	}

}
