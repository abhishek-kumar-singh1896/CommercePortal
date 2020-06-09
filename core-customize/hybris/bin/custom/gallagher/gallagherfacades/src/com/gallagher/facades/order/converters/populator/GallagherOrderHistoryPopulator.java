/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.session.SessionService;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.util.GallagherSiteUtil;


/**
 * Order history populator extending OOTB populator with custom fields
 *
 * @author Vikram Bishnoi
 */
public class GallagherOrderHistoryPopulator extends OrderHistoryPopulator
{

	private SessionService sessionService;

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);
		target.setFormattedOrderDate(GallagherSiteUtil.getFormattedDateWithTimeZoneForDate(source.getDate(),
				GallagherCoreConstants.GGL_ORDER_DATE_FORMAT, sessionService.getAttribute(GallagherCoreConstants.GGL_TIMEZONE)));

	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

