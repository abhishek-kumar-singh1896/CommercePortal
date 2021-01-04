/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Attachment Folder Item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherServiceRequestAttachmentFolderItem
{
	@JsonProperty("Name")
	private String name;

	@JsonProperty("DocumentLink")
	private String documentLink;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDocumentLink()
	{
		return documentLink;
	}

	public void setDocumentLink(final String documentLink)
	{
		this.documentLink = documentLink;
	}

}
