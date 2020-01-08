package com.braintree.cscockpit.widgets.services.management.impl;

import com.braintree.constants.BraintreeConstants;
import com.braintree.cscockpit.widgets.services.management.BraintreeOrderManagementActionsWidgetService;
import com.braintree.model.BrainTreePaymentInfoModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.math.BigDecimal;

public class DefaultBraintreeOrderManagementActionsWidgetService implements BraintreeOrderManagementActionsWidgetService {

    @Override
    public boolean isMultipleCapturePossible(final OrderModel order) {
        if (order != null) {
            return isSuccessfulTransactionPresent(order, PaymentTransactionType.AUTHORIZATION);
        }
        return false;
    }

    @Override
    public boolean isPartialRefundPossible(final OrderModel order) {
        if (order == null){
            return false;
        }
        BigDecimal capturedAmount = BigDecimal.ZERO;
        for (PaymentTransactionModel paymentTransaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel transactionEntry : paymentTransaction.getEntries()) {
                if ("SUCCESFULL".equals(transactionEntry.getTransactionStatusDetails()) &&
                        TransactionStatus.ACCEPTED.name().equals(transactionEntry.getTransactionStatus())) {
                    PaymentTransactionType transactionType =  transactionEntry.getType();
                    if (PaymentTransactionType.CAPTURE.equals(transactionType) || PaymentTransactionType.PARTIAL_CAPTURE.equals(transactionType)){
                        capturedAmount = capturedAmount.add(transactionEntry.getAmount());
                    } else if (PaymentTransactionType.REFUND_PARTIAL.equals(transactionType)){
                        capturedAmount = capturedAmount.subtract(transactionEntry.getAmount());
                    }
                }
            }
        }
        return capturedAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public boolean isAvailableOrderAuthorization(final OrderModel order) {
        return isIntentOrder(order);
    }

    @Override
    public boolean isVoidAuthorizationPossible(final OrderModel order) {
        if (order != null) {
            return isSuccessfulTransactionPresent(order, PaymentTransactionType.AUTHORIZATION);
        }
        return false;
    }

    private boolean isSuccessfulTransactionPresent(final OrderModel order, PaymentTransactionType transactionType){
        for (PaymentTransactionModel paymentTransaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel transactionEntry : paymentTransaction.getEntries()) {
                if (transactionType.equals(transactionEntry.getType()) &&
                        "SUCCESFULL".equals(transactionEntry.getTransactionStatusDetails()) &&
                        TransactionStatus.ACCEPTED.name().equals(transactionEntry.getTransactionStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getBraintreeIntent(final OrderModel order) {
        if (order != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel){
            return ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPayPalIntent();
        }
        return null;
    }

    @Override
    public String getPaymentProvider(final OrderModel order) {
        if (order != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel){
            return ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPaymentProvider();
        }
        return null;
    }

    private boolean isIntentOrder(final OrderModel order) {
        if (order != null && order.getPaymentInfo() != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel) {
            String intent = ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPayPalIntent();
            if (BraintreeConstants.PAYPAL_INTENT_ORDER.equalsIgnoreCase(intent)) {
                return true;
            }
        }
        return false;
    }
}
