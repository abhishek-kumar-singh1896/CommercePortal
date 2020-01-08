package com.braintree.cscockpit.widgets.controllers.impl;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.BraintreeVoidAuthorizationController;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import org.apache.log4j.Logger;

import java.util.Map;

public class DefaultBraintreeVoidAuthorizationController extends AbstractCsWidgetController implements
        BraintreeVoidAuthorizationController {

    private static final Logger LOG = Logger.getLogger(DefaultBraintreeVoidAuthorizationController.class);

    private OrderManagementActionsWidgetController orderManagementActionsWidgetController;

    private CsBrainTreeFacade csBrainTreeFacade;

    @Override
    public TypedObject getOrder() {
        return getOrderManagementActionsWidgetController().getOrder();
    }

    @Override
    public BrainTreeResponseResultData voidAuthorization(PaymentTransactionEntryModel authorization) throws BraintreeErrorException {
        return getCsBrainTreeFacade().voidTransaction(authorization);
    }


    @Override
    public void dispatchEvent(final String context, final Object source, final Map<String, Object> data) {
        this.getOrderManagementActionsWidgetController().dispatchEvent((String) null, source, data);
    }

    public OrderManagementActionsWidgetController getOrderManagementActionsWidgetController() {
        return orderManagementActionsWidgetController;
    }

    public void setOrderManagementActionsWidgetController(
            OrderManagementActionsWidgetController orderManagementActionsWidgetController) {
        this.orderManagementActionsWidgetController = orderManagementActionsWidgetController;
    }

    public CsBrainTreeFacade getCsBrainTreeFacade() {
        return csBrainTreeFacade;
    }

    public void setCsBrainTreeFacade(CsBrainTreeFacade csBrainTreeFacade) {
        this.csBrainTreeFacade = csBrainTreeFacade;
    }
}
