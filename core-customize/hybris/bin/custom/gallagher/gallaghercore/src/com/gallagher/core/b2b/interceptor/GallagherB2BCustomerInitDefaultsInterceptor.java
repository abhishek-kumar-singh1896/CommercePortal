/**
 *
 */
package com.gallagher.core.b2b.interceptor;

import de.hybris.platform.b2b.interceptor.B2BCustomerInitDefaultsInterceptor;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;


/**
 * @author shilpiverma
 *
 */
public class GallagherB2BCustomerInitDefaultsInterceptor extends B2BCustomerInitDefaultsInterceptor
{

	@Override
	public void onInitDefaults(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof B2BCustomerModel)
		{
			final UserGroupModel employeeGroup = getOrCreateGroup(B2badvanceCoreConstants.B2BTECNICIANGROUP);
			final B2BCustomerModel customer = (B2BCustomerModel) model;
			customer.setGroups(Collections.<PrincipalGroupModel> singleton(employeeGroup));
		}
	}

}
