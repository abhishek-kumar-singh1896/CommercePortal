package com.braintree.cscockpit.widgets.controllers.impl;

import com.braintree.cscockpit.widgets.services.management.BraintreeOrderManagementActionsWidgetService;
import com.braintree.cscockpit.widgets.controllers.BraintreeOrderManagementActionsWidgetController;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;


public class DefaultBraintreeOrderManagementActionsWidgetController extends DefaultOrderManagementActionsWidgetController
        implements BraintreeOrderManagementActionsWidgetController {

    private BraintreeOrderManagementActionsWidgetService braintreeOrderManagementActionsWidgetService;

    @Override
    public boolean isMultipleCapturePossible() {
        return braintreeOrderManagementActionsWidgetService.isMultipleCapturePossible(getOrderIfExist());
    }

    @Override
    public boolean isPartialRefundPossible() {
        return braintreeOrderManagementActionsWidgetService.isPartialRefundPossible(getOrderIfExist());
    }

    @Override
    public boolean isAvailableOrderAuthorization() {
        return braintreeOrderManagementActionsWidgetService.isAvailableOrderAuthorization(getOrderIfExist());
    }

    @Override
    public boolean isVoidAuthorizationPossible() {
        return braintreeOrderManagementActionsWidgetService.isVoidAuthorizationPossible(getOrderIfExist());
    }

    @Override
    public String getBraintreeIntent() {
        return braintreeOrderManagementActionsWidgetService.getBraintreeIntent(getOrderIfExist());
    }

    @Override
    public String getPaymentProvider() {
        return braintreeOrderManagementActionsWidgetService.getPaymentProvider(getOrderIfExist());
    }

    private OrderModel getOrderIfExist(){
        TypedObject orderObject = this.getOrder();
        if (orderObject != null && orderObject.getObject() instanceof OrderModel) {
            return (OrderModel) orderObject.getObject();
        }
        return null;
    }

    public BraintreeOrderManagementActionsWidgetService getBraintreeOrderManagementActionsWidgetService() {
        return braintreeOrderManagementActionsWidgetService;
    }

    public void setBraintreeOrderManagementActionsWidgetService(BraintreeOrderManagementActionsWidgetService braintreeOrderManagementActionsWidgetService) {
        this.braintreeOrderManagementActionsWidgetService = braintreeOrderManagementActionsWidgetService;
    }
}
