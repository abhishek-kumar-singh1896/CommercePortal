/**
 *
 */
package com.gallagher.facades;

import de.hybris.platform.commercefacades.user.data.CountryData;

import javax.servlet.http.HttpServletRequest;

import com.gallagher.core.enums.RegionCode;


/**
 * @author shishirkant
 */
public interface GallagherRegionDetectionFacade
{
	/**
	 * Get region based on the request
	 *
	 * @param request
	 * @return Detected region. Using global as default
	 */
	RegionCode getRegionCode(final HttpServletRequest request);

	/**
	 * Get region based on the request
	 *
	 * @param request
	 * @return Detected Country. Using global as default
	 */
	CountryData getCountry(final HttpServletRequest request);
}
