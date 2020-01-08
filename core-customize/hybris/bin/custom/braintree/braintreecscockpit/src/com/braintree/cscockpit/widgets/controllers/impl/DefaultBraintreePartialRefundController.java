package com.braintree.cscockpit.widgets.controllers.impl;


import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.BraintreePartialRefundController;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Map;


public class DefaultBraintreePartialRefundController extends AbstractCsWidgetController implements
		BraintreePartialRefundController
{

	private static final Logger LOG = Logger.getLogger(DefaultBraintreePartialRefundController.class);

	private OrderManagementActionsWidgetController orderManagementActionsWidgetController;

	private CsBrainTreeFacade csBrainTreeFacade;

	@Override
	public TypedObject getOrder()
	{
		return getOrderManagementActionsWidgetController().getOrder();
	}

	@Override
	public BrainTreeResponseResultData partialRefund(PaymentTransactionEntryModel paymentTransactionEntry,
			BigDecimal amount) throws BraintreeErrorException
	{
		return getCsBrainTreeFacade().partialRefundTransaction((OrderModel) getOrder().getObject(), paymentTransactionEntry, amount);
	}


	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		this.getOrderManagementActionsWidgetController().dispatchEvent((String) null, source, data);
	}

	public OrderManagementActionsWidgetController getOrderManagementActionsWidgetController()
	{
		return orderManagementActionsWidgetController;
	}

	public void setOrderManagementActionsWidgetController(
			OrderManagementActionsWidgetController orderManagementActionsWidgetController)
	{
		this.orderManagementActionsWidgetController = orderManagementActionsWidgetController;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

}
