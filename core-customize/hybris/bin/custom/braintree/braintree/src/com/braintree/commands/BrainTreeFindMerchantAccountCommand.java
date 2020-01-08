/**
 *
 */
package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeFindMerchantAccountRequest;
import com.braintree.command.result.BrainTreeFindMerchantAccountResult;


public interface BrainTreeFindMerchantAccountCommand extends
		Command<BrainTreeFindMerchantAccountRequest, BrainTreeFindMerchantAccountResult>
{
	@Override
	BrainTreeFindMerchantAccountResult perform(final BrainTreeFindMerchantAccountRequest request);
}
