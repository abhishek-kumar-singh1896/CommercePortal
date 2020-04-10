/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.TreeMap;


/**
 * @author shilpiverma
 *
 */
public class ProductComparisonData
{
	private ProductData productData;

	private TreeMap<String, String> productAttrValueMap = new TreeMap<>();

	/**
	 * @param productAttrValueMap
	 *           the productAttrValueMap to set
	 */
	public void setProductAttrValueMap(final TreeMap<String, String> productAttrValueMap)
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

	/**
	 * @return the productAttrValueMap
	 */
	public TreeMap<String, String> getProductAttrValueMap()
	{
		return productAttrValueMap;
	}
}
