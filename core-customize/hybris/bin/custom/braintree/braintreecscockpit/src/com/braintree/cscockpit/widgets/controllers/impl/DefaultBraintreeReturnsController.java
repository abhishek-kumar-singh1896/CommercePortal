package com.braintree.cscockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultReturnsController;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.order.refund.BraintreeRefundService;
import com.braintreegateway.Transaction;


public class DefaultBraintreeReturnsController extends DefaultReturnsController
{
	private static final Logger LOG = Logger.getLogger(DefaultBraintreeReturnsController.class);
	private CsBrainTreeFacade csBrainTreeFacade;
	private BraintreeRefundService refundService;

	@Override
	public TypedObject createRefundOrderPreview(List<ObjectValueContainer> refundEntriesValueContainers)
	{
		OrderModel orderModel = getOrderModel();
		if (isRefundPossible(orderModel))
		{
			return super.createRefundOrderPreview(refundEntriesValueContainers);
		}
		else
		{
			showMessage(Labels.getLabel("cscockpit.widget.transaction.refund.denied"), "Refund transaction Error", Messagebox.ERROR);
			this.deleteRefundOrderPreview();
			return null;
		}
	}

	/**
	 * copy method applyRefunds from DefaultReturnsController for adding BT refund external command
	 */
	@Override
	protected OrderModel applyRefunds(final OrderModel orderModel, final ReturnRequestModel request,
			final Map<Long, RefundDetails> refundDetailsList, final boolean history)
	{
		if (history)
		{
            getRefundService().applyBraintreeRefund(this.refundOrderPreview, orderModel, request);
		}
		return super.applyRefunds(orderModel, request, refundDetailsList, history);
	}



	private boolean isRefundPossible(final OrderModel order)
	{
		final List<PaymentTransactionModel> paymentTransactions = order.getPaymentTransactions();
		if (CollectionUtils.isNotEmpty(paymentTransactions))
		{
			PaymentTransactionModel paymentTransactionModel = paymentTransactions.get(0);
			final BraintreeTransactionEntryData brainTreeTransaction = getCsBrainTreeFacade().findBrainTreeTransaction(
					paymentTransactionModel.getRequestId());
			if (brainTreeTransaction == null)
			{
				throw new AdapterException("BT No transaction with id:" + paymentTransactionModel.getRequestId() + " in BrainTree");
			}
			final String status = brainTreeTransaction.getStatus();

			if (Transaction.Status.SETTLED.toString().equals(status) || Transaction.Status.SETTLING.toString().equals(status))
			{
				return true;
			}
		}
		return false;
	}

	private void showMessage(final String errMessage, final String title, final String icon)
	{
		try
		{
			Messagebox.show(errMessage, title, Messagebox.OK, icon);
		}
		catch (final InterruptedException e)
		{
			LOG.error("Errors occurred while showing message box!", e);
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

	@Override
	public BraintreeRefundService getRefundService()
	{
		return refundService;
	}

	public void setRefundService(BraintreeRefundService refundService)
	{
		this.refundService = refundService;
	}
}
