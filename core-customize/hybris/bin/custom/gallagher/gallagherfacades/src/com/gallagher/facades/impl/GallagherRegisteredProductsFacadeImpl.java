/**
 *
 */
package com.gallagher.facades.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import com.gallagher.core.dtos.GallagherRegisteredProductDto;
import com.gallagher.core.services.GallagherCustomerService;
import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.facades.product.data.RegisteredProductData;


/**
 * {@inheritDoc}
 */
public class GallagherRegisteredProductsFacadeImpl implements GallagherRegisteredProductsFacade
{
	private Converter<GallagherRegisteredProductDto, RegisteredProductData> registeredProductsConverter;
	private GallagherCustomerService gallagherCustomerService;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RegisteredProductData> getRegisteredProducts()
	{
		return getRegisteredProductsConverter().convertAll(getGallagherCustomerService().getRegisteredProductsFromC4C());
	}

	protected Converter<GallagherRegisteredProductDto, RegisteredProductData> getRegisteredProductsConverter()
	{
		return registeredProductsConverter;
	}

	public void setRegisteredProductsConverter(
			final Converter<GallagherRegisteredProductDto, RegisteredProductData> registeredProductsConverter)
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
