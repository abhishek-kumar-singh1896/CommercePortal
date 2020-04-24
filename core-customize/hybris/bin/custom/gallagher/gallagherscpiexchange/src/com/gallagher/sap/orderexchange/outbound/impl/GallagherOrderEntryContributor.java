package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultOrderEntryContributor;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;


/**
 * Builds the Row map for the CSV files for the Order Entry
 *
 * @author Vikram Bishnoi
 */
public class GallagherOrderEntryContributor extends DefaultOrderEntryContributor
{
	private final static Logger LOG = Logger.getLogger(GallagherOrderEntryContributor.class);

	@Override
	protected String determineItemShortText(final AbstractOrderEntryModel item, final String language)
	{
		final String shortText = item.getProduct().getName(LocaleUtils.toLocale(language));
		return shortText == null ? "" : shortText;
	}

}
