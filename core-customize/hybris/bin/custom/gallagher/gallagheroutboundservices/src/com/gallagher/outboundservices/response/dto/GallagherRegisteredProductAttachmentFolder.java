/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Attachment Folder
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisteredProductAttachmentFolder
{
	@JsonProperty("RegisteredProductAttachmentFolder")
	private GallagherRegisteredProductAttachmentFolderItem registeredProductAttachmentFolderItem;

	public GallagherRegisteredProductAttachmentFolderItem getRegisteredProductAttachmentFolderItem()
	{
		return registeredProductAttachmentFolderItem;
	}

	public void setRegisteredProductAttachmentFolderItem(
			final GallagherRegisteredProductAttachmentFolderItem registeredProductAttachmentFolderItem)
	{
		this.registeredProductAttachmentFolderItem = registeredProductAttachmentFolderItem;
	}
}

