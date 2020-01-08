/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;


/**
 * @author Enterprise Wide
 *
 */
public class B2badvanceCategoryPopulator extends CategoryPopulator
{
	/*
	 * @description This method is overrided from CategoryPopulator to display the products count
	 */
	@Override
	public void populate(final CategoryModel source, final CategoryData target)
	{

		super.populate(source, target);

		if (source.getProducts() != null && source.getProducts().size() > 0)
		{
			target.setProductCount(new Long(source.getProducts().size()));
		}
	}
}
