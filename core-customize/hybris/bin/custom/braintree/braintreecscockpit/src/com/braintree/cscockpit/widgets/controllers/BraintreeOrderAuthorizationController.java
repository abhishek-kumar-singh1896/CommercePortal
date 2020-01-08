package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;

import java.math.BigDecimal;
import java.util.Map;


public interface BraintreeOrderAuthorizationController extends WidgetController{
    TypedObject getOrder();

    Double getTotalAmount();

    Map<String, String> getCustomFields();

    String getPaymentMethodToken();
}
