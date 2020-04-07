package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation Register Product Info
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherRegisterProductInfo
{
	@JsonProperty("ObjectID")
	private String objectID;

	@JsonProperty("ID")
	private String id;

	@JsonProperty("SerialID")
	private String serialID;

	@JsonProperty("ProductID")
	private String productID;

	public String getObjectID()
	{
		return objectID;
	}

	public void setObjectID(final String objectID)
	{
		this.objectID = objectID;
	}

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getSerialID()
	{
		return serialID;
	}

	public void setSerialID(final String serialID)
	{
		this.serialID = serialID;
	}

	public String getProductID()
	{
		return productID;
	}

	public void setProductID(final String productID)
	{
		this.productID = productID;
	}
}
