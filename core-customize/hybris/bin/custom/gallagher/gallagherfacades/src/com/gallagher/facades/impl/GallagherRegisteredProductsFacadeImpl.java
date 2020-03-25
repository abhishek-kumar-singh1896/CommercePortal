/**
 *
 */
package com.gallagher.facades.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import com.gallagher.core.dtos.GallagherRegisteredProductsDto;
import com.gallagher.core.services.GallagherCustomerService;
import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.facades.product.data.RegisteredProductsData;


/**
 * {@inheritDoc}
 */
public class GallagherRegisteredProductsFacadeImpl implements GallagherRegisteredProductsFacade
{
	private Converter<GallagherRegisteredProductsDto, RegisteredProductsData> registeredProductsConverter;
	private GallagherCustomerService gallagherCustomerService;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RegisteredProductData> getRegisteredProducts()
	{
		return getRegisteredProductsConverter().convert(getGallagherCustomerService().getRegisteredProductsFromC4C())
				.getResgisteredProducts();
	}

	protected Converter<GallagherRegisteredProductsDto, RegisteredProductsData> getRegisteredProductsConverter()
	{
		return registeredProductsConverter;
	}

	public void setRegisteredProductsConverter(
			final Converter<GallagherRegisteredProductsDto, RegisteredProductsData> registeredProductsConverter)
	{
		this.registeredProductsConverter = registeredProductsConverter;
	}

	protected GallagherCustomerService getGallagherCustomerService()
	{
		return gallagherCustomerService;
	}

	public void setGallagherCustomerService(final GallagherCustomerService gallagherCustomerService)
	{
		this.gallagherCustomerService = gallagherCustomerService;
	}

}
