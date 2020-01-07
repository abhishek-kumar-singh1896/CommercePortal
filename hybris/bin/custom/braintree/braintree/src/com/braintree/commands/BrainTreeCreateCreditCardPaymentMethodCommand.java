/**
 *
 */
package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeCreateCreditCardPaymentMethodRequest;
import com.braintree.command.result.BrainTreePaymentMethodResult;


public interface BrainTreeCreateCreditCardPaymentMethodCommand extends
		Command<BrainTreeCreateCreditCardPaymentMethodRequest, BrainTreePaymentMethodResult>
{
	//interface
}
