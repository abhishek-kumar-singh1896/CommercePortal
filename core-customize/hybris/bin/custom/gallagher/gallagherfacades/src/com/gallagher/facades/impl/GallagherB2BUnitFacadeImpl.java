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
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import com.gallagher.core.services.GallagherB2BUnitService;
import com.gallagher.facades.GallagherB2BUnitFacade;


/**
 * @author anujmanocha
 *
 */
public class GallagherB2BUnitFacadeImpl extends DefaultB2BUnitFacade implements GallagherB2BUnitFacade
{

	@Resource(name = "gallagherB2BUnitService")
	protected GallagherB2BUnitService b2bUnitService;

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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getActiveUnitsOfOrganizationMap()
	{
		final B2BCustomerModel currentUser = (B2BCustomerModel) this.getUserService().getCurrentUser();
		final Set<B2BUnitModel> units = getB2BUnitService().getAllUnitsOfOrganization(currentUser);
		final Map<String, String> b2BUnits = new HashMap<>(units.size());
		for (final B2BUnitModel b2BUnitModel : units)
		{
			if (Boolean.TRUE.equals(b2BUnitModel.getActive()))
			{
				b2BUnits.put(b2BUnitModel.getUid(), b2BUnitModel.getName());
			}
		}
		return b2BUnits;
	}

	public List<B2BUnitModel> getAllB2BUnits(final CustomerModel customer)
	{
		return getB2bUnitService().getAllB2BUnits(customer);

	}

	public void updateBranchInSession(final Session session, final UserModel currentUser)
	{
		getB2BUnitService().updateBranchInSession(getSessionService().getCurrentSession(), currentUser);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gallagher.facades.GallagherB2BUnitFacade#getAllB2BData(de.hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public List<B2BUnitData> getAllB2BData(final CustomerModel currentCustomer)
	{
		final List<B2BUnitModel> b2bUnitList = getB2bUnitService().getAllB2BData(currentCustomer);
		final List<B2BUnitData> b2bUnitData = new ArrayList();


		for (final B2BUnitModel b2bModel : b2bUnitList)
		{
			final B2BUnitData unitData = getB2BUnitConverter().convert(b2bModel);
			unitData.setCode(unitData.getUid());
			b2bUnitData.add(unitData);
		}
		return b2bUnitData;
	}



	/**
	 * @return the b2bUnitService
	 */
	public GallagherB2BUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}


	/**
	 * @param b2bUnitService
	 *           the b2bUnitService to set
	 */
	public void setB2bUnitService(final GallagherB2BUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}
}
