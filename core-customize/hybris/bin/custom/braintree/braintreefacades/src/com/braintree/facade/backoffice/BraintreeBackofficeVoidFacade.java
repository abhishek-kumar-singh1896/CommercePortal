package com.braintree.facade.backoffice;

import java.util.List;

import com.braintree.exceptions.BraintreeErrorException;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

public interface BraintreeBackofficeVoidFacade {

    boolean isVoidPossible(final OrderModel order);

    List<PaymentTransactionEntryModel> getVoidableTransactions(final OrderModel order);

    void executeVoid(final PaymentTransactionEntryModel transactionId) throws BraintreeErrorException;
}
