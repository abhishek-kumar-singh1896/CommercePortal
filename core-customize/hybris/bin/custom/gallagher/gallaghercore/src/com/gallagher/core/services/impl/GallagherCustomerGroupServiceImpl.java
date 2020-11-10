/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.gallagher.core.dao.GallagherB2BUnitDao;
import com.gallagher.core.services.GallagherCustomerGroupService;


/**
 * @author ankituniyal
 *
 */
public class GallagherCustomerGroupServiceImpl implements GallagherCustomerGroupService
{

	private static final String CURRENT_SESSION_CUSTOMER_GROUP = "currentSessionCustomerGroup";

	private SessionService sessionService;

	private GallagherB2BUnitDao b2bUnitDao;

	@Override
	public String getCurrentCustomerGroup()
	{
		return getSessionService().getAttribute(CURRENT_SESSION_CUSTOMER_GROUP);
	}

	@Override
	public Set<String> getAllCustomerGroups()
	{
		final List<B2BUnitModel> b2bUnits = getB2bUnitDao().getAllB2BUnits();
		final Set<String> salesAreas = new HashSet<>();
		if (CollectionUtils.isNotEmpty(b2bUnits))
		{
			b2bUnits.stream().forEach(unit -> {
				if (StringUtils.isNotBlank(unit.getCustomerGroup()))
				{
					salesAreas.add(unit.getCustomerGroup());
				}
			});
		}
		return salesAreas;
	}

	@Override
	public void setCurrentCustomerGroupInSession(final String salesArea)
	{
		getSessionService().setAttribute(CURRENT_SESSION_CUSTOMER_GROUP, salesArea);
	}

	/**
	 * @return the b2bUnitDao
	 */
	public GallagherB2BUnitDao getB2bUnitDao()
	{
		return b2bUnitDao;
	}

	/**
	 * @param b2bUnitDao
	 *           the b2bUnitDao to set
	 */
	public void setB2bUnitDao(final GallagherB2BUnitDao b2bUnitDao)
	{
		this.b2bUnitDao = b2bUnitDao;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}
