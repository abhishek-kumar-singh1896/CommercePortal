package com.braintree.facade.backoffice.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.braintree.command.result.BrainTreeVoidResult;
import com.braintree.constants.BraintreeConstants;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.facade.backoffice.BraintreeBackofficeVoidFacade;
import com.braintree.method.BrainTreePaymentService;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintreegateway.Transaction;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;


public class BraintreeBackofficeVoidFacadeImpl implements BraintreeBackofficeVoidFacade {
    private final static Logger LOG = Logger.getLogger(BraintreeBackofficeVoidFacadeImpl.class);

    @Autowired
    private BrainTreePaymentService brainTreePaymentService;
    @Autowired
    private ModelService modelService;


    @Override
    public boolean isVoidPossible(final OrderModel order) {
        if (null == order) {
            LOG.error("order: " + order);
            return false;
        }
        LOG.info("isVoidPossible, order.getTotalPrice: " + order.getTotalPrice());

        modelService.refresh(order);

        if (order != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel) {
            final String paymentProvider = ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPaymentProvider();
            final String intent = ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPayPalIntent();

            if (BraintreeConstants.BRAINTREE_CREDITCARD_PAYMENT.equals(paymentProvider) || BraintreeConstants.APPLE_PAY_PAYMENT.equals(paymentProvider)) {
                return false;
            } else if (BraintreeConstants.PAYPAL_INTENT_SALE.equalsIgnoreCase(intent)) {
                return false;
            } else if (BraintreeConstants.PAYPAL_INTENT_AUTHORIZE.equalsIgnoreCase(intent)) {
                return isVoidAuthorizationPossible(order);
            } else if (BraintreeConstants.PAYPAL_INTENT_ORDER.equalsIgnoreCase(intent)) {
                return isVoidAuthorizationPossible(order);
            } else {
                LOG.error("Order was placed with incorrect intent = '" + intent);
                return false;
            }
        }

        return false;
    }

    @Override
    public List<PaymentTransactionEntryModel> getVoidableTransactions(final OrderModel order) {
        List<PaymentTransactionEntryModel> result = new ArrayList<>();

        for (PaymentTransactionModel transaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel paymentEntry : transaction.getEntries()) {
                if (TransactionStatus.ACCEPTED.name().equals(paymentEntry.getTransactionStatus()) && PaymentTransactionType.AUTHORIZATION.equals(paymentEntry.getType())) {
                    result.add(paymentEntry);
                }
            }
        }

        return result;
    }

    @Override
    public void executeVoid(final PaymentTransactionEntryModel transaction) throws BraintreeErrorException {
        final VoidRequest voidRequest = new VoidRequest("-not-used-", transaction.getRequestId(), StringUtils.EMPTY, StringUtils.EMPTY);
        final BrainTreeVoidResult voidResult = brainTreePaymentService.voidTransaction(voidRequest);

        if (voidResult.isSuccess()) {
            transaction.setTransactionStatus(Transaction.Status.VOIDED.name());
            modelService.save(transaction);
        } else {
            LOG.error("Error, message: " + voidResult.getErrorMessage());
            throw new BraintreeErrorException(voidResult.getErrorMessage(), voidResult.getTransactionId());
        }
    }

    private boolean isVoidAuthorizationPossible(final OrderModel order) {
        if (order != null) {
            return isSuccessfulTransactionPresent(order, PaymentTransactionType.AUTHORIZATION);
        }
        return false;
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
