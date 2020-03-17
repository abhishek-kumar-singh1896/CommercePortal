package com.gallagher.core.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;

import com.gallagher.core.restrictions.GallagherSiteTransactionRestrictionDynamicDescription;


public class GallagherSiteTransactionRestriction extends GeneratedGallagherSiteTransactionRestriction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GallagherSiteTransactionRestriction.class.getName());

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

	@Override
	public String getDescription(final SessionContext arg0)
	{
		return Localization.getLocalizedString(GallagherSiteTransactionRestrictionDynamicDescription.DESCRIPTION_TEXT);
	}

}
