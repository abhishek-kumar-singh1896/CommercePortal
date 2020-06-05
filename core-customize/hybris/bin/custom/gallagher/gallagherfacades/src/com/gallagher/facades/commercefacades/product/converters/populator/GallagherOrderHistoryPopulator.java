/**
 *
 */
package com.gallagher.facades.commercefacades.product.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.i18n.FormatFactory;

import java.text.DateFormat;
import java.text.ParseException;


/**
 * @author shilpiverma
 *
 */
public class GallagherOrderHistoryPopulator extends OrderHistoryPopulator
{
	private FormatFactory formatFactory;

	public FormatFactory getFormatFactory()
	{
		return formatFactory;
	}

	public void setFormatFactory(final FormatFactory formatFactory)
	{
		this.formatFactory = formatFactory;
	}

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);
		final DateFormat dateFormat = getFormatFactory().createDateTimeFormat(DateFormat.MEDIUM, DateFormat.DEFAULT);
		//		final Locale locale = getI18nService().getCurrentLocale();
		//		final SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", locale);
		try
		{
			target.setPlaced(dateFormat.parse(dateFormat.format(source.getDate())));
		}
		catch (final ParseException e)
		{
			// XXX Auto-generated catch block
			e.printStackTrace();
		}
	}
}
