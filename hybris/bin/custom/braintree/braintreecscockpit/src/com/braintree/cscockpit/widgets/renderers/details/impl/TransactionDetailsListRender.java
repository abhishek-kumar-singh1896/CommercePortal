package com.braintree.cscockpit.widgets.renderers.details.impl;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;

import org.zkoss.zul.Div;

import com.braintree.model.BrainTreeTransactionDetailModel;


public class TransactionDetailsListRender extends AbstractTransactionDetailsListRender
{
	@Override
	protected void populateKnownDetails(final Object context, final TypedObject item, final Widget widget,
			final Div detailContainer)
	{
		if (item != null && item.getObject() instanceof BrainTreeTransactionDetailModel)
		{
			final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) item.getObject();
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "transactionID", new Object[0])),
					this.createValueLabel(transactionDetail.getId()), detailContainer);
			//			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "merchant", new Object[0])),
			//					this.createValueLabel(transactionDetail.getMerchant()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "merchantAccount", new Object[0])),
					this.createValueLabel(transactionDetail.getMerchantAccount()), detailContainer);
			renderRefundRow(widget, detailContainer, transactionDetail);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "transactionType", new Object[0])),
					this.createValueLabel(transactionDetail.getType()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "amount", new Object[0])),
					this.createValueLabel(transactionDetail.getAmount()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "transactionDate", new Object[0])),
					this.createValueLabel(transactionDetail.getTransactionDate()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "transactionStatus", new Object[0])),
					this.createValueLabel(transactionDetail.getStatus()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "settlementBatch", new Object[0])),
					this.createValueLabel(transactionDetail.getSettlementBatch()), detailContainer);
			//for creditCard
			renderCreditCardInfo(widget, detailContainer, transactionDetail);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "riskDecision", new Object[0])),
					this.createValueLabel(transactionDetail.getRiskDecision()), detailContainer);
		}

	}

	private void renderRefundRow(final Widget widget, final Div detailContainer,
			final BrainTreeTransactionDetailModel transactionDetail)
	{
		if (transactionDetail.getRefund() != null)
		{
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "refund", new Object[0])),
					this.createValueLabel(transactionDetail.getRefund()), detailContainer);
		}
	}

	private void renderCreditCardInfo(final Widget widget, final Div detailContainer,
			final BrainTreeTransactionDetailModel transactionDetail)
	{
		if (BraintreeConstants.CREDIT_CARD_PAYMENT_TYPE.equals(transactionDetail.getPaymentType()))
		{
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "processorAuthorizationCode", new Object[0])),
					this.createValueLabel(transactionDetail.getProcessorAuthorizationCode()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "cvvResponse", new Object[0])),
					this.createValueLabel(transactionDetail.getCvvResponse()), detailContainer);
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "avsResponse", new Object[0])),
					this.createValueLabel(transactionDetail.getAvsResponse()), detailContainer);
		}
	}
}
