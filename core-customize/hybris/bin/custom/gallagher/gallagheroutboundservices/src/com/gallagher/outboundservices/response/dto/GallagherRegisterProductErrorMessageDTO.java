/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Error message
 *
 * @author Vikram Bishnoi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisterProductErrorMessageDTO
{
	@JsonProperty("lang")
	private String lang;

	@JsonProperty("value")
	private String value;

	public String getLang()
	{
		return lang;
	}

	public void setLang(final String lang)
	{
		this.lang = lang;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}
}