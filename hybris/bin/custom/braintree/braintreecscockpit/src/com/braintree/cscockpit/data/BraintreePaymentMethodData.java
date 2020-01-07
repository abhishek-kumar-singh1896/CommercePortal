package com.braintree.cscockpit.data;

import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintreegateway.PaymentMethod;


public class BraintreePaymentMethodData extends BrainTreeResponseResultData
{
	private PaymentMethod paymentMethod;

	public BraintreePaymentMethodData()
	{
	}

	public PaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(final PaymentMethod paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}
}
