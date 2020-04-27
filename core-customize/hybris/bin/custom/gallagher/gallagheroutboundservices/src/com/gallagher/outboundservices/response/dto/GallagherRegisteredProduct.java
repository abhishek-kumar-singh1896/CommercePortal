/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisteredProduct
{
	@JsonProperty("SerialID")
	private String serialID;

	@JsonProperty("ProductID")
	private String productID;

	@JsonProperty("ReferenceDate")
	private String referenceDate;

	@JsonProperty("CreationDateTime")
	private String creationDateTime;

	@JsonProperty("RegisteredProductAttachmentFolder")
	private GallagherRegisteredProductAttachmentFolder registeredProductAttachmentFolder;


	public String getSerialID()
	{
		return serialID;
	}

	public void setSerialID(final String serialID)
	{
		this.serialID = serialID;
	}

	public String getProductID()
	{
		return productID;
	}

	public void setProductID(final String productID)
	{
		this.productID = productID;
	}

	public String getReferenceDate()
	{
		return referenceDate;
	}

	public void setReferenceDate(final String referenceDate)
	{
		this.referenceDate = referenceDate;
	}

	public String getCreationDateTime()
	{
		return creationDateTime;
	}

	public void setCreationDateTime(final String creationDateTime)
	{
		this.creationDateTime = creationDateTime;
	}

	public GallagherRegisteredProductAttachmentFolder getRegisteredProductAttachmentFolder()
	{
		return registeredProductAttachmentFolder;
	}

	public void setRegisteredProductAttachmentFolder(
			final GallagherRegisteredProductAttachmentFolder registeredProductAttachmentFolder)
	{
		this.registeredProductAttachmentFolder = registeredProductAttachmentFolder;
	}

}
