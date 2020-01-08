/**
 *
 */
package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeCustomerRequest;
import com.braintree.command.result.BrainTreeFindCustomerResult;
import com.braintree.command.result.BrainTreeFindCustomersResult;


public interface BrainTreeFindCustomerCommand extends Command<BrainTreeCustomerRequest, BrainTreeFindCustomerResult>
{
	BrainTreeFindCustomersResult process(final BrainTreeCustomerRequest request);
}
