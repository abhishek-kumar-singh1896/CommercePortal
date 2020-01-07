package com.braintree.cscockpit.widgets.controllers;


import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import java.math.BigDecimal;

public interface BraintreePartialRefundController extends WidgetController {

    TypedObject getOrder();

    BrainTreeResponseResultData partialRefund(PaymentTransactionEntryModel paymentTransactionEntry, BigDecimal amount) throws BraintreeErrorException;

}
