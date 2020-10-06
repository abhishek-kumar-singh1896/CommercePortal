/**
 *
 */
package com.gallagher.facades.process.email.context;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerUpdateProcessModel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;

import com.enterprisewide.b2badvance.facades.process.email.context.CustomerEmailContext;



/**
 * @author ankituniyal
 *
 *         Velocity context for a Existing Customer.
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

		if (storeFrontCustomerProcessModel instanceof StoreFrontCustomerUpdateProcessModel)
		{
			final StoreFrontCustomerUpdateProcessModel storeFrontCustomerUpdateProcessModel = (StoreFrontCustomerUpdateProcessModel) storeFrontCustomerProcessModel;

			final Map<String, String> modifiedAttributesMap = storeFrontCustomerUpdateProcessModel.getModifiedAttributesMap();

			final Set<Entry<String, String>> entrySet = modifiedAttributesMap.entrySet();
			for (final Entry<String, String> entry : entrySet)
			{
				if (StringUtils.isNotBlank(entry.getValue()))
				{
					put(entry.getKey(), entry.getValue());
				}
			}
		}

		put("firstName", getCustomer().getFirstName());
		put(REPLY_TO, siteConfigService.getString("customer.email.replyTo", Strings.EMPTY));
	}

}
