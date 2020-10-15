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

import com.gallagher.core.dao.GallagherSalesAreaDao;
import com.gallagher.core.services.GallagherSalesAreaService;

/**
 * @author ankituniyal
 *
 */
public class GallagherSalesAreaServiceImpl implements GallagherSalesAreaService
{

	private static final String CURRENT_SESSION_SALES_AREA = "currentSessionSalesArea";

	private SessionService sessionService;

	private GallagherSalesAreaDao salesAreaDao;

	@Override
	public String getCurrentSalesArea()
	{
		return getSessionService().getAttribute(CURRENT_SESSION_SALES_AREA);
	}

	@Override
	public Set<String> getAllSalesAreas()
	{
		final List<B2BUnitModel> b2bUnits = salesAreaDao.getAllB2BUnits();
		final Set<String> salesAreas = new HashSet<>();
		if (CollectionUtils.isNotEmpty(b2bUnits))
		{
			b2bUnits.stream().forEach(unit -> salesAreas.add(unit.getSalesArea()));
		}
		return salesAreas;
	}

	@Override
	public void setCurrentSalesAreaInSession(final String salesArea)
	{
		getSessionService().setAttribute(CURRENT_SESSION_SALES_AREA, salesArea);
	}

	/**
	 * @return the salesAreaDao
	 */
	public GallagherSalesAreaDao getSalesAreaDao()
	{
		return salesAreaDao;
	}

	/**
	 * @param salesAreaDao
	 *           the salesAreaDao to set
	 */
	public void setSalesAreaDao(final GallagherSalesAreaDao salesAreaDao)
	{
		this.salesAreaDao = salesAreaDao;
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
