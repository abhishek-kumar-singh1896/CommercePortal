/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import com.gallagher.core.dtos.GallagherRegisteredProductDto;
import com.gallagher.facades.product.converters.populator.GallagherProductPrimaryImagePopulator;
import com.gallagher.facades.product.data.RegisteredProductData;


/**
 *
 */
public class GallagherRegisteredProductsPopulator implements Populator<GallagherRegisteredProductDto, RegisteredProductData>
{

	@Resource
	ProductService productService;

	@Resource(name = "productPrimaryImagePopulator")
	GallagherProductPrimaryImagePopulator gallagherProductPrimaryImagePopulator;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final GallagherRegisteredProductDto source, final RegisteredProductData target)
			throws ConversionException
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
