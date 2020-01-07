/**
 *
 */
package com.braintree.command.request;

import de.hybris.platform.payment.commands.request.AbstractRequest;


public abstract class BrainTreeAbstractRequest extends AbstractRequest
{

	protected BrainTreeAbstractRequest(final String merchantTransactionCode)
	{
		super(merchantTransactionCode);
	}

	private String paymentProvider;

	public String getPaymentProvider()
	{
		return paymentProvider;
	}

	public void setPaymentProvider(final String paymentProvider)
	{
		this.paymentProvider = paymentProvider;
	}
}
