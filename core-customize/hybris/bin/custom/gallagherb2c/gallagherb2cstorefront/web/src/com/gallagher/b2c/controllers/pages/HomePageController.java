/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/")
public class HomePageController extends AbstractPageController
{
	private static final String LOGOUT = "logout";
    private static final String UNKNOWN = "unknown";
	private static final String ACCOUNT_CONFIRMATION_SIGNOUT_TITLE = "account.confirmation.signout.title";
	private static final String ACCOUNT_CONFIRMATION_CLOSE_TITLE = "account.confirmation.close.title";
	private static final Logger LOG = LoggerFactory.getLogger(HomePageController.class.getName());

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "storeSessionFacade")
	StoreSessionFacade storeSessionFacade;;

	@Resource(name = "siteConfigService")
	SiteConfigService siteConfigService;

	@RequestMapping(method = RequestMethod.GET)
	public String home(@RequestParam(value = WebConstants.CLOSE_ACCOUNT, defaultValue = "false")
	final boolean closeAcc, @RequestParam(value = LOGOUT, defaultValue = "false")
	final boolean logout, final Model model, final RedirectAttributes redirectModel,final ServletRequest request) throws CMSItemNotFoundException
	{

		final HttpServletRequest req = (HttpServletRequest) request;
		final String remoteAddr = getClientIPAddress(req);

		String sitecoreIPAddresses = siteConfigService.getString("sitecore.backend.ips", "");

		if (sitecoreIPAddresses== null || !sitecoreIPAddresses.contains(remoteAddr)) {
			final String siteCoreUrl = new StringBuilder(siteConfigService.getString(new StringBuilder("sitecore.root.url").append(".")
					.append(storeSessionFacade.getCurrentLanguage().getIsocode()).toString(), "#")).toString();
			LOG.info("Redirecting the request to {} as request doesn't come from Sitecore backend ({})", siteCoreUrl, remoteAddr);
			return REDIRECT_PREFIX + siteCoreUrl;
		}
		else
		{
			final String urlRedirect = new StringBuilder(req.getRequestURI()).append("/home").toString();
			LOG.info("Redirecting the request to Commerce /homepage as request comes from Sitecore backend ({})", remoteAddr);
			return REDIRECT_PREFIX + "/homepage";
		}
	}


	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}

	/**
	 * Returns the IP address of client
	 *
	 * @param request
	 * @return IP address
	 */
	private String getClientIPAddress(final HttpServletRequest request)
	{
		String remoteAddr = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr))
		{
			remoteAddr = request.getHeader("Proxy-Client-remoteAddr");
		}
		if (StringUtils.isEmpty(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr))
		{
			remoteAddr = request.getHeader("WL-Proxy-Client-remoteAddr");
		}
		if (StringUtils.isEmpty(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr))
		{
			remoteAddr = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr))
		{
			remoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr))
		{
			remoteAddr = request.getRemoteAddr();
		}
		return remoteAddr;
	}
}
