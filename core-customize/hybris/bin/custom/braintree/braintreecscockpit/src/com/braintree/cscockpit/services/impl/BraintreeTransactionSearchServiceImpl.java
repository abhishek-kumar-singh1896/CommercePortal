package com.braintree.cscockpit.services.impl;

import static com.braintree.constants.BraintreeConstants.BRAINTREE_PROVIDER_NAME;

import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;

import org.apache.log4j.Logger;

import com.braintree.cscockpit.services.TransactionSearchService;
import com.braintree.command.request.BrainTreeFindTransactionRequest;
import com.braintree.command.result.BrainTreeFindTransactionResult;
import com.braintree.commands.BrainTreeFindTransactionCommand;


public class BraintreeTransactionSearchServiceImpl implements TransactionSearchService
{
	private final static Logger LOG = Logger.getLogger(BraintreeTransactionSearchServiceImpl.class);
	private CommandFactoryRegistry commandFactoryRegistry;

	@Override
	public BrainTreeFindTransactionResult findTransactions(final BrainTreeFindTransactionRequest findTransactionRequest)
			throws AdapterException
	{
		try
		{
			final BrainTreeFindTransactionCommand command = getCommandFactory().createCommand(BrainTreeFindTransactionCommand.class);
			BrainTreeFindTransactionResult result = command.perform(findTransactionRequest);
			return result;
		}
		catch (final Exception exception)
		{
			LOG.error("[BT Payment Service] Errors during trying to find transactions" + exception.getMessage());
			throw new AdapterException(exception.getMessage(), exception);
		}
	}

	private CommandFactory getCommandFactory()
	{
		return commandFactoryRegistry.getFactory(BRAINTREE_PROVIDER_NAME);
	}


	public void setCommandFactoryRegistry(CommandFactoryRegistry commandFactoryRegistry)
	{
		this.commandFactoryRegistry = commandFactoryRegistry;
	}
}
