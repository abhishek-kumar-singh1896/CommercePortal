/**
 *
 */
package com.gallagher.core.customer.interceptor;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import com.gallagher.core.enums.BU;


/**
 * @author shilpiverma
 *
 */
public class GallagherCustomerInitDefaultInterceptor implements InitDefaultsInterceptor
{
	@Override
	public void onInitDefaults(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) model;
			customer.setUid(BU.SEC.getCode().toLowerCase() + "|");
		}
		else if (model instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) model;
			customer.setUid(BU.AM.getCode().toLowerCase() + "|");
		}
	}
}
