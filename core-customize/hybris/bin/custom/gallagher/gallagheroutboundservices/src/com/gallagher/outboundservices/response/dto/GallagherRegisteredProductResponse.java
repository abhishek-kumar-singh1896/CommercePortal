/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisteredProductResponse
{
	@JsonProperty("RegisteredProductCollection")
	private GallagherRegisteredProductCollection registeredProductCollection;

	public void setRegisteredProductCollection(final GallagherRegisteredProductCollection registeredProductCollection)
	{
		this.registeredProductCollection = registeredProductCollection;
	}

	public GallagherRegisteredProductCollection getRegisteredProductCollection()
	{
		return this.registeredProductCollection;
	}
}