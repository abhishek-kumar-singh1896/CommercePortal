/**
 *
 */
package com.gallagher.facades.process.email.context;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import javax.annotation.Resource;

import org.apache.logging.log4j.util.Strings;

import com.enterprisewide.b2badvance.facades.process.email.context.CustomerEmailContext;



/**
 * Velocity context for a New/Existing Customer.
 */
public class GallagherCustomerUpdateEmailContext extends CustomerEmailContext
{
	private static final String REPLY_TO = "replyTo";

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		put("ISUSEREXIST", getCustomer(storeFrontCustomerProcessModel).getIsUserExist());
		put("firstName", getCustomer().getFirstName());
		put(REPLY_TO, siteConfigService.getString("customer.email.replyTo", Strings.EMPTY));
	}

}
