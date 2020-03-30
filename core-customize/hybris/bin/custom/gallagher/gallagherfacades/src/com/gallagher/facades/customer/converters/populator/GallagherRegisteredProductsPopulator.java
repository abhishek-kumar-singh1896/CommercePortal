/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;


/**
 *
 */
public class GallagherRegisteredProductsPopulator implements Populator<GallagherRegisteredProduct, RegisteredProductData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final GallagherRegisteredProduct source, final RegisteredProductData target) throws ConversionException
	{
		target.setAttachment(source.getAttachment());
		target.setAttachmentUrl(source.getAttachmentUrl());
		target.setCode(source.getCode());
		target.setName(source.getName());
		target.setPurchaseDate(source.getPurchaseDate());
		target.setRegistrationDate(source.getRegistrationDate());
		target.setImage(null);

	}


}
