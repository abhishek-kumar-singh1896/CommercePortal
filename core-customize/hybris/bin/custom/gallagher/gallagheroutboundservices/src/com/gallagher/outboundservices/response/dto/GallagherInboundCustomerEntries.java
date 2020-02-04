package com.gallagher.outboundservices.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of contact info list
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherInboundCustomerEntries
{
	@JsonProperty("results")
	private List<GallagherInboundCustomerEntry> customerEntries;

	public void setCustomerEntries(final List<GallagherInboundCustomerEntry> customerEntries)
	{
		this.customerEntries = customerEntries;
	}

	public List<GallagherInboundCustomerEntry> getCustomerEntries()
	{
		return this.customerEntries;
	}
}
