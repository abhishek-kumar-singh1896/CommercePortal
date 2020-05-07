/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product error
 *
 * @author Vikram Bishnoi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisterProductErrorResponse
{
	@JsonProperty("error")
	private GallagherRegisterProductErrorDTO error;

	public GallagherRegisterProductErrorDTO getError()
	{
		return error;
	}

	public void setError(final GallagherRegisterProductErrorDTO error)
	{
		this.error = error;
	}
}