/**
 *
 */
package com.gallagher.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import com.enterprisewide.b2badvance.facades.process.email.context.CustomerEmailContext;



/**
 * Velocity context for a New/Existing Customer.
 */
public class GallagherCustomerEmailContext extends CustomerEmailContext
{
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		put("ISUSEREXIST", getCustomer(storeFrontCustomerProcessModel).getIsUserExist());
		put("firstName", getCustomer().getFirstName());
	}
}
