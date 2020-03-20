/**
 *
 */
package com.gallagher.facades.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;

import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.facades.product.data.RegisteredProductsData;


/**
 *
 */
public class GallagherRegisteredProductsFacadeImpl implements GallagherRegisteredProductsFacade
{
	private Converter<String, RegisteredProductsData> registeredProductsConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gallagher.facades.GallagherRegisteredProductsFacade#getRegisteredProducts(java.lang.String)
	 */
	@Override
	public ArrayList<RegisteredProductData> getRegisteredProducts(final String uid)
	{
		return getRegisteredProductsConverter().convert(uid).getResgisteredProducts();
	}

	protected Converter<String, RegisteredProductsData> getRegisteredProductsConverter()
	{
		return registeredProductsConverter;
	}

	public void setRegisteredProductsConverter(final Converter<String, RegisteredProductsData> registeredProductsConverter)
	{
		this.registeredProductsConverter = registeredProductsConverter;
	}

}
