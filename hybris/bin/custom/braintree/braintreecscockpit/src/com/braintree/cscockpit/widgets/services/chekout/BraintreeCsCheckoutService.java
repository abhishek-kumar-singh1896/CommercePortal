/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.services.chekout;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.services.checkout.CsCheckoutService;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import java.math.BigDecimal;

import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;


public interface BraintreeCsCheckoutService extends CsCheckoutService
{

	void createPaymentInfo(CartModel cart, CardInfo cardInfo, double amount, BraintreeInfo braintreeInfo)
			throws PaymentException, ValidationException;

	PaymentTransactionEntryModel authorisePayment(CartModel paramCartModel, BrainTreePaymentInfoModel paymentInfo,
			BigDecimal amount) throws PaymentException, ValidationException;
}
