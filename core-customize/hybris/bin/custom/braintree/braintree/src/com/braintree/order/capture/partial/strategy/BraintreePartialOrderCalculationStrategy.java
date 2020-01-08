package com.braintree.order.capture.partial.strategy;

import de.hybris.platform.core.model.order.OrderModel;

import java.math.BigDecimal;

public interface BraintreePartialOrderCalculationStrategy {

    /**
     * return possible capture amount;
     */
    BigDecimal calculateCaptureAmount(OrderModel orderModel);
}
