/**
 *
 */
package com.gallagher.facades.impl;

import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.core.enums.RegionCode;
import com.gallagher.facades.GallagherRegionDetectionFacade;


/**
 * Implementation of GallagherRegionDetectionFacade
 *
 * @author shishirkant
 */
public class GallagherRegionDetectionFacadeImpl implements GallagherRegionDetectionFacade
{
	@Resource(name = "countryDao")
	private CountryDao countryDao;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherRegionDetectionFacadeImpl.class);
	private static final RegionCode DEFAULT_REGION_CODE = RegionCode.valueOf("global");

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RegionCode getRegionCode(final HttpServletRequest request, final boolean isSecurity)
	{

		final CountryData country = getCountry(request);
		RegionCode regionCode = DEFAULT_REGION_CODE;
		if (null != country)
		{
			final String countryIsoCode = country.getIsocode();

			if (!StringUtils.isEmpty(countryIsoCode))
			{
				final Optional<CountryModel> countryModel = countryDao.findCountriesByCode(countryIsoCode).stream().findFirst();
				if (countryModel.isPresent())
				{
					LOGGER.debug("Detected Region Code <" + countryModel.get().getRegionCode() + "> for Country ISO Code <"
							+ countryIsoCode + ">");
					regionCode = isSecurity ? countryModel.get().getSecurityRegionCode() : countryModel.get().getRegionCode();
				}
			}

			LOGGER.debug(
					"No Region detected for Country ISO Code <" + countryIsoCode + ">, defaulting to <" + DEFAULT_REGION_CODE + ">");
		}
		return regionCode;
	}

	@Override
	public CountryData getCountry(final HttpServletRequest request)
	{
		final String requestURL = request.getRequestURI();
		if (!StringUtils.isEmpty(requestURL))
		{
			final String countryCode = requestURL.split("/")[2];
			if (!StringUtils.isEmpty(countryCode))
			{
				try
				{
					return i18NFacade.getCountryForIsocode(countryCode.toUpperCase());
				}
				catch (final UnknownIdentifierException e)
				{
					LOGGER.warn("Unknown Country Code <" + countryCode + ">");
				}
			}
		}
		return null;
	}

}
