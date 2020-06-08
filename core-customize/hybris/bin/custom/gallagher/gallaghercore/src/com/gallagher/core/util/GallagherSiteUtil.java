/**
 *
 */
package com.gallagher.core.util;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletRequest;


/**
 * @author shishirkant
 */
public class GallagherSiteUtil
{
	private static final String SITE_SWITCHED = "siteSwitched";

	/**
	 * Checks whether user switched between sites based on session attributes
	 */

	public static boolean isSiteSwitched(final ServletRequest request)
	{
		return Boolean.TRUE.equals(request.getAttribute(SITE_SWITCHED));
	}

	/**
	 * Set site to switched for the scope of this request.
	 */
	public static void setSiteSwitched(final ServletRequest request)
	{
		request.setAttribute(SITE_SWITCHED, Boolean.TRUE);
	}

	public static String getFormattedDateWithTimeZoneForDate(final Date date, final String format, final TimeZone timzezone)
	{
		validateParameterNotNullStandardMessage("date", date);
		validateParameterNotNullStandardMessage("format", format);
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		final ZonedDateTime dateWithZone = ZonedDateTime.ofInstant(date.toInstant(),
				timzezone == null ? ZoneOffset.systemDefault() : timzezone.toZoneId());
		return dateWithZone.format(formatter);
	}
}
