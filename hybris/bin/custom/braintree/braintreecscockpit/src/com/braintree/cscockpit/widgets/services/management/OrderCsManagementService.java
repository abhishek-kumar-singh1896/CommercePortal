package com.braintree.cscockpit.widgets.services.management;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.ordercancel.OrderCancelException;

import com.braintree.model.BrainTreeTransactionDetailModel;


public interface OrderCsManagementService
{
	void createOrderCancellationRequest(BrainTreeTransactionDetailModel transactionDetail, OrderModel linkedOrderWithBraintree) throws OrderCancelException, ValidationException;

	OrderModel getLinkedOrderWithBraintree(BrainTreeTransactionDetailModel transactionDetail);
}
