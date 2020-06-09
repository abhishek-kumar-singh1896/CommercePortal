package com.gallagher.core.services;

import java.util.TimeZone;


/**
 * Location service to get location specific details i.e. Country/Timezone for an IP address
 *
 * @author Vikram Bishnoi
 *
 */
public interface GallagherLocationService
{

	/**
	 * Returns the country for the IP address
	 *
	 * @param remoteAddr
	 *           IP address
	 * @return country ISO code
	 */
	String getCountryofIPAddress(final String remoteAddr);

	/**
	 * Returns the timezone for the IP address
	 *
	 * @param remoteAddr
	 *           IP address
	 * @return timezone
	 */
	TimeZone getTimezoneOfIPAddress(final String remoteAddr);
}
