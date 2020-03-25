/**
 *
 */
package com.gallagher.core.dtos;

import java.util.List;


/**
 * DTO to store the list of registered products
 *
 * obtained from C4C
 *
 * @author gauravkamboj
 */
public class GallagherRegisteredProductsDto
{
	private List<GallagherRegisteredProductDto> registeredProducts;

	/**
	 * @return the registeredProducts
	 */
	public List<GallagherRegisteredProductDto> getRegisteredProducts()
	{
		return registeredProducts;
	}

	/**
	 * @param registeredProducts
	 *
	 */
	public void setRegisteredProducts(final List<GallagherRegisteredProductDto> registeredProducts)
	{
		this.registeredProducts = registeredProducts;
	}


}
