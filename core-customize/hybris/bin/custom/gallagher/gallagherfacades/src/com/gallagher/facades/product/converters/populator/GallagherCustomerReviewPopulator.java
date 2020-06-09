/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.CustomerReviewPopulator;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.session.SessionService;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.util.GallagherSiteUtil;


/**
 * Review populator extending OOTB populator with custom fields
 *
 * @author Vikram Bishnoi
 */
public class GallagherCustomerReviewPopulator extends CustomerReviewPopulator
{
	private SessionService sessionService;


	@Override
	public void populate(final CustomerReviewModel source, final ReviewData target)
	{
		super.populate(source, target);
		target.setFormattedReviewDate(GallagherSiteUtil.getFormattedDateWithTimeZoneForDate(source.getCreationtime(), "dd/MM/yyyy",
				sessionService.getAttribute(GallagherCoreConstants.GGL_TIMEZONE)));

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
