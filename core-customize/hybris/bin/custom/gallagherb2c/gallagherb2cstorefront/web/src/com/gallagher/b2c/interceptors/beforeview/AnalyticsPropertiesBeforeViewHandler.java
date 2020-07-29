/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


public class AnalyticsPropertiesBeforeViewHandler implements BeforeViewHandler
{
	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	private static final String ANALYTICS_TRACKING_ID = "googleAnalyticsTrackingId";
	private static final String GOOGLE_PREFIX = "googleAnalyticsTrackingId";

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		// Add config properties for google analytics and GTM
		modelAndView.addObject("gtmId", siteConfigService.getString("gtm.tracking.id", ""));
		modelAndView.addObject("commerceGTMId", siteConfigService.getString("gtm.commerce.tracking.id", ""));
		modelAndView.addObject("gtagId", siteConfigService.getString("gtag.tracking.id", ""));
		modelAndView.addObject("hotjarId", siteConfigService.getString("hotjar.tracking.id", ""));
		modelAndView.addObject(ANALYTICS_TRACKING_ID,
				siteConfigService.getString(ThirdPartyConstants.Google.ANALYTICS_TRACKING_ID, ""));
	}
}
