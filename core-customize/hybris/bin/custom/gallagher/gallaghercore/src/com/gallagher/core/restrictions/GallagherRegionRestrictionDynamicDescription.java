/**
 *
 */
package com.gallagher.core.restrictions;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

import com.gallagher.core.model.GallagherRegionRestrictionModel;


/**
 * This class is used description of GallagherRegionRestriction
 *
 * @author gauravkamboj
 */
public class GallagherRegionRestrictionDynamicDescription
		implements DynamicAttributeHandler<String, GallagherRegionRestrictionModel>
{

	public static final String DESCRIPTION_TEXT = "type.GallagherRegionRestriction.description.text";

	@Override
	public String get(final GallagherRegionRestrictionModel arg0)
	{
		return Localization.getLocalizedString(DESCRIPTION_TEXT);
	}

	@Override
	public void set(final GallagherRegionRestrictionModel arg0, final String arg1)
	{
		throw new UnsupportedOperationException();
	}

}
