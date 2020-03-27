/**
 *
 */
package com.gallagher.b2c.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gallagher.b2c.form.RegisterProductForm;




/**
 * @author shilpiverma
 *
 */
@Component("registerProductValidator")
public class RegisterProductValidator implements Validator
{

	public static final String PHONE_REGEX = "^(\\+[0-9]*?|[0-9]*)$";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return RegisterProductForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final RegisterProductForm registerProductForm = (RegisterProductForm) object;
		final String productSKU = registerProductForm.getProductSku();
		final String serialNum = registerProductForm.getSerialNumber();
		final String datePurchased = registerProductForm.getDatePurchased();
		final String addressLine1 = registerProductForm.getAddressLine1();
		final String townCity = registerProductForm.getTownCity();
		final String postCode = registerProductForm.getPostCode();
		final String country = registerProductForm.getCountry();
		final String phoneNumber = registerProductForm.getPhoneNumber();
		final String region = registerProductForm.getRegion();
		if (StringUtils.isEmpty(productSKU) || StringUtils.length(productSKU) > 7)
		{
			errors.rejectValue("productSku", "registerProduct.productsku.invalid");
		}
		if (StringUtils.isEmpty(serialNum) || StringUtils.length(serialNum) > 10)
		{
			errors.rejectValue("serialNumber", "registerProduct.serialNumber.invalid");
		}
		if (StringUtils.isEmpty(datePurchased))
		{
			errors.rejectValue("datePurchased", "registerProduct.datePurchased.invalid");
		}
		if (StringUtils.isEmpty(addressLine1))
		{
			errors.rejectValue("addressLine1", "registerProduct.addressLine1.invalid");
		}
		if (StringUtils.isEmpty(townCity))
		{
			errors.rejectValue("townCity", "registerProduct.townCity.invalid");
		}
		if (StringUtils.isEmpty(postCode))
		{
			errors.rejectValue("postCode", "registerProduct.postCode.invalid");
		}
		if (StringUtils.isEmpty(country))
		{
			errors.rejectValue("country", "registerProduct.country.invalid");
		}
		if (StringUtils.isEmpty(region))
		{
			errors.rejectValue("region", "registerProduct.region.invalid");
		}
		/*
		 * if (!validateRegex(phoneNumber, EMAIL_REGEX)) { errors.rejectValue("emailAddressSignup",
		 * "registerProduct.country.invalid"); }
		 */

		validatePhone(phoneNumber, errors);



	}

	private void validatePhone(final String phoneNum, final Errors errors)
	{
		if (StringUtils.isNotEmpty(phoneNum))
		{
			if (!validateRegex(phoneNum, PHONE_REGEX))
			{
				errors.rejectValue("phoneNumber", "registerProduct.phoneNumber.invalid");
			}
		}


	}

	public boolean validateRegex(final String str, final String regex)
	{
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}