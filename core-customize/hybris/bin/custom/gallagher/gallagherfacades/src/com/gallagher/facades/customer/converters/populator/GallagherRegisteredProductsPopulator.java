/**
 *
 */
package com.gallagher.facades.customer.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gallagher.core.dtos.GallagherRegisteredProductDto;
import com.gallagher.core.dtos.GallagherRegisteredProductsDto;
import com.gallagher.facades.product.converters.populator.GallagherProductPrimaryImagePopulator;
import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.facades.product.data.RegisteredProductsData;


/**
 *
 */
public class GallagherRegisteredProductsPopulator implements Populator<GallagherRegisteredProductsDto, RegisteredProductsData>
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
	public void populate(final GallagherRegisteredProductsDto source, final RegisteredProductsData target)
			throws ConversionException
	{
		final List<RegisteredProductData> registeredProductsDataList = new ArrayList<RegisteredProductData>();
		for (final GallagherRegisteredProductDto gallagherRegisteredProductDto : source.getRegisteredProducts())
		{
			final RegisteredProductData registeredProductData = new RegisteredProductData();
			registeredProductData.setAttachment(gallagherRegisteredProductDto.getAttachment());
			registeredProductData.setAttachmentUrl(gallagherRegisteredProductDto.getAttachmentUrl());
			registeredProductData.setCode(gallagherRegisteredProductDto.getCode());
			registeredProductData.setName(gallagherRegisteredProductDto.getName());
			registeredProductData.setPurchaseDate(gallagherRegisteredProductDto.getPurchaseDate());
			registeredProductData.setRegistrationDate(gallagherRegisteredProductDto.getRegistrationDate());
			registeredProductData.setImage(null);
			registeredProductsDataList.add(registeredProductData);
		}
		target.setResgisteredProducts(registeredProductsDataList);

	}


}
