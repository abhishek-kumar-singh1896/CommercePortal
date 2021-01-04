/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Attachment Folder Information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherServiceRequestAttachmentFolder
{
	@JsonProperty("TypeCode")
	private String typeCode;

	@JsonProperty("Binary")
	private String binary;

	@JsonProperty("MimeType")
	private String mimeType;

	@JsonProperty("Name")
	private String name;

	public String getTypeCode()
	{
		return typeCode;
	}

	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	public String getBinary()
	{
		return binary;
	}

	public void setBinary(final String binary)
	{
		this.binary = binary;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(final String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}
}