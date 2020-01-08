package com.braintree.facade.backoffice.impl;

import java.util.ArrayList;
import java.util.List;

import com.braintree.constants.BraintreeConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.facade.backoffice.BraintreeBackofficeMultiCaptureFacade;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;


public class BraintreeBackofficeMultiCaptureFacadeImpl implements BraintreeBackofficeMultiCaptureFacade {
    private final static Logger LOG = Logger.getLogger(BraintreeBackofficeMultiCaptureFacadeImpl.class);

    @Autowired
    private BrainTreePaymentService brainTreePaymentService;
    @Autowired
    private ModelService modelService;


    @Override
    public boolean isMultiCapturePossible(final OrderModel order) {
        if (null == order) {
            LOG.error("order: " + order);
            return false;
        }
        LOG.info("isMultiCapturePossible, order.getTotalPrice: " + order.getTotalPrice());

        modelService.refresh(order);

        if (order != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel) {
            final String intent = getOrderIntent(order);
            return isSuccessfulTransactionPresent(order, PaymentTransactionType.AUTHORIZATION) &&
                    !BraintreeConstants.PAYPAL_INTENT_SALE.equalsIgnoreCase(intent);
        }
        return false;
    }

    private String getOrderIntent(final OrderModel order) {
        return ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPayPalIntent();
    }

    @Override
    public List<PaymentTransactionEntryModel> getMultiCaptureableTransactions(final OrderModel order) {
        List<PaymentTransactionEntryModel> result = new ArrayList<>();

        for (PaymentTransactionModel transaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel paymentEntry : transaction.getEntries()) {
                if (PaymentTransactionType.AUTHORIZATION.equals(paymentEntry.getType())
                        && TransactionStatus.ACCEPTED.name().equals(paymentEntry.getTransactionStatus())) {
                    result.add(paymentEntry);
                }
            }
        }

        return result;
    }

    @Override
    public void executeMultiCapture(final PaymentTransactionEntryModel transaction) throws BraintreeErrorException {
        LOG.error("brainTreePaymentService: " + brainTreePaymentService);
    }

    private boolean isSuccessfulTransactionPresent(final OrderModel order, final PaymentTransactionType transactionType) {
        for (PaymentTransactionModel paymentTransaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel transactionEntry : paymentTransaction.getEntries()) {
                if (transactionType.equals(transactionEntry.getType()) && "SUCCESFULL".equals(transactionEntry.getTransactionStatusDetails())
                        && TransactionStatus.ACCEPTED.name().equals(transactionEntry.getTransactionStatus())) {
                    return true;
                }
            }
        }
        return false;
    }
}
