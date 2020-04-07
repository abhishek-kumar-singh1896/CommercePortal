package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Register Product Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisterProductResponse
{
	@JsonProperty("d")
	private GallagherRegisterProductInfo productInfoInfo;

	public GallagherRegisterProductInfo getProductInfoInfo()
	{
		return productInfoInfo;
	}

	public void setProductInfoInfo(final GallagherRegisterProductInfo productInfoInfo)
	{
		this.productInfoInfo = productInfoInfo;
	}
}
