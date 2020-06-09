package com.gallagher.core.services.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.net.InetAddress;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gallagher.core.services.GallagherLocationService;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;


/**
 * Location service to get location specific details i.e. Country/Timezone for an IP address
 *
 * @author Vikram Bishnoi
 *
 */
public class GallagherLocationServiceImpl implements GallagherLocationService
{

	private static final String GLOBAL = "Global";
	private static final String DEFAULT_VALUE = "";
	private static final Logger LOG = Logger.getLogger(GallagherLocationServiceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
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
