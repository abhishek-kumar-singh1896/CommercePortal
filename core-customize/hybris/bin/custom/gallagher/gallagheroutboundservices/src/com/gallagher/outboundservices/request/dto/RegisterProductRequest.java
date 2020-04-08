/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Register Product Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterProductRequest
{
	@JsonProperty("Status")
	private String status;

	@JsonProperty("ProductID")
	private String productID;

	@JsonProperty("SerialID")
	private String serialID;

	@JsonProperty("RegisteredProductPartyInformation")
	private List<RegisteredProductPartyInformation> registeredProductPartyInformation;

	@JsonProperty("RegisteredProductAttachmentFolder")
	private List<RegisteredProductAttachmentFolder> registeredProductAttachmentFolder;

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getProductID()
	{
		return productID;
	}

	public void setProductID(final String productID)
	{
		this.productID = productID;
	}

	public String getSerialID()
	{
		return serialID;
	}

	public void setSerialID(final String serialID)
	{
		this.serialID = serialID;
	}

	public List<RegisteredProductPartyInformation> getRegisteredProductPartyInformation()
	{
		return registeredProductPartyInformation;
	}

	public void setRegisteredProductPartyInformation(
			final List<RegisteredProductPartyInformation> registeredProductPartyInformation)
	{
		this.registeredProductPartyInformation = registeredProductPartyInformation;
	}

	public List<RegisteredProductAttachmentFolder> getRegisteredProductAttachmentFolder()
	{
		return registeredProductAttachmentFolder;
	}

	public void setRegisteredProductAttachmentFolder(
			final List<RegisteredProductAttachmentFolder> registeredProductAttachmentFolder)
	{
		this.registeredProductAttachmentFolder = registeredProductAttachmentFolder;
	}
}