package com.braintree.cscockpit.services;

import com.braintree.command.request.BrainTreeFindTransactionRequest;
import com.braintree.command.result.BrainTreeFindTransactionResult;


public interface TransactionSearchService
{
	BrainTreeFindTransactionResult findTransactions(final BrainTreeFindTransactionRequest findCustomerRequest);
}
