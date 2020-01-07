package com.braintree.cscockpit.widgets.services.management.impl;


import com.braintree.cscockpit.widgets.services.management.OrderCsManagementService;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.BasicPropertyDescriptor;
import de.hybris.platform.ordercancel.OrderCancelException;

import org.apache.commons.lang.StringUtils;

import com.braintree.cscockpit.dao.CsOrderDao;
import com.braintree.cscockpit.widgets.controllers.BraintreeCancellationController;
import com.braintree.model.BrainTreeTransactionDetailModel;


public class BraintreeDefaultOrderCsManagementService implements OrderCsManagementService
{
	private CsOrderDao csOrderDao;
	private BraintreeCancellationController csCancellationController;

	@Override
	public void createOrderCancellationRequest(BrainTreeTransactionDetailModel transactionDetail,
			OrderModel linkedOrderWithBraintree) throws OrderCancelException, ValidationException
	{

		ObjectValueContainer currentObjectValues = new ObjectValueContainer((ObjectType) null, (Object) null);
		currentObjectValues.addValue(new BasicPropertyDescriptor("OrderCancelRequest.notes", "TEXT"), null, StringUtils.EMPTY);
		currentObjectValues.addValue(new BasicPropertyDescriptor("OrderCancelRequest.cancelReason", "ENUM"), null,
				CancelReason.CUSTOMERREQUEST);

		getCsCancellationController().createOrderCancellationRequest(currentObjectValues, linkedOrderWithBraintree);
	}

	@Override
	public OrderModel getLinkedOrderWithBraintree(BrainTreeTransactionDetailModel transactionDetail)
	{
		return csOrderDao.findOrderByBraintreeTransactionID(transactionDetail.getId());
	}

	public BraintreeCancellationController getCsCancellationController()
	{
		return csCancellationController;
	}

	public void setCsCancellationController(BraintreeCancellationController csCancellationController)
	{
		this.csCancellationController = csCancellationController;
	}

	public CsOrderDao getCsOrderDao()
	{
		return csOrderDao;
	}

	public void setCsOrderDao(CsOrderDao csOrderDao)
	{
		this.csOrderDao = csOrderDao;
	}

}
