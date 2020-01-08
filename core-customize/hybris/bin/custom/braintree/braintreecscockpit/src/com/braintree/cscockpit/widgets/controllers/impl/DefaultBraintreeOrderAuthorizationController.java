package com.braintree.cscockpit.widgets.controllers.impl;

import com.braintree.cscockpit.widgets.controllers.BraintreeOrderAuthorizationController;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.order.capture.partial.services.BraintreePartialCaptureService;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultBraintreeOrderAuthorizationController extends AbstractCsWidgetController implements BraintreeOrderAuthorizationController {

    private static final Double ZERO_VAL = Double.valueOf(0);
    private static final Logger LOG = Logger.getLogger(DefaultBraintreeMultipleCaptureController.class);

    private OrderManagementActionsWidgetController orderManagementActionsWidgetController;
    private BraintreeDefaultCallContextController callContextController;

    @Override
    public TypedObject getOrder() {
        return this.getOrderManagementActionsWidgetController().getOrder();
    }

    @Override
    public void dispatchEvent(String context, Object source, Map<String, Object> data) {
        callContextController.setImpersonationContextChanged();
        this.getOrderManagementActionsWidgetController().dispatchEvent((String) null, source, data);
    }

    @Override
    public Double getTotalAmount() {
        Object order = getOrder().getObject();
        if (order != null && order instanceof OrderModel)
        {
            return ((OrderModel) order).getTotalPrice();
        }
        return ZERO_VAL;
    }

    @Override
    public Map<String, String> getCustomFields() {
        Object order = getOrder().getObject();
        if (order != null && ((OrderModel) order).getPaymentInfo() instanceof BrainTreePaymentInfoModel)
        {
            BrainTreePaymentInfoModel paymentInfoModel = (BrainTreePaymentInfoModel) ((OrderModel) order).getPaymentInfo();
            Map<String, String> customFields = paymentInfoModel.getCustomFields();
            if (customFields != null)
            {
                return customFields;
            }
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    public String getPaymentMethodToken() {
        Object orderObject = getOrder().getObject();
        if (orderObject != null && orderObject instanceof OrderModel)
        {
            OrderModel order = (OrderModel) orderObject;
            if (order.getPaymentInfo() != null && order.getPaymentInfo() instanceof BrainTreePaymentInfoModel)
            {
                return ((BrainTreePaymentInfoModel)order.getPaymentInfo()).getPaymentMethodToken();
            }
        }
        return null;
    }

    public OrderManagementActionsWidgetController getOrderManagementActionsWidgetController() {
        return orderManagementActionsWidgetController;
    }

    public void setOrderManagementActionsWidgetController(OrderManagementActionsWidgetController orderManagementActionsWidgetController) {
        this.orderManagementActionsWidgetController = orderManagementActionsWidgetController;
    }

    public void setCallContextController(BraintreeDefaultCallContextController callContextController) {
        this.callContextController = callContextController;
    }
}
