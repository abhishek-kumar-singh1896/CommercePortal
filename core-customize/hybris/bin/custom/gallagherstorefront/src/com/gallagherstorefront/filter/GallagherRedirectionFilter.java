/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagherstorefront.filter;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import com.gallagher.core.enums.RegionCode;


/**
 * Filter to redirect website to default B2C or B2B website. Gallagher has two websites securityB2B and amB2C which are
 * available for multiple countries. For each country there is a separate CMS Site means there are multiple website
 * URLs. This filter facilitates User to use a generic URL like am.gallagher.com instead of am.gallagher.com/us/en.
 *
 * @author Vikram Bishnoi
 */
public class GallagherRedirectionFilter extends GenericFilterBean
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherRedirectionFilter.class.getName());

	private BaseSiteService baseSiteService;

	private CommonI18NService commonI18NService;

	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{

		LOG.info("Pre Request actions...");
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;
		//		String userAddress = req.getHeader("X-FORWARDED-FOR");
		//		if (userAddress == null)
		//		{
		//			userAddress = request.getRemoteAddr();
		//		}
		//		else
		//		{
		//			if (userAddress.contains(","))
		//			{
		//				userAddress = userAddress.split(",")[0];
		//			}
		//		}
		//		LOG.debug("Client IP is {}", userAddress);
		//		final String countryCode = getCountryFromIPAddress(userAddress);

		final String requestURI = req.getRequestURI();
		String base = null;
		if (requestURI.contains("security"))
		{
			base = "securityB2B";
		}
		else
		{
			base = "amB2C";
		}

		final String countryCode = req.getLocale().getCountry();
		final StringBuilder countrySite = new StringBuilder(base).append(countryCode);
		BaseSiteModel site = getBaseSiteService().getBaseSiteForUID(countrySite.toString());
		if (site == null)
		{
			final CountryModel country = commonI18NService.getCountry(countryCode.trim());
			final RegionCode regionCode = country.getRegionCode();
			if (regionCode != null)
			{
				//TODO get site by regionCode
			}
		}
		if (site == null)
		{
			final StringBuilder globalSite = new StringBuilder(base).append("Global");
			site = getBaseSiteService().getBaseSiteForUID(globalSite.toString());
		}
		final String redirectURL = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(site, true, null);
		LOG.info("Redirecting the request to {} for country {}", redirectURL, countryCode);
		res.sendRedirect(redirectURL);
	}

	/**
	 * Returns website URL for redirection
	 *
	 * @param base
	 *           securityB2B or amB2C
	 * @param regionCode
	 *           for the country
	 * @return redirectURL i.e. website URL
	 */
	private String getWebsiteURL(final String base, final RegionCode regionCode)
	{
		final StringBuilder siteName = new StringBuilder(base).append(regionCode);

		final String redirectURL = getSiteBaseUrlResolutionService()
				.getWebsiteUrlForSite(getBaseSiteService().getBaseSiteForUID(siteName.toString()), true, null);

		return redirectURL;
	}

	/**
	 * Returns country from IP address
	 *
	 * @param userAddress/IPAddress
	 *           of the client request
	 * @return country ISO code
	 */
	private String getCountryFromIPAddress(final String userAddress)
	{
		final RestTemplate restTemplate = new RestTemplate();
		//final String url = "https://ipinfo.io/" + "72.229.28.185" + "/country";
		final String url = "https://ipinfo.io/" + userAddress + "/country";
		final String result = restTemplate.getForObject(url, String.class);
		return result;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}
}
