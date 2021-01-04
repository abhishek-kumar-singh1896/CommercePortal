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
public class GallagherServiceRequestAttachmentFolder
{
	@JsonProperty("ServiceRequestAttachmentFolder")
	private GallagherServiceRequestAttachmentFolderItem serviceRequestAttachmentFolderItem;

	public GallagherServiceRequestAttachmentFolderItem getServiceRequestAttachmentFolderItem()
	{
		return serviceRequestAttachmentFolderItem;
	}

	public void setServiceRequestAttachmentFolderItem(
			final GallagherServiceRequestAttachmentFolderItem serviceRequestAttachmentFolderItem)
	{
		this.serviceRequestAttachmentFolderItem = serviceRequestAttachmentFolderItem;
	}
}

