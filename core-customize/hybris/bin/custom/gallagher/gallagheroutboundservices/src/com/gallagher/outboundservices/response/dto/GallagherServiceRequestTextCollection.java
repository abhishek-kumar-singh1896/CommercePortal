/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherServiceRequestTextCollection
{
	@JsonProperty("results")
	@JsonDeserialize(contentAs = GallagherServiceRequestText.class, as = ArrayList.class)
	private List<GallagherServiceRequestText> serviceRequestText;

	public void setServiceRequestText(final List<GallagherServiceRequestText> serviceRequestText)
	{
		this.serviceRequestText = serviceRequestText;
	}

	public List<GallagherServiceRequestText> getServiceRequestText()
	{
		return this.serviceRequestText;
	}
}

