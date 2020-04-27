/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallgherRegisteredProductRequest
{
	@JsonProperty("ProductQuery")
	private GallgherRegisteredProductQuery productQuery;

	public void setProductQuery(final GallgherRegisteredProductQuery productQuery)
	{
		this.productQuery = productQuery;
	}

	public GallgherRegisteredProductQuery getProductQuery()
	{
		return this.productQuery;
	}
}
