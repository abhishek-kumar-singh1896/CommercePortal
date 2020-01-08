package com.braintree.cscockpit.widgets.controllers;


import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;


public interface BraintreeOrderManagementActionsWidgetController extends OrderManagementActionsWidgetController
{
    boolean isMultipleCapturePossible();

    boolean isPartialRefundPossible();

    boolean isAvailableOrderAuthorization();

    boolean isVoidAuthorizationPossible();

    String getBraintreeIntent();

    String getPaymentProvider();
}
