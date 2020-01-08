package com.braintree.cscockpit.widgets.renderers.details.impl;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;

import org.zkoss.zul.Div;

import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintree.model.TransactionCreditCardInfoModel;
import com.braintree.model.TransactionPayPalInfoModel;


public class TransactionPaymentDetailsListRender extends AbstractTransactionDetailsListRender
{
	@Override
	protected void populateKnownDetails(final Object context, final TypedObject item, final Widget widget,
			final Div detailContainer)
	{
		if (item != null && item.getObject() instanceof BrainTreeTransactionDetailModel)
		{
			final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) item.getObject();
			this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "paymentType", new Object[0])),
					this.createValueLabel(transactionDetail.getPaymentType()), detailContainer);

			//for creditCard
			if (BraintreeConstants.CREDIT_CARD_PAYMENT_TYPE.equals(transactionDetail.getPaymentType()))
			{
				final TransactionCreditCardInfoModel cartInfo = transactionDetail.getCreditCartInfo();
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "token", new Object[0])),
						this.createValueLabel(cartInfo.getToken()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "cardType", new Object[0])),
						this.createValueLabel(cartInfo.getCardType()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "cardholderName", new Object[0])),
						this.createValueLabel(cartInfo.getCardholderName()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "creditCardNumber", new Object[0])),
						this.createValueLabel(cartInfo.getCreditCardNumber()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "expirationDate", new Object[0])),
						this.createValueLabel(cartInfo.getExpirationDate()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "uniqueNumberIdentifier", new Object[0])),
						this.createValueLabel(cartInfo.getUniqueNumberIdentifier()), detailContainer);
			}

			//for PayPal
			if (BraintreeConstants.PAYPAL_PAYMENT_TYPE_NAME.equals(transactionDetail.getPaymentType()))
			{
				final TransactionPayPalInfoModel payPalInfo = transactionDetail.getPayPalInfo();
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "token", new Object[0])),
						this.createValueLabel(payPalInfo.getToken()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payerEmail", new Object[0])),
						this.createValueLabel(payPalInfo.getPayerEmail()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payerID", new Object[0])),
						this.createValueLabel(payPalInfo.getPayerID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payerFirstName", new Object[0])),
						this.createValueLabel(payPalInfo.getPayerFirstName()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payerLastName", new Object[0])),
						this.createValueLabel(payPalInfo.getPayerLastName()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payeeEmail", new Object[0])),
						this.createValueLabel(payPalInfo.getPayeeEmail()), detailContainer);

				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "paymentID", new Object[0])),
						this.createValueLabel(payPalInfo.getPaymentID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "authUniqTransID", new Object[0])),
						this.createValueLabel(payPalInfo.getAuthorizationUniqueTransactionID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payPalDebugID", new Object[0])),
						this.createValueLabel(payPalInfo.getPayPalDebugID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payPalCustomField", new Object[0])),
						this.createValueLabel(payPalInfo.getPayPalCustomField()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payPalSellerProtection", new Object[0])),
						this.createValueLabel(payPalInfo.getPayPalSellerProtection()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payPalCaptureID", new Object[0])),
						this.createValueLabel(payPalInfo.getPayPalCaptureID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "payPalRefundID", new Object[0])),
						this.createValueLabel(payPalInfo.getPayPalRefundID()), detailContainer);

			}
		}
	}
}
