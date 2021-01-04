/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherServiceRequestTextCollectionResponse
{
	@JsonProperty("d")
	private GallagherServiceRequestTextCollection serviceRequestTextCollection;

	public void setServiceRequestTextCollection(final GallagherServiceRequestTextCollection serviceRequestTextCollection)
	{
		this.serviceRequestTextCollection = serviceRequestTextCollection;
	}

	public GallagherServiceRequestTextCollection getServiceRequestTextCollection()
	{
		return this.serviceRequestTextCollection;
	}
}

