package com.gallagher.core.restrictions;



import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;

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

	private static final String TRANSACTION_ENABLED = "transaction.enabled";

	@Override
	public boolean evaluate(final GallagherSiteTransactionRestrictionModel arg0, final RestrictionData arg1)
	{
		return siteConfigService.getBoolean(TRANSACTION_ENABLED, true);
	}

}
