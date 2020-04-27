/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Query
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallgherRegisteredProductQuery
{
	@JsonProperty("customerID")
	private String customerID;

	public void setCustomerID(final String customerID)
	{
		this.customerID = customerID;
	}

	public String getCustomerID()
	{
		return this.customerID;
	}
}
