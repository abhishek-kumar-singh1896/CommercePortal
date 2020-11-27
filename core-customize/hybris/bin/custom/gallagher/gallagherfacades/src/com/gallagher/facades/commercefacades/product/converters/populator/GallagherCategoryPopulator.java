/**
 *
 */
package com.gallagher.facades.commercefacades.product.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;


/**
 * populating description from category model.
 *
 * @author shilpiverma
 */
public class GallagherCategoryPopulator extends CategoryPopulator
{
	@Override
	public void populate(final CategoryModel source, final CategoryData target)
	{
		super.populate(source, target);
		target.setDescription(source.getDescription());
		target.setCodeCompare(source.getCode());
	}

}
