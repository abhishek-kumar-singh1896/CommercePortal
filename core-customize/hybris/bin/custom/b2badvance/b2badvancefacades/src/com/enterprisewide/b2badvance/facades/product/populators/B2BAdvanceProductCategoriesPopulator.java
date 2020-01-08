/**
 *
 */
package com.enterprisewide.b2badvance.facades.product.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.Collection;


/**
 * Populates categories into product data
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceProductCategoriesPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductCategoriesPopulator<SOURCE, TARGET>
{
	/**
	 * {@inheritDoc}
	 *
	 * @description This method is overridden from ProductCategoriesPopulator to add the count of variant categories
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final Collection<CategoryModel> categories = getCommerceProductService()
				.getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		productData.setCategories(getCategoryConverter().convertAll(categories));
		int variantCategoriesCount = 0;
		for (final CategoryModel category : categories)
		{
			if ((productModel instanceof GenericVariantProductModel && category instanceof VariantValueCategoryModel)
					|| category instanceof VariantCategoryModel)
			{
				variantCategoriesCount++;
			}
		}
		productData.setTotalVariantCategories(variantCategoriesCount);
	}
}
