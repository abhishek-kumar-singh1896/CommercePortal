/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.services.GallagherB2BUnitService;

/**
 * @author ankituniyal
 *
 */
public class GallagherB2BUnitServiceImpl extends DefaultB2BUnitService implements GallagherB2BUnitService
{

	private static final String SECURITY_B2B_GLOBAL = "securityB2BGlobal";
	private static final String SECURITY_B2B = "securityB2B";
	private static final String SECURITY = "security";

	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private BaseSiteService baseSiteService;

	@Override
	public List<B2BUnitModel> getAllB2BUnits(final CustomerModel customer)
	{
		final Set<PrincipalGroupModel> customerGroups = customer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final Set<B2BUnitModel> rootB2BUnits = new HashSet<>();
		for (final PrincipalGroupModel group : customerGroups)
		{
			if (group instanceof B2BUnitModel)
			{
				rootB2BUnits.add(getRootUnit((B2BUnitModel) group));
				b2bUnitList.add((B2BUnitModel) group);
			}
		}
		if (rootB2BUnits.size() > 1)
		{
			return b2bUnitList;
		}
		else
		{
			b2bUnitList.clear();
			return b2bUnitList;
		}
	}

	@Override
	public Pair<BaseSiteModel, BaseStoreModel> getBaseSiteAndStoreForUnit(final B2BUnitModel defaultB2BUnit)
	{
		BaseSiteModel defaultSite = null;
		BaseStoreModel defaultBaseStore = null;

		if (CollectionUtils.isEmpty(defaultB2BUnit.getAddresses()))
		{
			defaultBaseStore = getBaseStoreService().getBaseStoreForUid(SECURITY_B2B_GLOBAL);
			defaultSite = getBaseSiteService().getBaseSiteForUID(SECURITY_B2B_GLOBAL);
		}
		else
		{
			final List<BaseStoreModel> baseStores = getBaseStoreService().getAllBaseStores();
			final String isoCode = defaultB2BUnit.getAddresses().iterator().next().getCountry().getIsocode();
			boolean flag = false;
			for (final BaseStoreModel baseStore : baseStores)
			{
				if (baseStore.getUid().startsWith(SECURITY) && baseStore.getUid().contains(isoCode))
				{
					String securityB2BisoCode = SECURITY_B2B;
					securityB2BisoCode = securityB2BisoCode.concat(isoCode);
					defaultBaseStore = getBaseStoreService().getBaseStoreForUid(securityB2BisoCode);
					defaultSite = getBaseSiteService().getBaseSiteForUID(securityB2BisoCode);
					flag = true;
					break;
				}
			}
			if (!flag)
			{
				defaultBaseStore = getBaseStoreService().getBaseStoreForUid(SECURITY_B2B_GLOBAL);
				defaultSite = getBaseSiteService().getBaseSiteForUID(SECURITY_B2B_GLOBAL);
			}
		}

		return Pair.of(defaultSite, defaultBaseStore);
	}

	@Override
	public List<B2BUnitModel> getAllB2BData(final CustomerModel currentCustomer)
	{
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) currentCustomer;
		int i = 0;
		boolean flag = false;
		String selectedUnit = null;
		final B2BUnitModel unitModel = getSessionService().getAttribute("selectedB2BUnit");
		if (unitModel != null)
		{
			selectedUnit = unitModel.getUid();
		}

		List<B2BUnitModel> rootNodes = null;
		final Set<PrincipalGroupModel> customerGroups = currentCustomer.getGroups();

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
				if (StringUtils.equals(unit.getUid(), selectedUnit) && getRootUnit(unit).equals(unit))//If parent unit then add child unit also in list
				{
					for (final PrincipalGroupModel group : customerGroups)
					{
						if (group instanceof B2BUnitModel && getRootUnit((B2BUnitModel) group).equals(unit))
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
		return b2bUnitList;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}



}
