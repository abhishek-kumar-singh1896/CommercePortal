/**
 *
 */
package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author Enterprise Wide
 *
 */
public class TemplateOrderPopulator extends CartPopulator
{
	/**
	 *
	 */
	@Override
	public void populate(final CartModel source, final CartData target) throws ConversionException
	{
		super.populate(source, target);
	}

	protected void addEntries(final CartModel source, final CartData target)
	{
		target.setEntries(getOrderEntryConverter().convertAll(source.getEntries()));
	}
}
