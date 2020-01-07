/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.commercewebservices.validator;

import de.hybris.platform.webservicescommons.validators.EnumValueValidator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;


/**
 * Generic enum validator
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceEnumValueValidator extends EnumValueValidator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private String fieldPath;
	private boolean required;

	public B2BAdvanceEnumValueValidator(final String enumClass)
	{
		super(enumClass);
	}

	@Override
	public boolean supports(final Class<?> paramClass)
	{
		return true;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null");
		final Object fieldValue = errors.getFieldValue(this.fieldPath);

		if (fieldValue == null || fieldValue instanceof String && StringUtils.isBlank((String) fieldValue))
		{
			if (required)
			{
				errors.rejectValue(this.fieldPath, FIELD_REQUIRED_MESSAGE_ID, new String[]
				{ this.fieldPath }, "This field is required.");
			}
		}
		else
		{
			super.validate((String) fieldValue, errors);
		}

	}

	public String getFieldPath()
	{
		return this.fieldPath;
	}

	@Required
	public void setFieldPath(final String fieldPath)
	{
		this.fieldPath = fieldPath;
	}

	public boolean isRequired()
	{
		return required;
	}

	@Required
	public void setRequired(final boolean required)
	{
		this.required = required;
	}
}
