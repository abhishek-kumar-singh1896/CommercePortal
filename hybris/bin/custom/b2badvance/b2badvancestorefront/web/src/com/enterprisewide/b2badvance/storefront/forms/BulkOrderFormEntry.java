/**
 *
 */
package com.enterprisewide.b2badvance.storefront.forms;

/**
 * @author Enterprise Wide
 *
 */
public class BulkOrderFormEntry
{
	private String productCode;
	private String quantity;

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}

}
