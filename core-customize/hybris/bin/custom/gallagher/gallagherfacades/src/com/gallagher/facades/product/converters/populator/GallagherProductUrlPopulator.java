/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;


/**
 *
 */
public class GallagherProductUrlPopulator extends ProductUrlPopulator
{
	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		super.populate(source, target);
		target.setName(source.getMarketingDescription());
	}
}
