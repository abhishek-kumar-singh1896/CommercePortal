/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.web.payment.validation;


import de.hybris.platform.acceleratorservices.util.CalendarHelper;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.SopPaymentDetailsForm;

import java.util.Calendar;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



@Component("gallagherSOPPaymentDetailsValidator")
@Primary
public class GallagherSopPaymentDetailsValidator implements Validator
{
	private static final int MAX_POSTCODE_LENGTH = 10;
	private static final String PAYMENT_START_DATE_INVALID = "payment.startDate.invalid";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return PaymentDetailsForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final SopPaymentDetailsForm form = (SopPaymentDetailsForm) object;

		final Calendar startOfCurrentMonth = CalendarHelper.getCalendarResetTime();
		startOfCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);

		final Calendar startOfNextMonth = CalendarHelper.getCalendarResetTime();
		startOfNextMonth.set(Calendar.DAY_OF_MONTH, 1);
		startOfNextMonth.add(Calendar.MONTH, 1);

		final Calendar start = CalendarHelper.parseDate(form.getCard_startMonth(), form.getCard_startYear());
		final Calendar expiration = CalendarHelper.parseDate(form.getCard_expirationMonth(), form.getCard_expirationYear());

		if (start != null && !start.before(startOfNextMonth))
		{
			errors.rejectValue("card_startMonth", PAYMENT_START_DATE_INVALID);
		}
		if (expiration != null && expiration.before(startOfCurrentMonth))
		{
			errors.rejectValue("card_expirationMonth", PAYMENT_START_DATE_INVALID);
		}
		if (start != null && expiration != null && start.after(expiration))
		{
			errors.rejectValue("card_startMonth", PAYMENT_START_DATE_INVALID);
		}

		if (StringUtils.isBlank(form.getBillTo_country()))
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billTo_country", "address.country.invalid");
		}
		else
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billTo_firstName", "address.firstName.invalid");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billTo_lastName", "address.lastName.invalid");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billTo_street1", "address.line1.invalid");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billTo_city", "address.city.invalid");
			validateStringFieldForPostCode(form.getBillTo_postalCode(), "billTo_postalCode", MAX_POSTCODE_LENGTH, errors);

			String phoneNumber = null;
			if (StringUtils.isNotEmpty(form.getBillTo_phoneNumber()))
			{
				phoneNumber = form.getBillTo_phoneNumber().trim();
				form.setBillTo_phoneNumber(phoneNumber);
			}
			validateStringFieldForPhoneNumber(phoneNumber, errors);
		}
	}

	private void validateStringFieldForPhoneNumber(final String phoneNumber, final Errors errors)
	{
		if (StringUtils.isEmpty(phoneNumber)
				|| (StringUtils.isNotEmpty(phoneNumber) && !Pattern.matches(("^[0-9- ]{10}+$"), phoneNumber)))
		{
			errors.rejectValue("billTo_phoneNumber", "address.phone.invalid");
		}
	}

	protected static void validateStringFieldForPostCode(final String addressField, final String field, final int maxFieldLength,
			final Errors errors)
	{
		if (addressField == null || StringUtils.isEmpty(addressField) || (StringUtils.length(addressField) > maxFieldLength)
				|| !Pattern.matches(("^[a-zA-Z0-9- ]+$"), addressField))
		{
			errors.rejectValue(field, "address.postcode.invalid");
		}

	}
}
