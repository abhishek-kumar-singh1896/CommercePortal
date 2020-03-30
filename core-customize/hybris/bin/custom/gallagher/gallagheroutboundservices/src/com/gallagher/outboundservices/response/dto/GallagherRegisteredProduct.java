/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Representation of Registered Product
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisteredProduct
{
	private String name;
	private String code;
	private String attachment;
	private String attachmentUrl;
	private Date registrationDate;
	private Date purchaseDate;
	private byte[] image;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getAttachment()
	{
		return attachment;
	}

	public void setAttachment(final String attachment)
	{
		this.attachment = attachment;
	}

	public String getAttachmentUrl()
	{
		return attachmentUrl;
	}

	public void setAttachmentUrl(final String attachmentUrl)
	{
		this.attachmentUrl = attachmentUrl;
	}

	public Date getRegistrationDate()
	{
		return registrationDate;
	}

	public void setRegistrationDate(final Date registrationDate)
	{
		this.registrationDate = registrationDate;
	}

	public Date getPurchaseDate()
	{
		return purchaseDate;
	}

	public void setPurchaseDate(final Date purchaseDate)
	{
		this.purchaseDate = purchaseDate;
	}

	public byte[] getImage()
	{
		return image;
	}

	public void setImage(final byte[] image)
	{
		this.image = image;
	}
}