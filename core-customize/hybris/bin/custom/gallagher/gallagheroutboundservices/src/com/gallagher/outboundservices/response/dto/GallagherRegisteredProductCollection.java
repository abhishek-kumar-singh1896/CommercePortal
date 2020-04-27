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
 * Representation of Registered Product Collection
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisteredProductCollection
{
	@JsonProperty("RegisteredProduct")
	@JsonDeserialize(contentAs = GallagherRegisteredProduct.class, as = ArrayList.class)
	private List<GallagherRegisteredProduct> registeredProduct;

	public void setRegisteredProduct(final List<GallagherRegisteredProduct> registeredProduct)
	{
		this.registeredProduct = registeredProduct;
	}

	public List<GallagherRegisteredProduct> getRegisteredProduct()
	{
		return this.registeredProduct;
	}
}
