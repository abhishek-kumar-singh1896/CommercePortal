/**
 *
 */
package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeDeletePaymentMethodRequest;
import com.braintree.command.result.BrainTreePaymentMethodResult;


public interface BrainTreeDeletePaymentMethodCommand extends
		Command<BrainTreeDeletePaymentMethodRequest, BrainTreePaymentMethodResult>
{
	//interface
}
