/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.b2b.storefront.interceptors.beforeview;

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
		modelAndView.addObject("gtagId", siteConfigService.getString("gtag.tracking.id", ""));
		modelAndView.addObject("hotjarId", siteConfigService.getString("hotjar.tracking.id", ""));
		modelAndView.addObject(ANALYTICS_TRACKING_ID,
				siteConfigService.getString(ThirdPartyConstants.Google.ANALYTICS_TRACKING_ID, ""));
		modelAndView.addObject("siteCoreTagUrl", siteConfigService.getString("security.sitecore.tag.url", ""));
	}
}
