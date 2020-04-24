package com.gallagher.sap.sapcpiorderexchange.inbound.events;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.persistence.hook.PrePersistHook;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Stock persistence Hook to be used by SCPI
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOmmStockLevelPersistenceHook implements PrePersistHook
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherSCPIOmmStockLevelPersistenceHook.class);

	@Override
	public Optional<ItemModel> execute(final ItemModel item)
	{
		LOG.info("The persistence hook GallagherSCPIOmmStockLevelPersistenceHook is called!");
		return Optional.of(item);
	}
}
