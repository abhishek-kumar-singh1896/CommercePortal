/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagherstorefront.filter;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.enums.RegionCode;
import com.gallagher.core.services.GallagherLocationService;


/**
 * Filter to redirect website to default B2C or B2B website. Gallagher has two websites securityB2B and amB2C which are
 * available for multiple countries. For each country there is a separate CMS Site means there are multiple website
 * URLs. This filter facilitates User to use a generic URL like am.gallagher.com instead of am.gallagher.com/us/en.
 *
 * @author Vikram Bishnoi
 */
public class GallagherRedirectionFilter extends GenericFilterBean
{

	private static final String DEFAULT_VALUE = "";

	private static final String SLASH = "/";

	private static final String UNKNOWN = "unknown";

	private static final String AM_BASE = "amB2C";

	private static final String GLOBAL = "Global";

	private static final String SECURITY = "security";

	private static final String SECURITY_BASE = "securityB2B";

	private static final String IP_SERVICE = "https://ipinfo.io/%s/country";

	private static final Logger LOG = LoggerFactory.getLogger(GallagherRedirectionFilter.class.getName());

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "gallagherLocationService")
	private GallagherLocationService gallagherLocationService;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{

		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;

		final String requestURI = req.getServerName();
		String base = null;
		boolean isSecurity = false;
		if (requestURI.contains(SECURITY))
		{
			base = SECURITY_BASE;
			isSecurity = true;
		}
		else
		{
			base = AM_BASE;
		}

		final String remoteAddr = getClientIPAddress(req);

		final String countryCode;
		if (StringUtils.isEmpty(remoteAddr) || isLocalHost(remoteAddr))
		{
			countryCode = "Global";
		}
		else
		{
			countryCode = gallagherLocationService.getCountryofIPAddress(remoteAddr.split(",")[0]);
		}

		//final String countryCode = req.getLocale().getCountry();

		final StringBuilder countrySite = new StringBuilder(base).append(countryCode);
		BaseSiteModel site = baseSiteService.getBaseSiteForUID(countrySite.toString());
		if (site == null)
		{
			final CountryModel country = commonI18NService.getCountry(countryCode.trim());
			final RegionCode regionCode = isSecurity ? country.getSecurityRegionCode() : country.getRegionCode();
			if (regionCode != null)
			{
				final StringBuilder regionSite = new StringBuilder(base).append(regionCode.getCode().toUpperCase());
				site = baseSiteService.getBaseSiteForUID(regionSite.toString());
			}
		}
		if (site == null)
		{
			final StringBuilder globalSite = new StringBuilder(base).append(GLOBAL);
			site = baseSiteService.getBaseSiteForUID(globalSite.toString());
		}
		String redirectURL = siteBaseUrlResolutionService.getWebsiteUrlForSite(site, true, null);
		redirectURL = redirectURL + SLASH + ((CMSSiteModel) site).getRegionCode() + SLASH
				+ ((CMSSiteModel) site).getDefaultLanguage().getIsocode();
		LOG.info("Redirecting the request to {} for country {}. Detected IP is {}", redirectURL, countryCode, remoteAddr);
		res.sendRedirect(redirectURL);
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

	/**
	 * Returns true if IP is localhost i.e. 127.0.0.1 (IPV4) or 0:0:0:0:0:0:0:1 (IPV6)
	 *
	 * @param ipAddress
	 *           to be verified
	 * @return whether IP belongs to localhost or not
	 */
	private boolean isLocalHost(final String ipAddress)
	{
		return GallagherCoreConstants.LOCALHOST_IPV4.equals(ipAddress) || GallagherCoreConstants.LOCALHOST_IPV6.equals(ipAddress);
	}
}
