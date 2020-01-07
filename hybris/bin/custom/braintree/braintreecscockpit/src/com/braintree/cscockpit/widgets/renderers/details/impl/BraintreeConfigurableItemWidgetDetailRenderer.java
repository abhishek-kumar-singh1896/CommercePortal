package com.braintree.cscockpit.widgets.renderers.details.impl;

import static com.braintree.constants.BraintreeConstants.PAYPAL_PAYMENT;
import static com.braintree.constants.BraintreeConstants.PAY_PAL_EXPRESS_CHECKOUT;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.ConfigurableItemWidgetDetailRenderer;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;

import com.braintree.cscockpit.widgets.renderers.utils.TypeUtils;
import com.braintree.enums.BrainTreePaymentMethod;
import com.braintree.model.BrainTreePaymentInfoModel;


public class BraintreeConfigurableItemWidgetDetailRenderer extends ConfigurableItemWidgetDetailRenderer
{
	@Override
	public HtmlBasedComponent createContent(Object context, TypedObject item, Widget widget)
	{
		Div detailContainer = new Div();
		if (item != null && item.getObject() != null)
		{
			if (item.getObject() instanceof BrainTreePaymentInfoModel)
			{
				renderPaymentInfo(item, widget, detailContainer);
			}
			else
			{
				return super.createContent(context, item, widget);
			}
		}
		else
		{
			detailContainer.setSclass("csObjectCreditCardPaymentInfoContainer");
		}
		return detailContainer;
	}

	public void renderPaymentInfo(TypedObject item, Widget widget, Div detailContainer)
	{
		detailContainer.setSclass("csObjectCreditCardPaymentInfoContainer");

		BrainTreePaymentInfoModel paymentInfo = (BrainTreePaymentInfoModel) item.getObject();
		if (PAYPAL_PAYMENT.equals(paymentInfo.getPaymentProvider())
				|| PAY_PAL_EXPRESS_CHECKOUT.equals(paymentInfo.getPaymentProvider()))
		{
			this.renderRow(LabelUtils.getLabel(widget, "paymentMethodOwner"), getPayPalPaymentInfo(paymentInfo), detailContainer);
			this.renderRow(LabelUtils.getLabel(widget, "paymentMethodType"), BrainTreePaymentMethod.PAYPAL.getCode(),
					detailContainer);
		}
		else
		{
			this.renderRow(LabelUtils.getLabel(widget, "cardholderName"), paymentInfo.getCardholderName(), detailContainer);
			this.renderRow(LabelUtils.getLabel(widget, "cardNumber"), paymentInfo.getCardNumber(), detailContainer);

			if (paymentInfo.getCardType() != null)
			{
				this.renderRow(LabelUtils.getLabel(widget, "paymentMethodType"), paymentInfo.getCardType().getCode(), detailContainer);
			}
			else
			{
				this.renderRow(LabelUtils.getLabel(widget, "paymentMethodType"), BrainTreePaymentMethod.CREDITCARD.getCode(),
						detailContainer);
			}

			this.renderRow(LabelUtils.getLabel(widget, "cardExpirationYear"), paymentInfo.getExpirationYear(), detailContainer);
			this.renderRow(LabelUtils.getLabel(widget, "cardExpirationMonth"), paymentInfo.getExpirationMonth(), detailContainer);
		}
		String stringAddressRepresentation = TypeUtils.getStringValueFormAddress(paymentInfo.getBillingAddress());
		this.renderRow(LabelUtils.getLabel(widget, "billingAddress"), stringAddressRepresentation, detailContainer);
	}

	private String getPayPalPaymentInfo(BrainTreePaymentInfoModel paymentInfo)
	{
		if (paymentInfo.getPaymentInfo() != null)
		{
			return paymentInfo.getPaymentInfo();
		}
		else
		{
			if (paymentInfo.getBillingAddress() != null)
			{
				return paymentInfo.getBillingAddress().getEmail();
			}
		}
		return StringUtils.EMPTY;
	}
}
