/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 */
public class GallagherServiceRequestText
{
	@JsonProperty("ServiceRequestID")
	private String serviceRequestID;

	@JsonProperty("TypeCode")
	private String typeCode;

	@JsonProperty("TypeCodeText")
	private String typeCodeText;

	@JsonProperty("Text")
	private String text;

	@JsonProperty("FormattedText")
	private String formattedText;

	@JsonProperty("UpdatedOn")
	private Date updatedOn;

	@JsonProperty("CreatedBy")
	private String createdBy;

	@JsonProperty("LastUpdatedBy")
	private String lastUpdatedBy;

	@JsonProperty("CreatedOn")
	private Date createdOn;

	@JsonProperty("TextCreatedOn")
	private Date textCreatedOn;

	@JsonProperty("AuthorID")
	private Date authorID;

	@JsonProperty("AuthorName")
	private String authorName;

	/**
	 * @return the serviceRequestID
	 */
	public String getServiceRequestID()
	{
		return serviceRequestID;
	}

	/**
	 * @param serviceRequestID
	 *           the serviceRequestID to set
	 */
	public void setServiceRequestID(final String serviceRequestID)
	{
		this.serviceRequestID = serviceRequestID;
	}

	/**
	 * @return the typeCode
	 */
	public String getTypeCode()
	{
		return typeCode;
	}

	/**
	 * @param typeCode
	 *           the typeCode to set
	 */
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	/**
	 * @return the typeCodeText
	 */
	public String getTypeCodeText()
	{
		return typeCodeText;
	}

	/**
	 * @param typeCodeText
	 *           the typeCodeText to set
	 */
	public void setTypeCodeText(final String typeCodeText)
	{
		this.typeCodeText = typeCodeText;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *           the text to set
	 */
	public void setText(final String text)
	{
		this.text = text;
	}

	/**
	 * @return the formattedText
	 */
	public String getFormattedText()
	{
		return formattedText;
	}

	/**
	 * @param formattedText
	 *           the formattedText to set
	 */
	public void setFormattedText(final String formattedText)
	{
		this.formattedText = formattedText;
	}

	/**
	 * @return the updatedOn
	 */
	public Date getUpdatedOn()
	{
		return updatedOn;
	}

	/**
	 * @param updatedOn
	 *           the updatedOn to set
	 */
	public void setUpdatedOn(final Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * @param createdBy
	 *           the createdBy to set
	 */
	public void setCreatedBy(final String createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy()
	{
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy
	 *           the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(final String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn()
	{
		return createdOn;
	}

	/**
	 * @param createdOn
	 *           the createdOn to set
	 */
	public void setCreatedOn(final Date createdOn)
	{
		this.createdOn = createdOn;
	}

	/**
	 * @return the textCreatedOn
	 */
	public Date getTextCreatedOn()
	{
		return textCreatedOn;
	}

	/**
	 * @param textCreatedOn
	 *           the textCreatedOn to set
	 */
	public void setTextCreatedOn(final Date textCreatedOn)
	{
		this.textCreatedOn = textCreatedOn;
	}

	/**
	 * @return the authorID
	 */
	public Date getAuthorID()
	{
		return authorID;
	}

	/**
	 * @param authorID
	 *           the authorID to set
	 */
	public void setAuthorID(final Date authorID)
	{
		this.authorID = authorID;
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName()
	{
		return authorName;
	}

	/**
	 * @param authorName
	 *           the authorName to set
	 */
	public void setAuthorName(final String authorName)
	{
		this.authorName = authorName;
	}

}
