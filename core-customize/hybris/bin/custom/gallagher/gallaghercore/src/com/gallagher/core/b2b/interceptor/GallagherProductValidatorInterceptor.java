/**
 *
 */
package com.gallagher.core.b2b.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


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
		}
	}

}
