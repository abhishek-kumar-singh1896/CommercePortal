package com.braintree.commands;

import de.hybris.platform.payment.commands.Command;

import com.braintree.command.request.BrainTreeSaleTransactionRequest;
import com.braintree.command.result.BrainTreeSaleTransactionResult;


public interface BrainTreePartialCaptureCommand extends Command<BrainTreeSaleTransactionRequest, BrainTreeSaleTransactionResult>
{
	//interface
}
