/**
 *
 */
package com.gallagher.b2c.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gallagher.core.form.RegisterProductForm;




/**
 * @author shilpiverma
 *
 */
@Component("registerProductValidator")
public class RegisterProductValidator implements Validator
{

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

		if (StringUtils.isEmpty(productSKU))
		{
			errors.rejectValue("productSKU", "registerProduct.productsku.invalid");
		}

	}
}
