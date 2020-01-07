/**
 *
 */
package com.braintree.command.request;

public class BrainTreeFindMerchantAccountRequest extends BrainTreeAbstractRequest
{
	public BrainTreeFindMerchantAccountRequest(final String merchantTransactionCode)
	{
		super(merchantTransactionCode);
	}

	private String merchantAccount;

	public String getMerchantAccount()
	{
		return merchantAccount;
	}

	public void setMerchantAccount(final String merchantAccount)
	{
		this.merchantAccount = merchantAccount;
	}

}
