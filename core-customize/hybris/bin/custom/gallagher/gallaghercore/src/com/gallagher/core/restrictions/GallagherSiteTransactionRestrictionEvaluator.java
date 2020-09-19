package com.gallagher.core.restrictions;



import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import com.gallagher.core.model.GallagherSiteTransactionRestrictionModel;




/**
 * Evaluator to check whether Transaction on Site is enabled or not
 *
 * @author shishirkant
 */
public class GallagherSiteTransactionRestrictionEvaluator
		implements CMSRestrictionEvaluator<GallagherSiteTransactionRestrictionModel>
{
	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "defaultB2BUnitService")
	private B2BUnitService b2BUnitService;

	private static final String TRANSACTION_ENABLED = "transaction.enabled";

	@Override
	public boolean evaluate(final GallagherSiteTransactionRestrictionModel arg0, final RestrictionData arg1)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		boolean b2bUnitTransactional = true;

		if (currentCustomer instanceof B2BCustomerModel && null != b2BUnitService.getParent((B2BCustomerModel) currentCustomer))
		{
			b2bUnitTransactional = ((B2BUnitModel) b2BUnitService.getParent((B2BCustomerModel) currentCustomer)).getTransactional();
		}
		return (b2bUnitTransactional && siteConfigService.getBoolean(TRANSACTION_ENABLED, true));
	}

}
