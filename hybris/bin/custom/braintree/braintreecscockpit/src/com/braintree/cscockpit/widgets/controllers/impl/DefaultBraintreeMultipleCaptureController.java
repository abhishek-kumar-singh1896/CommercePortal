package com.braintree.cscockpit.widgets.controllers.impl;

import com.braintree.exceptions.BraintreeErrorException;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

import com.braintree.cscockpit.widgets.controllers.BraintreeMultipleCaptureController;
import com.braintree.order.capture.partial.services.BraintreePartialCaptureService;


public class DefaultBraintreeMultipleCaptureController extends AbstractCsWidgetController implements
		BraintreeMultipleCaptureController
{
	private static final Logger LOG = Logger.getLogger(DefaultBraintreeMultipleCaptureController.class);
	private OrderManagementActionsWidgetController orderManagementActionsWidgetController;
	private OrderCancelService orderCancelService;
	private UserService userService;
	private BraintreePartialCaptureService braintreePartialCaptureService;
    private BraintreeDefaultCallContextController callContextController;

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
        callContextController.setImpersonationContextChanged();
		this.getOrderManagementActionsWidgetController().dispatchEvent((String) null, source, data);
	}

	@Override
	public TypedObject getOrder()
	{
		return this.getOrderManagementActionsWidgetController().getOrder();
	}

	public OrderManagementActionsWidgetController getOrderManagementActionsWidgetController()
	{
		return orderManagementActionsWidgetController;
	}

    @Override
    public Boolean partialCapture(final BigDecimal amount, final String authorizeTransactionID) throws BraintreeErrorException {
        Object object = getOrder().getObject();
        if (object instanceof OrderModel)
        {
            if (getBraintreePartialCaptureService().isValidTransactionId((OrderModel) object, authorizeTransactionID)) {
                return getBraintreePartialCaptureService().partialCapture((OrderModel) object, amount, authorizeTransactionID);
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public BigDecimal getAmountAvailableForMultiCapture()
    {
        final OrderModel orderModel = (OrderModel) getOrder().getObject();
        if (orderModel != null)
        {
            return getBraintreePartialCaptureService().getPossibleAmountForCapture(orderModel);
        }
        return BigDecimal.ZERO;
    }

	public void setOrderManagementActionsWidgetController(
			final OrderManagementActionsWidgetController orderManagementActionsWidgetController)
	{
		this.orderManagementActionsWidgetController = orderManagementActionsWidgetController;
	}

	public OrderCancelService getOrderCancelService()
	{
		return orderCancelService;
	}

	public void setOrderCancelService(final OrderCancelService orderCancelService)
	{
		this.orderCancelService = orderCancelService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public BraintreePartialCaptureService getBraintreePartialCaptureService()
	{
		return braintreePartialCaptureService;
	}

	public void setBraintreePartialCaptureService(BraintreePartialCaptureService braintreePartialCaptureService)
	{
		this.braintreePartialCaptureService = braintreePartialCaptureService;
	}

    public BraintreeDefaultCallContextController getCallContextController() {
        return callContextController;
    }

    public void setCallContextController(BraintreeDefaultCallContextController callContextController) {
        this.callContextController = callContextController;
    }
}
