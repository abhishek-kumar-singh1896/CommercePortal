package com.braintree.command.result;

import de.hybris.platform.payment.commands.result.AuthorizationResult;


public class BrainTreeAuthorizationResult extends AuthorizationResult
{
	protected boolean success;

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(final boolean success)
	{
		this.success = success;
	}
}
