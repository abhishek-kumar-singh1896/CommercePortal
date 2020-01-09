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
package com.gallagher.b2b.storefront.forms.validation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gallagher.b2b.storefront.forms.CreateTemplateForm;


@Component("createTemplateFormValidator")
public class CreateTemplateFormValidator implements Validator
{
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return CreateTemplateForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final CreateTemplateForm templateForm = (CreateTemplateForm) object;
		final String name = templateForm.getTemplateName();
		final String description = templateForm.getTemplateDesc();

		if (StringUtils.isBlank(name))
		{
			errors.rejectValue("templateName", "create.template.validation.name.notBlank");
			return;
		}
		if (!StringUtils.isAlphanumericSpace(name))
		{
			errors.rejectValue("templateName", "create.template.validation.name.charset");
			return;
		}
		if (StringUtils.length(name) > 255)
		{
			errors.rejectValue("templateName", "create.template.validation.name.size");
			return;
		}
		if (StringUtils.length(description) > 255)
		{
			errors.rejectValue("templateDescription", "create.template.validation.description.size");
			return;
		}
		if (CollectionUtils.isEmpty(templateForm.getCartEntries()))
		{
			errors.rejectValue("createTemplateForm", "create.template.validation.entries.size");
			return;
		}
	}
}
