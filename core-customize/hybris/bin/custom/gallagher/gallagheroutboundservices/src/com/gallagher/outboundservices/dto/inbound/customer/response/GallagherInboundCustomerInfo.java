package com.gallagher.outboundservices.dto.inbound.customer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of d
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherInboundCustomerInfo
{
	@JsonProperty("d")
	private GallagherInboundCustomerEntries customerInfo;

	public void setCustomerInfo(final GallagherInboundCustomerEntries customerInfo)
	{
		this.customerInfo = customerInfo;
	}

	public GallagherInboundCustomerEntries getCustomerInfo()
	{
		return this.customerInfo;
	}
}
