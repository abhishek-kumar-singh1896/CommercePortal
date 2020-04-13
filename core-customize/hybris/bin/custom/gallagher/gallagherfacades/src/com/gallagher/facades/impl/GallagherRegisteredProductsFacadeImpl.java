/**
 *
 */
package com.gallagher.facades.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;


/**
 * {@inheritDoc}
 */
public class GallagherRegisteredProductsFacadeImpl implements GallagherRegisteredProductsFacade
{
	private Converter<GallagherRegisteredProduct, RegisteredProductData> registeredProductsConverter;

	@Resource(name = "gallagherC4COutboundServiceFacade")
	private GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade;

	@Resource(name = "userService")
	private UserService userService;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RegisteredProductData> getRegisteredProducts()
	{
		return getRegisteredProductsConverter()
				.convertAll(gallagherC4COutboundServiceFacade.getRegisteredProductFromC4C(userService.getCurrentUser().getUid()));
	}

	protected Converter<GallagherRegisteredProduct, RegisteredProductData> getRegisteredProductsConverter()
	{
		return registeredProductsConverter;
	}

	public void setRegisteredProductsConverter(
			final Converter<GallagherRegisteredProduct, RegisteredProductData> registeredProductsConverter)
	{
		this.registeredProductsConverter = registeredProductsConverter;
	}

	@Override
	public HttpStatus registerProduct(final RegisterProductRequest request)
	{
		return gallagherC4COutboundServiceFacade.registerProduct(request);
	}

}
