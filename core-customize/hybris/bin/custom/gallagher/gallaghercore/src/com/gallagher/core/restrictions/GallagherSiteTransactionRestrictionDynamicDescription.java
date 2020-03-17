/**
 *
 */
package com.gallagher.core.restrictions;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

import com.gallagher.core.model.GallagherSiteTransactionRestrictionModel;


/**
 * This class is used description of GallagherSiteTransactionRestriction
 *
 * @author shishirkant
 */
public class GallagherSiteTransactionRestrictionDynamicDescription
		implements DynamicAttributeHandler<String, GallagherSiteTransactionRestrictionModel>
{

	public static final String DESCRIPTION_TEXT = "type.GallagherSiteTransactionRestriction.description.text";

	@Override
	public String get(final GallagherSiteTransactionRestrictionModel arg0)
	{
		return Localization.getLocalizedString(DESCRIPTION_TEXT);
	}

	@Override
	public void set(final GallagherSiteTransactionRestrictionModel arg0, final String arg1)
	{
		throw new UnsupportedOperationException();
	}

}
