/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherServiceRequestPostRequest
{
	@JsonProperty("Name")
	private String name;

	@JsonProperty("ServiceSupportTeamPartyID")
	private String serviceSupportTeamPartyID;

	@JsonProperty("BuyerPartyID")
	private String buyerPartyID;

	@JsonProperty("BuyerMainContactPartyID")
	private String buyerMainContactPartyID;

	@JsonProperty("ZSapContactID")
	private String zSapContactID;

	@JsonProperty("ServiceRequestTextCollection")
	private List<GallagherServiceRequestText> serviceRequestTextCollection;

	@JsonProperty("ServiceRequestAttachmentFolder")
	private List<GallagherServiceRequestAttachmentFolder> serviceRequestAttachmentFolder;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the serviceSupportTeamPartyID
	 */
	public String getServiceSupportTeamPartyID()
	{
		return serviceSupportTeamPartyID;
	}

	/**
	 * @param serviceSupportTeamPartyID
	 *           the serviceSupportTeamPartyID to set
	 */
	public void setServiceSupportTeamPartyID(final String serviceSupportTeamPartyID)
	{
		this.serviceSupportTeamPartyID = serviceSupportTeamPartyID;
	}

	/**
	 * @return the buyerPartyID
	 */
	public String getBuyerPartyID()
	{
		return buyerPartyID;
	}

	/**
	 * @param buyerPartyID
	 *           the buyerPartyID to set
	 */
	public void setBuyerPartyID(final String buyerPartyID)
	{
		this.buyerPartyID = buyerPartyID;
	}

	/**
	 * @return the buyerMainContactPartyID
	 */
	public String getBuyerMainContactPartyID()
	{
		return buyerMainContactPartyID;
	}

	/**
	 * @param buyerMainContactPartyID
	 *           the buyerMainContactPartyID to set
	 */
	public void setBuyerMainContactPartyID(final String buyerMainContactPartyID)
	{
		this.buyerMainContactPartyID = buyerMainContactPartyID;
	}

	/**
	 * @return the zSapContactID
	 */
	public String getzSapContactID()
	{
		return zSapContactID;
	}

	/**
	 * @param zSapContactID
	 *           the zSapContactID to set
	 */
	public void setzSapContactID(final String zSapContactID)
	{
		this.zSapContactID = zSapContactID;
	}

	/**
	 * @return the serviceRequestTextCollection
	 */
	public List<GallagherServiceRequestText> getServiceRequestTextCollection()
	{
		return serviceRequestTextCollection;
	}

	/**
	 * @param serviceRequestTextCollection
	 *           the serviceRequestTextCollection to set
	 */
	public void setServiceRequestTextCollection(final List<GallagherServiceRequestText> serviceRequestTextCollection)
	{
		this.serviceRequestTextCollection = serviceRequestTextCollection;
	}

	/**
	 * @return the serviceRequestAttachmentFolder
	 */
	public List<GallagherServiceRequestAttachmentFolder> getServiceRequestAttachmentFolder()
	{
		return serviceRequestAttachmentFolder;
	}

	/**
	 * @param serviceRequestAttachmentFolder
	 *           the serviceRequestAttachmentFolder to set
	 */
	public void setServiceRequestAttachmentFolder(
			final List<GallagherServiceRequestAttachmentFolder> serviceRequestAttachmentFolder)
	{
		this.serviceRequestAttachmentFolder = serviceRequestAttachmentFolder;
	}
}
