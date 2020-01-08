/**
 *
 */
package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeGenerateClientTokenRequest;
import com.braintree.command.result.BrainTreeGenerateClientTokenResult;


public interface BrainTreeGenerateClientTokenCommand extends
		Command<BrainTreeGenerateClientTokenRequest, BrainTreeGenerateClientTokenResult>
{
	// interface
}
