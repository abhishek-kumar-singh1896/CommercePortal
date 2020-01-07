package com.enterprisewide.b2badvance.core.dto;

import java.util.Date;


/**
 * DTO Handle variant product attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceVariantProductCsvDTO extends B2BAdvanceCsvDTO
{
	private String articleNumber;
	private String erpArticleNumber;
	private String productName;
	private String productSummary;
	private Date onlineFrom;
	private Date onlineTo;
	private String salesUnit;
	private String variantValueCategories;
	private String baseProductArticleNumber;
	private String action;
	private String catalogId;

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

	public String getProductSummary()
	{
		return productSummary;
	}

	public void setProductSummary(final String productSummary)
	{
		this.productSummary = productSummary;
	}

	public Date getOnlineFrom()
	{
		return onlineFrom;
	}

	public void setOnlineFrom(final Date onlineFrom)
	{
		this.onlineFrom = onlineFrom;
	}

	public Date getOnlineTo()
	{
		return onlineTo;
	}

	public void setOnlineTo(final Date onlineTo)
	{
		this.onlineTo = onlineTo;
	}

	public String getSalesUnit()
	{
		return salesUnit;
	}

	public void setSalesUnit(final String salesUnit)
	{
		this.salesUnit = salesUnit;
	}

	public String getVariantValueCategories()
	{
		return variantValueCategories;
	}

	public void setVariantValueCategories(final String variantValueCategories)
	{
		this.variantValueCategories = variantValueCategories;
	}

	public String getBaseProductArticleNumber()
	{
		return baseProductArticleNumber;
	}

	public void setBaseProductArticleNumber(final String baseProductArticleNumber)
	{
		this.baseProductArticleNumber = baseProductArticleNumber;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(final String action)
	{
		this.action = action;
	}

	public String getCatalogId()
	{
		return catalogId;
	}

	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

}
