/**
 *
 */
package com.gallagher.facades;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.gallagher.facades.product.data.RegisteredProductData;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;


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

	/**
	 * Send Registered Product Information to c4c
	 *
	 * @param request
	 * @return status
	 */
	HttpStatus registerProduct(RegisterProductRequest request);
}
