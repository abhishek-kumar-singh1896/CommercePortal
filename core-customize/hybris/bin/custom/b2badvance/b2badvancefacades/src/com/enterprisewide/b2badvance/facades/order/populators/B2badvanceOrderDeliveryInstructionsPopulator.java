/**
 *
 */
package com.enterprisewide.b2badvance.facades.order.populators;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author Enterprise Wide
 *
 */
public class B2badvanceOrderDeliveryInstructionsPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{
	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException
	{
		target.setDeliveryInstructions(source.getDeliveryInstructions());

	}
}
