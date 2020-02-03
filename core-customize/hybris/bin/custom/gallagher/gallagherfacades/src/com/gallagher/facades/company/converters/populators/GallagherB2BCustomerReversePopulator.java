/**
 *
 */
package com.gallagher.facades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerReversePopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 *
 */
public class GallagherB2BCustomerReversePopulator extends B2BCustomerReversePopulator
{
	@Override
	public void populate(final CustomerData source, final B2BCustomerModel target) throws ConversionException
	{
		super.populate(source, target);

		target.setDuplicate(source.getDuplicate());
	}
}
