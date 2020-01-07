package com.braintree.cscockpit.data;


public class BraintreePaymentMethodInfo extends BrainTreeInfo
{
	private boolean isDefaultPaymentMethod;
	private String newPaymentMethodToken;

	public boolean isDefaultPaymentMethod()
	{
		return isDefaultPaymentMethod;
	}

	public BraintreePaymentMethodInfo setDefaultPaymentMethod(final boolean defaultPaymentMethod)
	{
		isDefaultPaymentMethod = defaultPaymentMethod;
		return this;
	}

	public String getNewPaymentMethodToken()
	{
		return newPaymentMethodToken;
	}

	public BraintreePaymentMethodInfo setNewPaymentMethodToken(final String newPaymentMethodToken)
	{
		this.newPaymentMethodToken = newPaymentMethodToken;
		return this;
	}
}
