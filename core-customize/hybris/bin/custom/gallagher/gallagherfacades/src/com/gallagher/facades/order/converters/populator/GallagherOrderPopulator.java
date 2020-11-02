/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.springframework.util.Assert;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.util.GallagherSiteUtil;


/**
 * Order populator extending OOTB populator with custom fields
 *
 * @author Vikram Bishnoi.
 */
public class GallagherOrderPopulator extends OrderPopulator
{

	private SessionService sessionService;

	@Override
	protected void addDetails(final OrderModel source, final OrderData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		super.addDetails(source, target);
		target.setFormattedOrderDate(GallagherSiteUtil.getFormattedDateWithTimeZoneForDate(source.getDate(),
				GallagherCoreConstants.GGL_ORDER_DATE_FORMAT, sessionService.getAttribute(GallagherCoreConstants.GGL_TIMEZONE)));
		target.setDeliveryInstruction(source.getDeliveryInstruction());
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
