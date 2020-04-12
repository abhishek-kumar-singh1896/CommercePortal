/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Map;
import java.util.TreeMap;


/**
 * @author shilpiverma
 *
 */
public class ProductComparisonData
{
	private ProductData productData;

	private Map<String, String> productAttrValueMap = new TreeMap<>();

	/**
	 * @return the productAttrValueMap
	 */
	public Map<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}

	/**
	 * @param productAttrValueMap
	 *           the productAttrValueMap to set
	 */
	public void setProductAttrValueMap(final Map<String, String> productAttrValueMap)
	{
		this.productAttrValueMap = productAttrValueMap;
	}

	/**
	 * @return the productData
	 */
	public ProductData getProductData()
	{
		return productData;
	}

	/**
	 * @param productData
	 *           the productData to set
	 */
	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}

}
