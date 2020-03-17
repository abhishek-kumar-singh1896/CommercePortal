/**
 *
 */
package com.gallagher.facades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerReversePopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Gallagher Populator to populate B2BCustomerModel with values from CustomerData
 *
 * @author shishirkant
 */
public class GallagherB2BCustomerReversePopulator extends B2BCustomerReversePopulator
{
	@Override
	public void populate(final CustomerData source, final B2BCustomerModel target) throws ConversionException
	{
		super.populate(source, target);

		target.setSapContactID(source.getCustomerId());
		target.setDuplicate(source.getDuplicate());
		target.setKeycloakGUID(source.getKeycloakGUID());
		target.setObjectID(source.getObjectID());
		target.setIsUserExist(source.isIsUserExist());
	}
}
