package com.gallagher.core.jalo.synchronization;

import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


public class GallagherCatalogVersionSyncJob extends GeneratedGallagherCatalogVersionSyncJob
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GallagherCatalogVersionSyncJob.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected SessionContext createSyncSessionContext(final SyncItemCronJob cronJob)
	{
		final SessionContext ctx = super.createSyncSessionContext(cronJob);
		ctx.setAttribute(FlexibleSearch.DISABLE_RESTRICTIONS, Boolean.FALSE);
		ctx.setAttribute(FlexibleSearch.DISABLE_RESTRICTION_GROUP_INHERITANCE, Boolean.FALSE);
		return ctx;
	}
}
