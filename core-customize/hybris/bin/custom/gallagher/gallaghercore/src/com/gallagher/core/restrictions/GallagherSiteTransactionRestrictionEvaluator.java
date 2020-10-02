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

import org.apache.log4j.Logger;

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
	private static final Logger LOG = Logger.getLogger(GallagherSiteTransactionRestrictionEvaluator.class);

	@Override
	public boolean evaluate(final GallagherSiteTransactionRestrictionModel arg0, final RestrictionData arg1)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

		boolean b2bUnitTransactional = true;

		if (currentCustomer instanceof B2BCustomerModel)
		{
			final B2BUnitModel b2bUnit = (B2BUnitModel) b2BUnitService.getParent((B2BCustomerModel) currentCustomer);
			LOG.info("Defalt B2B unit for user " + currentCustomer.getUid() + " is " + b2bUnit.getName());
			b2bUnitTransactional = Boolean.TRUE.equals(b2bUnit.getTransactional());
			LOG.info("Transactional value: " + " is " + b2bUnit.getTransactional() + " and check value:" + b2bUnitTransactional);
		}
		return (b2bUnitTransactional && siteConfigService.getBoolean(TRANSACTION_ENABLED, true));
	}

}
