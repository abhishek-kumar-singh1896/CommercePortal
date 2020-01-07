/**
 *
 */
package com.enterprisewide.b2badvance.facades.bulkorder.data;

import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * @author Enterprise Wide
 *
 */
public class BulkOrderProductData
{

	private ProductData productData;

	private Integer quantity;

	private Integer orderNumber;

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
	 * @return the quantity
	 */
	public Integer getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * @return the orderNumber
	 */
	public Integer getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	public void setOrderNumber(final Integer orderNumber)
	{
		this.orderNumber = orderNumber;
	}

}
