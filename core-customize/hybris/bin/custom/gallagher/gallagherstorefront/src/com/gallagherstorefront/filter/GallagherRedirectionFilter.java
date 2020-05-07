/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagherstorefront.filter;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.net.InetAddress;

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

import com.gallagher.core.enums.RegionCode;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;


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

	private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

	private static final String LOCALHOST_IPV4 = "127.0.0.1";

	private static final String IP_SERVICE = "https://ipinfo.io/%s/country";

	private static final Logger LOG = LoggerFactory.getLogger(GallagherRedirectionFilter.class.getName());

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

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
		if (requestURI.contains(SECURITY))
		{
			base = SECURITY_BASE;
		}
		else
		{
			base = AM_BASE;
		}

		final String remoteAddr = getClientIPAddress(req);

		final String countryCode;
		if (StringUtils.isNotEmpty(remoteAddr) && !isLocalHost(remoteAddr))
		{
			countryCode = getCountryofIPAddress(remoteAddr.split(",")[0]);
		}
		else
		{
			countryCode = "Global";
		}

		//final String countryCode = req.getLocale().getCountry();

		final StringBuilder countrySite = new StringBuilder(base).append(countryCode);
		BaseSiteModel site = baseSiteService.getBaseSiteForUID(countrySite.toString());
		if (site == null)
		{
			final CountryModel country = commonI18NService.getCountry(countryCode.trim());
			final RegionCode regionCode = country.getRegionCode();
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
	 * Returns the country for the IP address
	 *
	 * @param remoteAddr
	 *           IP address
	 * @return country ISO code
	 */
	private String getCountryofIPAddress(final String remoteAddr)
	{
		String countryCode = GLOBAL;
		final int maxMindAccountID = configurationService.getConfiguration().getInt("maxmind.account.id", 0);
		final String maxMindKey = configurationService.getConfiguration().getString("maxmind.account.key", DEFAULT_VALUE);
		if (StringUtils.isNotEmpty(maxMindKey) && maxMindAccountID != 0)
		{
			try (WebServiceClient client = new WebServiceClient.Builder(maxMindAccountID, maxMindKey).build())
			{

				final InetAddress ipAddress = InetAddress.getByName(remoteAddr);
				final CountryResponse response = client.country(ipAddress);
				final Country country = response.getCountry();
				countryCode = country.getIsoCode();
			}
			catch (IOException | GeoIp2Exception ex)
			{
				LOG.error("Exception occurred while fetching country for redirection", ex);
			}
		}
		return countryCode;
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
		return LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress);
	}
}