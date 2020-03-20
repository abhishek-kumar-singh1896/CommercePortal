/**
 *
 */
package com.gallagher.facades;

import java.util.ArrayList;

import com.gallagher.facades.product.data.RegisteredProductData;


/**
 *
 */
public interface GallagherRegisteredProductsFacade
{
	ArrayList<RegisteredProductData> getRegisteredProducts(String uid);
}
