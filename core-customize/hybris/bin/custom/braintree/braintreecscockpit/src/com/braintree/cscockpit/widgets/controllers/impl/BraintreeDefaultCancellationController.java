package com.braintree.cscockpit.widgets.controllers.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCancellationController;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.BraintreeCancellationController;
import com.google.common.collect.Iterables;


public class BraintreeDefaultCancellationController extends DefaultCancellationController implements
		BraintreeCancellationController
{
	private CsBrainTreeFacade csBrainTreeFacade;
	private OrderCancelStateMappingStrategy stateMappingStrategy;


	@Override
	public TypedObject createOrderCancellationRequest(final ObjectValueContainer cancelRequest) throws OrderCancelException,
			ValidationException
	{

		final TypedObject order = getOrder();
		if ((order != null) && (order.getObject() instanceof OrderModel))
		{
			final OrderModel orderModel = (OrderModel) order.getObject();
			return createOrderCancellationRequest(cancelRequest, orderModel);
		}
		return null;
	}

	@Override
	public TypedObject createOrderCancellationRequest(final ObjectValueContainer cancelRequest, final OrderModel orderModel)
			throws ValidationException, OrderCancelException
	{
		if (orderModel != null && validateCreateCancellationRequest(orderModel, cancelRequest))
		{
			validateOrderStatus(orderModel);
			voidBraintreeTransaction(orderModel);
			final OrderCancelRequest request = buildCancelRequest(orderModel, cancelRequest);
			if (request != null)
			{
				final OrderCancelRecordEntryModel orderRequestRecord = getOrderCancelService().requestOrderCancel(request,
						getUserService().getCurrentUser());
				return getCockpitTypeService().wrapItem(orderRequestRecord);
			}
		}
		return null;
	}

	private void validateOrderStatus(OrderModel orderModel) throws ValidationException
	{
		OrderCancelState orderCancelState = getStateMappingStrategy().getOrderCancelState(orderModel);
		if (orderCancelState.equals(OrderCancelState.CANCELIMPOSSIBLE)
				|| orderCancelState.equals(OrderCancelState.PARTIALLYSHIPPED))
		{
			throw new ValidationException(Arrays.asList(new ResourceMessage(
					"cancelRecordEntry.validation.cancelRequestReason.wrongState")));
		}
	}

	protected void voidBraintreeTransaction(OrderModel orderModel) throws OrderCancelException
	{
		final List<PaymentTransactionModel> paymentTransactions = orderModel.getPaymentTransactions();
		if (CollectionUtils.isNotEmpty(paymentTransactions))
		{
			final PaymentTransactionModel transaction = Iterables.getLast(paymentTransactions);
			getCsBrainTreeFacade().voidTransaction(transaction);
		}
		else
		{
			throw new IllegalStateException("No paymentTransactions for order");
		}
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public OrderCancelStateMappingStrategy getStateMappingStrategy()
	{
		return stateMappingStrategy;
	}

	public void setStateMappingStrategy(OrderCancelStateMappingStrategy stateMappingStrategy)
	{
		this.stateMappingStrategy = stateMappingStrategy;
	}
}
