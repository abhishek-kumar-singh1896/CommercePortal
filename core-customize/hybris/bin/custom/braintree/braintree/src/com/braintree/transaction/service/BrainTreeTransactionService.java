/**
 *
 */
package com.braintree.transaction.service;

import com.braintree.command.result.BrainTreeRefundTransactionResult;
import com.braintree.command.result.BrainTreeSaleTransactionResult;
import com.braintree.command.result.BrainTreeVoidResult;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.payment.dto.BraintreeInfo;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.math.BigDecimal;
import java.util.Map;


public interface BrainTreeTransactionService
{
	boolean createAuthorizationTransaction();

	boolean createAuthorizationTransaction(Map<String, String> customFields);

	boolean createPaymentMethodTokenForOrderReplenishment();

	PaymentTransactionEntryModel createCancelTransaction(PaymentTransactionModel transaction, BrainTreeVoidResult voidResult);

	PaymentTransactionEntryModel createCancelTransaction(PaymentTransactionModel transaction,
			BraintreeTransactionEntryData brainTreeTransaction);

	PaymentTransactionEntryModel createRefundTransaction(PaymentTransactionModel transaction,
			BrainTreeRefundTransactionResult result);

    PaymentTransactionEntryModel createPartialRefundTransaction(PaymentTransactionModel transaction,
                                                                BrainTreeRefundTransactionResult result);

    PaymentTransactionEntryModel createPartialCaptureTransaction(PaymentTransactionModel transaction,
			BrainTreeSaleTransactionResult result);

	PaymentTransactionEntryModel createAuthorizationTransaction(CartModel cart);

	PaymentTransactionEntryModel createAuthorizationTransaction(final AbstractOrderModel cart, Map<String, String> customFields);

	PaymentTransactionEntryModel createAuthorizationTransaction(final AbstractOrderModel cart, Map<String, String> customFields, BigDecimal totalAmount);

	BrainTreePaymentInfoModel createSubscription(final AddressModel billingAddress, final CustomerModel customer,
			final BraintreeInfo braintreeInfo);

}
