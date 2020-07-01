/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.payment.impl;

import de.hybris.platform.acceleratorfacades.payment.PaymentFacade;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorfacades.payment.impl.DefaultPaymentFacade;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.gallagher.facades.payment.GallagherPaymentFacade;


/**
 * Custom implementation of {@link PaymentFacade}
 *
 * @author Vikram Bishnoi
 */
public class GallagherPaymentFacadeImpl extends DefaultPaymentFacade implements GallagherPaymentFacade
{

	@Override
	public void parseMissingFields(final Map<String, String> parameters, final Map<String, PaymentErrorField> errors)
	{
		for (final Map.Entry<String, String> paramEntry : parameters.entrySet())
		{
			if (paramEntry.getKey().startsWith("MissingField"))
			{
				getOrCreatePaymentErrorField(errors, paramEntry.getValue()).setMissing(true);
			}
			if (paramEntry.getKey().startsWith("InvalidField"))
			{
				getOrCreatePaymentErrorField(errors, paramEntry.getValue()).setInvalid(true);
			}
		}
	}

	private PaymentErrorField getOrCreatePaymentErrorField(final Map<String, PaymentErrorField> errors, final String fieldName)
	{
		if (errors.containsKey(fieldName))
		{
			return errors.get(fieldName);
		}
		final PaymentErrorField result = new PaymentErrorField();
		result.setName(fieldName);
		errors.put(fieldName, result);
		return result;
	}

	@Override
	public PaymentSubscriptionResultData completeSopCreateSubscription(final Map<String, String> parameters,
			final boolean saveInAccount, final boolean defaultPayment)
	{
		final PaymentSubscriptionResultItem paymentSubscriptionResultItem;
		final Map<String, PaymentErrorField> errors = new HashMap<String, PaymentErrorField>();
		parseMissingFields(parameters, errors);
		final CustomerModel customerModel = getCurrentUserForCheckout();
		if (MapUtils.isNotEmpty(errors))
		{
			paymentSubscriptionResultItem = new PaymentSubscriptionResultItem();
			paymentSubscriptionResultItem.setErrors(errors);
		}
		else
		{
			paymentSubscriptionResultItem = getPaymentService().completeSopCreatePaymentSubscription(customerModel, saveInAccount,
					parameters);
		}
		if (paymentSubscriptionResultItem != null)
		{
			if (defaultPayment)
			{
				getCustomerAccountService().setDefaultPaymentInfo(customerModel, paymentSubscriptionResultItem.getStoredCard());
			}
			return getPaymentSubscriptionResultDataConverter().convert(paymentSubscriptionResultItem);
		}

		return null;
	}
}
