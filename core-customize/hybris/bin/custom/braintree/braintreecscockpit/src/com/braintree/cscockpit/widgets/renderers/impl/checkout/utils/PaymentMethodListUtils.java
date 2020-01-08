package com.braintree.cscockpit.widgets.renderers.impl.checkout.utils;

import static com.braintree.constants.BraintreeConstants.PAYPAL_PAYMENT;
import static com.braintree.constants.BraintreeConstants.PAY_PAL_EXPRESS_CHECKOUT;

import de.hybris.platform.cockpit.model.meta.TypedObject;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.braintree.cscockpit.widgets.renderers.utils.TypeUtils;
import com.braintree.enums.BrainTreePaymentMethod;
import com.braintree.model.BrainTreePaymentInfoModel;


public class PaymentMethodListUtils
{
	private PaymentMethodListUtils()
	{
	}

	public static boolean isBraintreePaymentMethod(TypedObject item)
	{
		return item.getObject() instanceof BrainTreePaymentInfoModel;
	}

	public static void populateBasePaymentMethodRow(Listitem row, TypedObject item)
	{
		if(!isBraintreePaymentMethod(item)){
			return;
		}

		BrainTreePaymentInfoModel paymentInfo = (BrainTreePaymentInfoModel) item.getObject();
		if (PAYPAL_PAYMENT.equals(paymentInfo.getPaymentProvider())
				|| PAY_PAL_EXPRESS_CHECKOUT.equals(paymentInfo.getPaymentProvider()))
		{
			Listcell cardType = new Listcell(BrainTreePaymentMethod.PAYPAL.getCode());
			row.appendChild(cardType);

			populatePayPalPaymentInfo(row, paymentInfo);
			Listcell cardHolder = new Listcell(paymentInfo.getCardholderName());
			row.appendChild(cardHolder);
		}
		else
		{
			Listcell cardType = new Listcell(BrainTreePaymentMethod.CREDITCARD.getCode());
			row.appendChild(cardType);
			Listcell cardNumber = new Listcell(paymentInfo.getCardNumber());
			row.appendChild(cardNumber);
			Listcell cardHolder = new Listcell(paymentInfo.getCardholderName());
			row.appendChild(cardHolder);
		}
		addAddressColumn(row, paymentInfo);
	}

	private static void populatePayPalPaymentInfo(Listitem row, BrainTreePaymentInfoModel paymentInfo)
	{
		if (paymentInfo.getPaymentInfo() != null)
		{
			Listcell info = new Listcell(paymentInfo.getPaymentInfo());
			row.appendChild(info);
		}
		else
		{
			if (paymentInfo.getBillingAddress() != null)
			{
				Listcell email = new Listcell(paymentInfo.getBillingAddress().getEmail());
				row.appendChild(email);
			}
		}
	}

	private static void addAddressColumn(Listitem row, BrainTreePaymentInfoModel paymentInfo)
	{
		String stringAddressRepresentation = TypeUtils.getStringValueFormAddress(paymentInfo.getBillingAddress());
		Listcell billingAddress = new Listcell(stringAddressRepresentation);
		row.appendChild(billingAddress);
	}

}
