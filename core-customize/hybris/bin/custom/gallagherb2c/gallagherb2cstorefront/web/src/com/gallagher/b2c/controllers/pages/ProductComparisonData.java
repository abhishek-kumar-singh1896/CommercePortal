/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Map;
import java.util.TreeMap;


/**
 * Data class to hold product comparison values
 *
 * @author Shilpi Verma
 */
public class ProductComparisonData
{
	private ProductData productData;

	private Map<String, String> productAttrValueMap = new TreeMap<>();

	public void setProductAttrValueMap(final Map<String, String> productAttrValueMap)
	{
		this.productAttrValueMap = productAttrValueMap;
	}

	public ProductData getProductData()
	{
		return productData;
	}

	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}

	public Map<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}
}
