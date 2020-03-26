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
	@JsonProperty("Status")
	private String status;

	@JsonProperty("ProductID")
	private String productID;

	@JsonProperty("SerialID")
	private String serialID;

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
}