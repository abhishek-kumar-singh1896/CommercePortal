package com.braintree.cscockpit.widgets.renderers.utils;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;


public class DefaultLocaleUtils
{
	private static final Locale DEFAULT_LOCALE = Locale.US;

	private DefaultLocaleUtils()
	{
	}

	public static String getRegionName(final RegionModel regionModel)
	{
		if (regionModel != null)
		{
			return getLocalizationName(regionModel, regionModel.getName());
		}
		return StringUtils.EMPTY;
	}

	public static String getRegionName(final RegionModel regionModel, final Locale currentLocale)
	{
		if (regionModel != null)
		{
			return getLocalizationName(regionModel, regionModel.getName(currentLocale));
		}
		return StringUtils.EMPTY;
	}

	private static String getLocalizationName(final RegionModel regionModel, final String name)
	{
		if (StringUtils.isBlank(name))
		{
			return regionModel.getName(DEFAULT_LOCALE);
		}
		return name;
	}


	public static String getCountryName(final CountryModel countryModel)
	{
		if (countryModel != null)
		{
			final String name = countryModel.getName();
			if (StringUtils.isBlank(name))
			{
				return countryModel.getName(DEFAULT_LOCALE);
			}
			return name;
		}
		return StringUtils.EMPTY;
	}


}
