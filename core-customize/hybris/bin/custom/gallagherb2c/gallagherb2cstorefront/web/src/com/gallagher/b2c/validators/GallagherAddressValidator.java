/**
 *
 */
package com.gallagher.b2c.validators;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


/**
 *
 *
 */
@Component("gallagherAddressValidator")
public class GallagherAddressValidator extends AddressValidator
{
	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;



	@Override
	protected void validateStandardFields(final AddressForm addressForm, final Errors errors)
	{
		super.validateStandardFields(addressForm, errors);
		if (errors.hasErrors() == false)
		{
			validateStringFieldForPostCode(addressForm.getPostcode(), AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);
		}
	}

	protected static void validateStringFieldForPostCode(final String addressField, final AddressField fieldType,
			final int maxFieldLength, final Errors errors)
	{
		if (addressField == null || StringUtils.isEmpty(addressField) || (StringUtils.length(addressField) > maxFieldLength)
				|| !Pattern.matches(("^[a-zA-Z0-9- ]+$"), addressField))
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}
}

