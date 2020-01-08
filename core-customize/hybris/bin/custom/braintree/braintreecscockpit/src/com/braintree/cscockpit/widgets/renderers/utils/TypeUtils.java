package com.braintree.cscockpit.widgets.renderers.utils;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;

import org.apache.commons.lang.StringUtils;


public class TypeUtils
{

	private static final String COMMA_VALUE_SEPARATOR = ",";
	private static final String EMPTY_VALUE_SEPARATOR = " ";
	private static final int BEGIN_INDEX_WITHOUT_COMMA = 1;

	private TypeUtils()
	{
	}

	public static String getStringValueFormAddress(AddressModel billingAddress)
	{
		StringBuilder buffer = new StringBuilder();
		if (billingAddress != null)
		{
			appendName(billingAddress, buffer);

			append(buffer, billingAddress.getLine1());
			append(buffer, billingAddress.getLine2());
			append(buffer, billingAddress.getTown());
			append(buffer, billingAddress.getPostalcode());

			RegionModel region = billingAddress.getRegion();
			if (region != null)
			{
				append(buffer, region.getName());
			}

			CountryModel country = billingAddress.getCountry();
			if (country != null)
			{
				append(buffer, country.getName());
			}
		}
		return removeDraftSymbols(buffer.toString());
	}

	private static void appendName(AddressModel billingAddress, StringBuilder buffer)
	{
		String firstName = billingAddress.getFirstname();
		String lastName = billingAddress.getLastname();
		if (StringUtils.isNotBlank(firstName))
		{
			buffer.append(firstName);
			if (StringUtils.isNotBlank(lastName))
			{
				buffer.append(EMPTY_VALUE_SEPARATOR).append(lastName);
			}
		}
		else
		{
			if (StringUtils.isNotBlank(lastName))
			{
				buffer.append(lastName);
			}
		}
	}

	private static void append(StringBuilder buffer, String value)
	{
		if (StringUtils.isNotBlank(value))
		{
			buffer.append(COMMA_VALUE_SEPARATOR).append(value);
		}
	}

	private static String removeDraftSymbols(String line)
	{
		if (line.startsWith(COMMA_VALUE_SEPARATOR))
		{
			return line.substring(BEGIN_INDEX_WITHOUT_COMMA);
		}
		return line;
	}
}
