/**
 *
 */
package com.gallagher.core.b2b.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.commons.lang3.StringUtils;


/**
 * @author abhinavgupta03
 *
 */
public class GallagherB2BProductValidateInterceptor implements ValidateInterceptor<ProductModel>
{
	private static final int VALID_INSTRUCTION_LENGTH = 250;

	@Override
	public void onValidate(final ProductModel arg0, final InterceptorContext arg1) throws InterceptorException
	{
		if (StringUtils.isNotEmpty(arg0.getProductSpecificDetailsHeading())
				&& StringUtils.length(arg0.getProductSpecificDetailsHeading()) > VALID_INSTRUCTION_LENGTH)
		{
			throw new InterceptorException("Length of productSpecificDetailsSubHeading should be under 250 characters.");
		}
	}

}
