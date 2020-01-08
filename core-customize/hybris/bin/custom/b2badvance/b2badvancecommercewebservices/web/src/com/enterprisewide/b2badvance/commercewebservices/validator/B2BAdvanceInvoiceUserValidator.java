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

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.enterprisewide.b2badvance.commercewebservices.invoice.dto.InvoiceWsDTO;


/**
 * Validates invoice user and order. Invoice is invalid if it doesn't have either user or order
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceInvoiceUserValidator implements Validator
{

	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required.order.user";

	@Override
	public boolean supports(final Class<?> paramClass)
	{
		return InvoiceWsDTO.class == paramClass;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (StringUtils.isEmpty(((InvoiceWsDTO) object).getHybrisOrderNumber())
				&& StringUtils.isEmpty(((InvoiceWsDTO) object).getUserId()))
		{
			errors.reject(FIELD_REQUIRED_MESSAGE_ID);
		}
	}
}
