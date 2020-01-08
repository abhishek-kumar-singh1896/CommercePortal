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

import com.enterprisewide.b2badvance.commercewebservices.price.dto.ProductDiscountWsDTO;


/**
 * Validates currency for discount row. Discount is invalid if currency is missing in VALUE type discount
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceDiscountCurrencyValidator implements Validator
{

	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required.currency";
	private static final String FIELD_NOT_REQUIRED_MESSAGE_ID = "field.not.required.currency";

	private static final String VALUE = "VALUE";
	private static final String PERCENTAGE = "PERCENTAGE";

	@Override
	public boolean supports(final Class<?> paramClass)
	{
		return ProductDiscountWsDTO.class == paramClass;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (StringUtils.isEmpty(((ProductDiscountWsDTO) object).getCurrency())
				&& (StringUtils.isEmpty(((ProductDiscountWsDTO) object).getDiscountType())
						|| ((ProductDiscountWsDTO) object).getDiscountType().equals(VALUE)))
		{
			errors.reject(FIELD_REQUIRED_MESSAGE_ID);
		}
		else if (StringUtils.isNotEmpty(((ProductDiscountWsDTO) object).getCurrency())
				&& ((ProductDiscountWsDTO) object).getDiscountType().equals(PERCENTAGE))
		{
			errors.reject(FIELD_NOT_REQUIRED_MESSAGE_ID);
		}
	}
}
