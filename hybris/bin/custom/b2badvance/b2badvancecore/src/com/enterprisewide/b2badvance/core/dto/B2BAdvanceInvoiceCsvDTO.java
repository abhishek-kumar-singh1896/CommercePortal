/**
 *
 */
package com.enterprisewide.b2badvance.core.dto;

import java.util.Date;


/**
 * DTO Handle invoice attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceInvoiceCsvDTO extends B2BAdvanceCsvDTO
{
	private String invoiceNumber;
	private String hybrisOrderNumber;
	private String userId;
	private String erpOrderNumber;
	private Date dueDate;
	private Date datePaid;
	private Double totalAmount;
	private String status;

	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	public void setInvoiceNumber(final String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	public String getHybrisOrderNumber()
	{
		return hybrisOrderNumber;
	}

	public void setHybrisOrderNumber(final String hybrisOrderNumber)
	{
		this.hybrisOrderNumber = hybrisOrderNumber;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	public String getErpOrderNumber()
	{
		return erpOrderNumber;
	}

	public void setErpOrderNumber(final String erpOrderNumber)
	{
		this.erpOrderNumber = erpOrderNumber;
	}

	public Date getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(final Date dueDate)
	{
		this.dueDate = dueDate;
	}

	public Date getDatePaid()
	{
		return datePaid;
	}

	public void setDatePaid(final Date datePaid)
	{
		this.datePaid = datePaid;
	}

	public Double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(final Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getPdfUrl()
	{
		return pdfUrl;
	}

	public void setPdfUrl(final String pdfUrl)
	{
		this.pdfUrl = pdfUrl;
	}

	private String pdfUrl;
}
