/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.payment;

import de.hybris.platform.acceleratorfacades.payment.PaymentFacade;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;

import java.util.Map;


/**
 * Custom implementation of {@link PaymentFacade}
 *
 * @author Vikram Bishnoi
 */
public interface GallagherPaymentFacade extends PaymentFacade
{
	/**
	 * Parses the errors returned by the payment process
	 *
	 * @param parameters
	 *           to get the errors
	 * @param errors
	 *           to be populated
	 */
	void parseMissingFields(final Map<String, String> parameters, final Map<String, PaymentErrorField> errors);
}
