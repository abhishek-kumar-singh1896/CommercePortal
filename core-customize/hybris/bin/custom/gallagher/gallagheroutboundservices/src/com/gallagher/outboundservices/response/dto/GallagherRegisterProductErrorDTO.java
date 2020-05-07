/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Error
 *
 * @author Vikram Bishnoi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisterProductErrorDTO
{
	@JsonProperty("code")
	private String code;

	@JsonProperty("message")
	private GallagherRegisterProductErrorMessageDTO message;

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public GallagherRegisterProductErrorMessageDTO getMessage()
	{
		return message;
	}

	public void setMessage(final GallagherRegisterProductErrorMessageDTO message)
	{
		this.message = message;
	}
}