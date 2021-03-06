/**
 *
 */
package com.braintree.transaction.service;

import com.braintree.model.BrainTreeTransactionDetailModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.math.BigDecimal;
import java.util.List;


public interface BrainTreePaymentTransactionService
{
	PaymentTransactionModel getTransactionByRequestIdAndPaymentProvider(String requestId, String paymentProvider);

	List<PaymentTransactionModel> getTransactionsByRequestIdAndPaymentProvider(String requestId, String paymentProvider);

	void continueSubmitOrder(final BrainTreeTransactionDetailModel currentTransaction, final BigDecimal amount);

	void resumeOrderProcess(OrderModel order);
}
