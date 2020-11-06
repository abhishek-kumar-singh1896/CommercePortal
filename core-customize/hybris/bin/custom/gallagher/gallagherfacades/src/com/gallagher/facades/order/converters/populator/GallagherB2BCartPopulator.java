/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;

import org.apache.commons.lang.StringUtils;


/**
 * @author ankituniyal
 *
 */
public class GallagherB2BCartPopulator implements Populator<CartModel, CartData>
{

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		target.setSalesArea(StringUtils.isNotBlank(source.getSalesArea()) ? source.getSalesArea() : StringUtils.EMPTY);
	}

}
