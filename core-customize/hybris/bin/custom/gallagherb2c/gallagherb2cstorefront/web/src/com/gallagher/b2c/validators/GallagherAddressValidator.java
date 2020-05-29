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
		validateStringField(addressForm.getCountryIso(), AddressField.COUNTRY, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getFirstName(), AddressField.FIRSTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getLastName(), AddressField.LASTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getLine1(), AddressField.LINE1, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getTownCity(), AddressField.TOWN, MAX_FIELD_LENGTH, errors);
		final String postcodeField = addressForm.getPostcode().trim();
		addressForm.setPostcode(postcodeField);
		validateStringFieldForPostCode(postcodeField, AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);
		/*
		 * super.validateStandardFields(addressForm, errors); if ((errors.hasErrors() == true &&
		 * addressForm.getPostcode().length() > 10) || errors.hasErrors() == false) { final String postCodeField =
		 * addressForm.getPostcode().trim(); addressForm.setPostcode(postCodeField);
		 * validateStringFieldForPostCode(postCodeField, AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors); }
		 */
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

