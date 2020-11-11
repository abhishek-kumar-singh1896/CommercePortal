/**
 *
 */
package com.gallagher.core.b2b.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
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
	private static final int VALID_INSTRUCTION_LENGTH = 80;
	private L10NService l10NService;

	/**
	 * @return the l10NService
	 */
	public L10NService getL10NService()
	{
		return l10NService;
	}

	/**
	 * @param l10nService
	 *           the l10NService to set
	 */
	public void setL10NService(final L10NService l10nService)
	{
		l10NService = l10nService;
	}

	@Override
	public void onValidate(final ProductModel arg0, final InterceptorContext arg1) throws InterceptorException
	{
		if (StringUtils.isNotEmpty(arg0.getProductSpecificDetailsHeading())
				&& StringUtils.length(arg0.getProductSpecificDetailsHeading()) > VALID_INSTRUCTION_LENGTH)
		{
			throw new InterceptorException(
					getL10NService().getLocalizedString("error.b2bProduct.productSpecificDetailsSubHeading.length.invalid"));
		}
	}

}
