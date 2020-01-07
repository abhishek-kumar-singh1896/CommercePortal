/**
 *
 */
package com.enterprisewide.b2badvance.core.dto;

import java.util.Date;


/**
 * DTO Handle PDT attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePDTCsvDTO extends B2BAdvanceCsvDTO
{

	private String articleNumber;

	private String erpArticleNumber;

	private String productName;

	private Date startDate;

	private Date endDate;

	private String currency;

	public void setArticleNumber(final String articleNumber)
	{
		this.articleNumber = articleNumber;
	}

	public String getArticleNumber()
	{
		return articleNumber;
	}

	public void setErpArticleNumber(final String erpArticleNumber)
	{
		this.erpArticleNumber = erpArticleNumber;
	}

	public String getErpArticleNumber()
	{
		return erpArticleNumber;
	}

	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setCurrency(final String currency)
	{
		this.currency = currency;
	}

	public String getCurrency()
	{
		return currency;
	}
}
