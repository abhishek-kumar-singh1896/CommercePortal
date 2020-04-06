/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.HashMap;
import java.util.Map;


/**
 * @author shilpiverma
 *
 */
public class ProductComparisonData
{
	private ProductData productData;

	private Map<String, String> productAttrValueMap = new HashMap<String, String>();

	/**
	 * @return the productData
	 */
	public ProductData getProductData()
	{
		return productData;
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
	 * @param productData
	 *           the productData to set
	 */
	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}

	/**
	 * @return the productAttrValueMap
	 */
	public Map<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}


}
