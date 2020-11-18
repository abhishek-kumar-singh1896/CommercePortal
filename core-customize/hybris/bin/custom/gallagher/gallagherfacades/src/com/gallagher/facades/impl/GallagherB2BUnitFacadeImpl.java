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
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) currentCustomer;
		int i = 0;
		boolean flag = false;
		String selectedUnit = null;
		final B2BUnitModel unitModel = getSessionService().getAttribute("selectedB2BUnit");
		if (unitModel != null)
		{
			selectedUnit = unitModel.getUid();
		}
		final List<B2BUnitData> b2bUnitData = new ArrayList();
		List<B2BUnitModel> rootNodes = null;
		final Set<PrincipalGroupModel> customerGroups = currentCustomer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final Set<B2BUnitModel> rootB2BUnits = new HashSet<>();
		if (currentCustomer instanceof B2BCustomerModel)
		{
			rootNodes = getAllB2BUnits(currentCustomer);
		}
		if (rootNodes != null && rootNodes.size() == 0)//If Units From Same Hirearachy then add all the units to display in dropdown,since popup doesn't appear
		{
			final B2BUnitModel defaultUnit = b2bCustomer.getDefaultB2BUnit();
			if (defaultUnit != null)
			{
				defaultUnit.getUid();
			}
			for (final PrincipalGroupModel group : customerGroups)
			{
				if (group instanceof B2BUnitModel && group.equals(defaultUnit))
				{
					flag = true;
					b2bUnitList.add(0, ((B2BUnitModel) group));
				}
				else if (group instanceof B2BUnitModel)
				{
					if (flag == true)
					{
						b2bUnitList.add(++i, ((B2BUnitModel) group));
					}
					else
					{
						b2bUnitList.add(i++, ((B2BUnitModel) group));
					}
				}
			}
		}
		else
		{
			for (final B2BUnitModel unit : rootNodes)//If Units were from diiferenct hireachy then check if parent or child unit then add accordingly in list
			{
				if (StringUtils.equals(unit.getUid(), selectedUnit) && getB2BUnitService().getRootUnit(unit).equals(unit))//If parent unit then add child unit also in list
				{
					for (final PrincipalGroupModel group : customerGroups)
					{
						if (group instanceof B2BUnitModel && getB2BUnitService().getRootUnit((B2BUnitModel) group).equals(unit))
						{
							b2bUnitList.add((B2BUnitModel) group);
						}
					}
					break;
				}
				else if (StringUtils.equals(unit.getUid(), selectedUnit))//If only child unit then add what is selected in login
				{
					b2bUnitList.add(unit);
					break;
				}
			}
		}
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
