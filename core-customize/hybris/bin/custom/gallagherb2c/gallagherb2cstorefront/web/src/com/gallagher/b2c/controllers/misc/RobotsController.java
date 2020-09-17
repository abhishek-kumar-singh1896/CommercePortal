/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.b2c.controllers.ControllerConstants;


/**
 * Controller for web robots instructions
 */
@Controller
public class RobotsController extends AbstractController
{
	// Number of seconds in one day
	private static final String ONE_DAY = String.valueOf(60 * 60 * 24);
	public static final String FORWARD_PREFIX = "forward:";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	public String getRobots(final HttpServletResponse response)
	{
		// Add cache control header to cache response for a day
		response.setHeader("Cache-Control", "public, max-age=" + ONE_DAY);
		if (isAllowRobots())
		{
			return ControllerConstants.Views.Pages.Misc.MiscRobotsPage;
		}
		else
		{
			return FORWARD_PREFIX + "/404";
		}
	}

	/**
	 * Checks whether robots are allowed for the current environment.
	 *
	 * @return true if they are allowed
	 */
	private boolean isAllowRobots()
	{
		return configurationService.getConfiguration().getBoolean("allow.regional.robots", Boolean.TRUE);
	}
}
