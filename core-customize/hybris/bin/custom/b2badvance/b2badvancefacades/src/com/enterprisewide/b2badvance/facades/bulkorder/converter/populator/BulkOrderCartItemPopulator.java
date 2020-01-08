/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder.converter.populator;

import com.enterprisewide.b2badvance.bulkorder.data.BulkOrderCartItemData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.util.Assert;


/**
 *
 */
public class BulkOrderCartItemPopulator implements Populator<CartModificationData, BulkOrderCartItemData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CartModificationData source, final BulkOrderCartItemData target) throws ConversionException
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setProduct(source.getEntry().getProduct());
		target.setQuantity(Long.valueOf(source.getQuantity()));

	}

}
