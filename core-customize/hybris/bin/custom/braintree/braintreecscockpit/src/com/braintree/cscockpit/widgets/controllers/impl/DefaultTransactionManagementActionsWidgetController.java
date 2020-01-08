package com.braintree.cscockpit.widgets.controllers.impl;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.braintree.converters.utils.BraintreeTransactionConverterUtils;
import com.braintree.cscockpit.widgets.controllers.TransactionManagementActionsWidgetController;
import com.braintree.cscockpit.widgets.services.security.BraintreeAccessRightsService;
import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintreegateway.Transaction;


public class DefaultTransactionManagementActionsWidgetController extends AbstractCsWidgetController
		implements TransactionManagementActionsWidgetController
{
	private BraintreeDefaultCallContextController callContextController;
	private BraintreeAccessRightsService braintreeAccessRightsService;

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		callContextController.setImpersonationContextChanged();
		this.getCallContextController().dispatchEvent((String) null, source, data);
	}

	@Override
	public boolean isVoidPossible()
	{
		if (getTransaction() != null && getTransaction().getObject() != null)
		{
			final BrainTreeTransactionDetailModel transaction = (BrainTreeTransactionDetailModel) getTransaction().getObject();
			final String status = transaction.getStatus();
			return Transaction.Status.AUTHORIZED.toString().equals(status);
		}
		return false;
	}

	@Override
	public boolean isClonePossible()
	{
		if (getTransaction() != null && getTransaction().getObject() != null)
		{
			final BrainTreeTransactionDetailModel transaction = (BrainTreeTransactionDetailModel) getTransaction().getObject();

			final String status = transaction.getStatus();
			if (BraintreeConstants.CREDIT_CARD_PAYMENT_TYPE.equals(transaction.getPaymentType()))
			{
				return Transaction.Status.AUTHORIZED.toString().equals(status)
						|| Transaction.Status.AUTHORIZATION_EXPIRED.toString().equals(status)
						|| Transaction.Status.VOIDED.toString().equals(status) || Transaction.Status.SETTLED.toString().equals(status)
						|| Transaction.Status.SUBMITTED_FOR_SETTLEMENT.toString().equals(status);
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean isRefundPossible()
	{
		if (getTransaction() != null && getTransaction().getObject() != null)
		{
			final BrainTreeTransactionDetailModel transaction = (BrainTreeTransactionDetailModel) getTransaction().getObject();
			final String status = transaction.getStatus();

			if (BraintreeConstants.PAYPAL_PAYMENT_TYPE_NAME.equals(transaction.getPaymentType()))
			{
				return (Transaction.Status.SETTLED.toString().equals(status) || Transaction.Status.SETTLING.toString().equals(status)
						|| Transaction.Status.SETTLEMENT_PENDING.toString().equals(status))
						&& !BraintreeTransactionConverterUtils.TRANSACTION_REFUND_SIGN.equals(transaction.getRefund());
			}
			return (Transaction.Status.SETTLED.toString().equals(status) || Transaction.Status.SETTLING.toString().equals(status))
					&& !BraintreeTransactionConverterUtils.TRANSACTION_REFUND_SIGN.equals(transaction.getRefund());
		}
		return false;
	}

	@Override
	public boolean isSubmitForSettlementPossible()
	{
		if (getTransaction() != null && getTransaction().getObject() != null)
		{
			final BrainTreeTransactionDetailModel transaction = (BrainTreeTransactionDetailModel) getTransaction().getObject();
			final String status = transaction.getStatus();
			return Transaction.Status.AUTHORIZED.toString().equals(status);
		}
		return false;
	}

	@Override
	public TypedObject getTransaction()
	{
		return this.getCallContextController().getCurrentTransaction();
	}

	@Override
	public void setTransaction(TypedObject typedObject)
	{
		this.getCallContextController().setCurrentTransaction(typedObject);
	}

	protected BraintreeDefaultCallContextController getCallContextController()
	{
		return this.callContextController;
	}

	@Required
	public void setCallContextController(final BraintreeDefaultCallContextController callContextController)
	{
		this.callContextController = callContextController;
	}

	@Override
	public boolean canUserVoid()
	{
		return getBraintreeAccessRightsService().canVoid();
	}

	@Override
	public boolean canUserClone()
	{
		return getBraintreeAccessRightsService().canClone();
	}

	@Override
	public boolean canUserRefund()
	{
		return getBraintreeAccessRightsService().canRefund();
	}

	@Override
	public boolean canUserSubmitForSettlement()
	{
		return getBraintreeAccessRightsService().canSubmitForSettlement();
	}

	/**
	 * @return the braintreeAccessRightsService
	 */
	public BraintreeAccessRightsService getBraintreeAccessRightsService()
	{
		return braintreeAccessRightsService;
	}

	/**
	 * @param braintreeAccessRightsService
	 *           the braintreeAccessRightsService to set
	 */
	public void setBraintreeAccessRightsService(final BraintreeAccessRightsService braintreeAccessRightsService)
	{
		this.braintreeAccessRightsService = braintreeAccessRightsService;
	}
}
