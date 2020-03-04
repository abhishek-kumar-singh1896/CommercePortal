/**
 *
 */
package com.gallagher.core.util;

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
}
