/**
 *
 */
package com.gallagher.core.b2b.interceptor;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.HashSet;
import java.util.Set;


/**
 * @author ishaanvashishth
 *
 */
public class GallagherProductValidatorInterceptor<MODEL extends ProductModel> implements ValidateInterceptor<MODEL>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onValidate(final MODEL model, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isModified(model, ProductModel.RECOMMENDEDCATEGORIES) || ctx.isModified(model, ProductModel.RECOMMENDEDPRODUCTS))
		{
			if (null != model.getRecommendedProducts() && !model.getRecommendedProducts().isEmpty()
					&& null != model.getRecommendedCategories() && !model.getRecommendedCategories().isEmpty())
			{
				throw new InterceptorException("Recommended Products and Categories cannot be set together.");
			}
			else
			{
				if (null != model.getRecommendedProducts() && !model.getRecommendedProducts().isEmpty())
				{
					final Set<ProductModel> productsSet = new HashSet<ProductModel>();
					for (final ProductModel product : model.getRecommendedProducts())
					{
						if (!productsSet.add(product))
						{
							throw new InterceptorException("Duplicate products in recommended products.");
						}
					}

				}
				if (null != model.getRecommendedCategories() && !model.getRecommendedCategories().isEmpty())
				{
					final Set<CategoryModel> categoriesSet = new HashSet<CategoryModel>();
					for (final CategoryModel category : model.getRecommendedCategories())
					{
						if (!categoriesSet.add(category))
						{
							throw new InterceptorException("Duplicate categories in recommended categories.");
						}
					}

				}
			}
		}
	}

}
