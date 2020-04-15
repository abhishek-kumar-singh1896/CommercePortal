package com.gallagher.core.restrictions;



import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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

	private static final Logger LOGGER = Logger.getLogger(GallagherRegionRestrictionEvaluator.class);


	@Override
	public boolean evaluate(final GallagherRegionRestrictionModel gallagherRegionRestrictionModel, final RestrictionData arg1)
	{
		boolean returnValue = false;
		if (cmsSiteService.getCurrentSite().getRegionCode().getCode().equals(gallagherRegionRestrictionModel.getCountry()))
		{
			if (StringUtils.isEmpty(gallagherRegionRestrictionModel.getLanguage()))
			{
				returnValue = true;
			}
			else if (commerceCommonI18NService
					.getCurrentLanguage().getIsocode().contains(gallagherRegionRestrictionModel.getLanguage()))
			{
				returnValue = true;
			}
		}
		final String s = "********** " + gallagherRegionRestrictionModel.getCountry() + " and language "
				+ gallagherRegionRestrictionModel.getLanguage() + " returned " + String.valueOf(returnValue);
		LOGGER.info(s);

		return returnValue;
	}

}
