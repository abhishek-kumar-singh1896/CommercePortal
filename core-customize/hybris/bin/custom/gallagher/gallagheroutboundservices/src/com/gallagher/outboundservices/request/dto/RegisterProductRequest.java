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

	@JsonProperty("ReferenceDate")
	private String referenceDate;

	@JsonProperty("AddressLine1")
	private String addressLine1;

	@JsonProperty("AddressLine2")
	private String addressLine2;

	@JsonProperty("District")
	private String district;

	@JsonProperty("City")
	private String city;

	@JsonProperty("PostalCode")
	private String postalCode;

	@JsonProperty("Country")
	private String country;


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

	public String getReferenceDate()
	{
		return referenceDate;
	}

	public void setReferenceDate(final String referenceDate)
	{
		this.referenceDate = referenceDate;
	}

	public String getAddressLine1()
	{
		return addressLine1;
	}

	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2()
	{
		return addressLine2;
	}

	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(final String district)
	{
		this.district = district;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(final String country)
	{
		this.country = country;
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