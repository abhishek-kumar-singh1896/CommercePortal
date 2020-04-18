package com.gallagher.backoffice.sync.facades;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;

import java.util.Collections;
import java.util.List;

import com.hybris.backoffice.sync.facades.DefaultSynchronizationFacade;


/**
 * Overrided the default class to remove pull from source list
 *
 * @author Vikram Bishnoi
 *
 */
public class GallagherSynchronizationFacadeImpl extends DefaultSynchronizationFacade
{

	@Override
	public List<SyncItemJobModel> getInboundSynchronizations(final CatalogVersionModel catalogVersion)
	{
		return Collections.unmodifiableList(Collections.EMPTY_LIST);
	}
}
