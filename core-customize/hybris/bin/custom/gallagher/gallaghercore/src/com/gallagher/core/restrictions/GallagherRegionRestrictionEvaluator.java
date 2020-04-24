package com.gallagher.core.restrictions;



import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import javax.annotation.Resource;

import com.gallagher.core.model.GallagherRegionRestrictionModel;




/**
 * Evaluator to check whether the site is of current site region or not
 *
 * @author gauravkamboj
 */
public class GallagherRegionRestrictionEvaluator implements CMSRestrictionEvaluator<GallagherRegionRestrictionModel>
{
	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;


	@Override
	public boolean evaluate(final GallagherRegionRestrictionModel gallagherRegionRestrictionModel, final RestrictionData arg1)
	{
		boolean returnValue = false;
		if (cmsSiteService.getCurrentSite().getRegionCode().equals(gallagherRegionRestrictionModel.getRegion()))
		{
			if (gallagherRegionRestrictionModel.getLanguage() == null
					|| commerceCommonI18NService.getCurrentLanguage().equals(gallagherRegionRestrictionModel.getLanguage()))
			{
				returnValue = true;
			}
		}
		return returnValue;
	}

}
