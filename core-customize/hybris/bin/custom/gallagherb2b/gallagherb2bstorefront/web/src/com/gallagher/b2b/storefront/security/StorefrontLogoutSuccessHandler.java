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
package com.gallagher.b2b.storefront.security;

import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;



public class StorefrontLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

	private static final Logger LOGGER = Logger.getLogger(StorefrontLogoutSuccessHandler.class);
	private List<String> restrictedPages;
	private CMSSiteService cmsSiteService;
	private GUIDCookieStrategy guidCookieStrategy;
	private ConfigurationService configurationService;
	private UrlEncoderService urlEncoderService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	public UrlEncoderService getUrlEncoderService()
	{
		return urlEncoderService;
	}

	public void setUrlEncoderService(final UrlEncoderService urlEncoderService)
	{
		this.urlEncoderService = urlEncoderService;
	}



	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}


	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		getGuidCookieStrategy().deleteCookie(request, response);

		// Delegate to default redirect behaviour
		super.onLogoutSuccess(request, response, authentication);
	}

	/**
	 * Find CMSSiteModel from HttpServletRequest
	 *
	 * @param request
	 * @return CMS site associated with current URL pattern
	 */
	private CMSSiteModel getCMSSiteFromRequest(final HttpServletRequest request)
	{

		CMSSiteModel cmsSiteModel = null;
		final String queryString = request.getQueryString();
		final String currentRequestURL = request.getRequestURL().toString();

		final String absoluteURL = StringUtils.removeEnd(currentRequestURL, "/")
				+ (StringUtils.isBlank(queryString) ? "" : "?" + queryString);
		try
		{
			final URL currentURL = new URL(absoluteURL);
			cmsSiteModel = getCmsSiteService().getSiteForURL(currentURL);
		}
		catch (final MalformedURLException e)
		{
			LOGGER.warn(
					"Cannot find CMSSite associated with current URL ( " + absoluteURL + " - check whether this is correct URL) !", e);
		}
		catch (final CMSItemNotFoundException e)
		{
			LOGGER.warn("Cannot find CMSSite associated with current URL (" + absoluteURL + ")!", e);
		}
		return cmsSiteModel;
	}

	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		final String disableLogout = request.getParameter("disabled");
		String redirectURI;

		final String ssoLogout = request.getParameter("sso");
		String targetUrl;

		final CMSSiteModel site = getCMSSiteFromRequest(request);
		 if ((Boolean.valueOf(ssoLogout) && null != site) || Boolean.valueOf(disableLogout))
		{
			if (Boolean.valueOf(disableLogout))
			{
				redirectURI = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(site, true,
						StringUtils.isEmpty(getUrlEncoderService().getUrlEncodingPattern())
								? "/" + site.getRegionCode() + "/" + site.getDefaultLanguage().getIsocode()
								: null,
						"disable=true");
			}
			else
			{
				redirectURI = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(site, true,
					StringUtils.isEmpty(getUrlEncoderService().getUrlEncodingPattern())
							? "/" + site.getRegionCode() + "/" + site.getDefaultLanguage().getIsocode()
							: null,
					"error=true");
			}

			try
			{
				redirectURI = URLEncoder.encode(redirectURI, StandardCharsets.UTF_8.toString());
				targetUrl = MessageFormat.format(configurationService.getConfiguration().getString("keycloak.logout.url", ""),
						redirectURI);

			}
			catch (final UnsupportedEncodingException uEnEx)
			{
				LOGGER.error("Error while creating redirect URL for logout", uEnEx);
				targetUrl = super.determineTargetUrl(request, response);
			}
		}
		else
		{
			targetUrl = super.determineTargetUrl(request, response);

			for (final String restrictedPage : getRestrictedPages())
			{
				// When logging out from a restricted page, return user to homepage.
				if (targetUrl.contains(restrictedPage))
				{
					targetUrl = super.getDefaultTargetUrl();
				}
			}

		}
		return targetUrl;
	}
}
