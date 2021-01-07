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
package com.enterprisewide.b2badvanceacceleratoraddon.forms.validation;

import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.enterprisewide.b2badvanceacceleratoraddon.forms.PaymentTypeForm;


/**
 * Validator for {@link PaymentTypeForm}.
 */
@Component("paymentTypeFormValidator")
public class PaymentTypeFormValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return PaymentTypeForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (object instanceof PaymentTypeForm)
		{
			final PaymentTypeForm paymentTypeForm = (PaymentTypeForm) object;

			if (CheckoutPaymentType.ACCOUNT.getCode().equals(paymentTypeForm.getPaymentType())
					&& paymentTypeForm.getPurchaseOrderNumber().length() == 0)
			{

				errors.rejectValue("purchaseOrderNumber", "general.required");
				return;
			}

			final String fetchDate = paymentTypeForm.getRequiredDeliveryDate();

			int flag = 0;
			if (!StringUtils.isEmpty(fetchDate))
			{
				final Date date = new Date();
				final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				final String convertPresentDate = format.format(date);
				Date dateConvert = null;
				if (!convertPresentDate.equals(fetchDate))
				{
				try
				{
					dateConvert = new SimpleDateFormat("dd/MM/yyyy").parse(fetchDate);
				}
				catch (final ParseException e)
				{
					e.printStackTrace();
					errors.rejectValue("requiredDeliveryDate", "requiredDate.invalid");
				}
				if (null != dateConvert)
				{
					final int returnValue = date.compareTo(dateConvert);
					if (!fetchDate.contains("/"))
					{
						flag = 1;
						paymentTypeForm.setIndicator(true);
					}
					else if (returnValue == 1)
					{
						flag = 1;
						paymentTypeForm.setIndicator(true);
					}
					if (flag == 1)
					{
						errors.rejectValue("requiredDeliveryDate", "requiredDate.invalid");
					}
				}
					else
					{
						paymentTypeForm.setIndicator(true);
					}
			}
			}
		}
	}

}
