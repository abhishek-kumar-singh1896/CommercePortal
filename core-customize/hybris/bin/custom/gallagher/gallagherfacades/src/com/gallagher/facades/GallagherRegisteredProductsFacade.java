/**
 *
 */
package com.gallagher.facades;

import java.util.List;

import com.gallagher.facades.product.data.RegisteredProductData;


/**
 * GallagherRegisteredProductsFacade for functionality
 *
 * related to products registered by user
 *
 * @author gauravkamboj
 */
public interface GallagherRegisteredProductsFacade
{
	/**
	 * Get list of registered products registered by logged in users
	 *
	 */
	List<RegisteredProductData> getRegisteredProducts();
}
