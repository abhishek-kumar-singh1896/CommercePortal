package com.braintree.cscockpit.widgets.services.management;

import de.hybris.platform.core.model.order.OrderModel;

public interface BraintreeOrderManagementActionsWidgetService {

    boolean isMultipleCapturePossible(OrderModel order);
    boolean isPartialRefundPossible(OrderModel order);
    boolean isAvailableOrderAuthorization(OrderModel order);
    boolean isVoidAuthorizationPossible(OrderModel order);
    String getBraintreeIntent(OrderModel order);
    String getPaymentProvider(OrderModel order);
}
