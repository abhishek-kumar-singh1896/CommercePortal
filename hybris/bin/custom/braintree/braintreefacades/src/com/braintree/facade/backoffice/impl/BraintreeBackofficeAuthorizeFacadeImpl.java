package com.braintree.facade.backoffice.impl;

import com.braintree.constants.BraintreeConstants;
import com.braintree.facade.backoffice.BraintreeBackofficeAuthorizeFacade;
import com.braintree.model.BrainTreePaymentInfoModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class BraintreeBackofficeAuthorizeFacadeImpl implements BraintreeBackofficeAuthorizeFacade {

    private final static Logger LOG = Logger.getLogger(BraintreeBackofficeAuthorizeFacadeImpl.class);

    @Autowired
    private ModelService modelService;

    @Override
    public boolean isAuthorizePossible(final OrderModel order) {
        if (null == order) {
            LOG.error("order: " + order);
            return false;
        }
        LOG.info("isAuthorizePossible, order.getTotalPrice: " + order.getTotalPrice());

        modelService.refresh(order);

        String paymentProvider = ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPaymentProvider();
        return isIntentOrder(order) && !isHostedFields(paymentProvider) && !isApplePay(paymentProvider);
    }

    private boolean isIntentOrder(final OrderModel order) {
        if (order.getPaymentInfo() instanceof BrainTreePaymentInfoModel) {
            String intent = ((BrainTreePaymentInfoModel) order.getPaymentInfo()).getPayPalIntent();
            return BraintreeConstants.PAYPAL_INTENT_ORDER.equalsIgnoreCase(intent);
        } else {
            return false;
        }
    }

    private boolean isApplePay(final String paymentProvider) {
        return BraintreeConstants.APPLE_PAY_PAYMENT.equals(paymentProvider);
    }

    private boolean isHostedFields(final String paymentProvider) {
        return BraintreeConstants.BRAINTREE_CREDITCARD_PAYMENT.equals(paymentProvider);
    }


}
