/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 */
public class GallagherServiceRequestText
{

	@JsonProperty("Text")
	private String text;

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *           the text to set
	 */
	public void setText(final String text)
	{
		this.text = text;
	}

}
