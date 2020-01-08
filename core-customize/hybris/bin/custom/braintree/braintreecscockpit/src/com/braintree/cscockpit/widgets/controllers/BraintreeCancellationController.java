package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;
import de.hybris.platform.ordercancel.OrderCancelException;


public interface BraintreeCancellationController extends CancellationController
{
	/**
	 * use this method to cancel order from cscockpit if you doesnt have order in controller but have standalone order
	 */
	TypedObject createOrderCancellationRequest(ObjectValueContainer cancelRequest, OrderModel order) throws OrderCancelException,
			ValidationException;
}
