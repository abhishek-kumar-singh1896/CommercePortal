/**
 *
 */
package com.enterprisewide.b2badvance.core.dto;

/**
 * DTO Handle Stock attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceStockCsvDTO extends B2BAdvanceCsvDTO
{
	private String articleNumber;

	private String erpArticleNumber;

	private String productName;

	private String warehouseCode;

	private int stockAvailable;

	private String inStockStatus;

	public String getArticleNumber()
	{
		return articleNumber;
	}

	public void setArticleNumber(final String articleNumber)
	{
		this.articleNumber = articleNumber;
	}

	public String getErpArticleNumber()
	{
		return erpArticleNumber;
	}

	public void setErpArticleNumber(final String erpArticleNumber)
	{
		this.erpArticleNumber = erpArticleNumber;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	public String getWarehouseCode()
	{
		return warehouseCode;
	}

	public void setWarehouseCode(final String warehouseCode)
	{
		this.warehouseCode = warehouseCode;
	}

	public int getStockAvailable()
	{
		return stockAvailable;
	}

	public void setStockAvailable(final int stockAvailable)
	{
		this.stockAvailable = stockAvailable;
	}

	public String getInStockStatus()
	{
		return inStockStatus;
	}

	public void setInStockStatus(final String inStockStatus)
	{
		this.inStockStatus = inStockStatus;
	}
}
