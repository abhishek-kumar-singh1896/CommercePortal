package com.gallagher.core.url.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.net.InetAddress;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;


/**
 * @author vikram1798
 *
 */
public class GallagherLocationUtil
{

	private static final String GLOBAL = "Global";
	private static final String DEFAULT_VALUE = "";
	private static final Logger LOG = Logger.getLogger(GallagherLocationUtil.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * Returns the country for the IP address
	 *
	 * @param remoteAddr
	 *           IP address
	 * @return country ISO code
	 */
	public String getCountryofIPAddress(final String remoteAddr)
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
	 * Returns the timezone for the IP address
	 *
	 * @param remoteAddr
	 *           IP address
	 * @return timezone
	 */
	public TimeZone getTimezoneOfIPAddress(final String remoteAddr)
	{
		TimeZone timeZone = TimeZone.getDefault();
		final int maxMindAccountID = configurationService.getConfiguration().getInt("maxmind.account.id", 0);
		final String maxMindKey = configurationService.getConfiguration().getString("maxmind.account.key", DEFAULT_VALUE);
		if (StringUtils.isNotEmpty(maxMindKey) && maxMindAccountID != 0)
		{
			try (WebServiceClient client = new WebServiceClient.Builder(maxMindAccountID, maxMindKey).build())
			{

				final InetAddress ipAddress = InetAddress.getByName(remoteAddr);
				final String timeZoneStr = client.city(ipAddress).getLocation().getTimeZone();
				timeZone = TimeZone.getTimeZone(timeZoneStr);
			}
			catch (IOException | GeoIp2Exception ex)
			{
				LOG.error("Exception occurred while fetching country for redirection", ex);
			}
		}
		return timeZone;
	}
}
