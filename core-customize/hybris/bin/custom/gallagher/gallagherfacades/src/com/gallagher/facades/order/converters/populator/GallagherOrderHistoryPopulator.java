/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import com.gallagher.core.util.GallagherSiteUtil;


/**
 * @author shilpiverma
 *
 */
public class GallagherOrderHistoryPopulator extends OrderHistoryPopulator
{

	private I18NService i18NService;

	public I18NService getI18NService()
	{
		return i18NService;
	}

	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);
		target.setFormattedOrderDate(GallagherSiteUtil.getFormattedDateWithTimeZoneForDate(source.getDate(), "MMM dd, yyyy hh:mm a",
				i18NService.getCurrentTimeZone() == null ? null : i18NService.getCurrentTimeZone().getID()));

	}
}

