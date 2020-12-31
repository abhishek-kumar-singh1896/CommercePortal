/**
 *
 */
package com.gallagher.b2c.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gallagher.b2c.form.GallagherAddressForm;


/**
 *
 *
 */
@Component("gallagherAddressValidator")
public class GallagherAddressValidator implements Validator
{
	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return GallagherAddressForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final GallagherAddressForm addressForm = (GallagherAddressForm) object;
		validateStandardFields(addressForm, errors);
		validateCountrySpecificFields(addressForm, errors);
	}

	protected void validateStandardFields(final GallagherAddressForm addressForm, final Errors errors)
	{
		validateStringField(addressForm.getCountryIso(), AddressField.COUNTRY, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getFirstName(), AddressField.FIRSTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getLastName(), AddressField.LASTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getLine1(), AddressField.LINE1, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getTownCity(), AddressField.TOWN, MAX_FIELD_LENGTH, errors);
		String postcodeField = null;
		if (addressForm.getPostcode() != null)
		{
			postcodeField = addressForm.getPostcode().trim();
			addressForm.setPostcode(postcodeField);
		}
		validateStringFieldForPostCode(postcodeField, AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);
		/*
		 * super.validateStandardFields(addressForm, errors); if ((errors.hasErrors() == true &&
		 * addressForm.getPostcode().length() > 10) || errors.hasErrors() == false) { final String postCodeField =
		 * addressForm.getPostcode().trim(); addressForm.setPostcode(postCodeField);
		 * validateStringFieldForPostCode(postCodeField, AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors); }
		 */
		String phoneNumber = null;
		if (StringUtils.isNotEmpty(addressForm.getPhone()))
		{
			phoneNumber = addressForm.getPhone().trim();
			addressForm.setPhone(phoneNumber);
		}
		validateStringFieldForPhoneNumber(phoneNumber, errors);
	}

	protected void validateCountrySpecificFields(final GallagherAddressForm addressForm, final Errors errors)
	{
		final String isoCode = addressForm.getCountryIso();
		if (isoCode != null)
		{
			switch (CountryCode.lookup(isoCode))
			{
				case CHINA:
					validateStringFieldLength(addressForm.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case CANADA:
					validateStringFieldLength(addressForm.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case USA:
					validateStringFieldLength(addressForm.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case JAPAN:
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					validateStringField(addressForm.getLine2(), AddressField.LINE2, MAX_FIELD_LENGTH, errors);
					break;
				default:
					validateStringFieldLength(addressForm.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					break;
			}
		}
	}

	private void validateStringFieldForPhoneNumber(final String phoneNumber, final Errors errors)
	{
		if (StringUtils.isEmpty(phoneNumber))
		{
			errors.rejectValue("phone", "address.phone.empty");
		}
		else if (phoneNumber.length() < 10)
		{
			errors.rejectValue("phone", "address.phone.invalid");
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

	protected static void validateStringField(final String addressField, final AddressField fieldType, final int maxFieldLength,
			final Errors errors)
	{
		if (addressField == null || StringUtils.isEmpty(addressField) || (StringUtils.length(addressField) > maxFieldLength))
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	protected static void validateStringFieldLength(final String field, final AddressField fieldType, final int maxFieldLength,
			final Errors errors)
	{
		if (StringUtils.isNotEmpty(field) && StringUtils.length(field) > maxFieldLength)
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	protected static void validateFieldNotNull(final String addressField, final AddressField fieldType, final Errors errors)
	{
		if (addressField == null)
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	protected enum CountryCode
	{
		USA("US"), CANADA("CA"), JAPAN("JP"), CHINA("CN"), BRITAIN("GB"), GERMANY("DE"), DEFAULT("");

		private final String isoCode;

		private static Map<String, CountryCode> lookupMap = new HashMap<String, CountryCode>();
		static
		{
			for (final CountryCode code : CountryCode.values())
			{
				lookupMap.put(code.getIsoCode(), code);
			}
		}

		private CountryCode(final String isoCodeStr)
		{
			this.isoCode = isoCodeStr;
		}

		public static CountryCode lookup(final String isoCodeStr)
		{
			CountryCode code = lookupMap.get(isoCodeStr);
			if (code == null)
			{
				code = DEFAULT;
			}
			return code;
		}

		public String getIsoCode()
		{
			return isoCode;
		}
	}

	protected enum AddressField
	{
		TITLE("titleCode", "address.title.invalid"), FIRSTNAME("firstName", "address.firstName.invalid"), LASTNAME("lastName",
				"address.lastName.invalid"), LINE1("line1", "address.line1.invalid"), LINE2("line2", "address.line2.invalid"), TOWN(
						"townCity", "address.townCity.invalid"), POSTCODE("postcode", "address.postcode.invalid"), REGION("regionIso",
								"address.regionIso.invalid"), COUNTRY("countryIso", "address.country.invalid");

		private final String fieldKey;
		private final String errorKey;

		private AddressField(final String fieldKey, final String errorKey)
		{
			this.fieldKey = fieldKey;
			this.errorKey = errorKey;
		}

		public String getFieldKey()
		{
			return fieldKey;
		}

		public String getErrorKey()
		{
			return errorKey;
		}
	}
}

