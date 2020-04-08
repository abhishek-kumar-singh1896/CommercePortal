/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Registered Product Party (Customer) Information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisteredProductPartyInformation
{
	@JsonProperty("PartyID")
	private String partyID;

	@JsonProperty("RoleCode")
	private String roleCode;

	public String getPartyID()
	{
		return partyID;
	}

	public void setPartyID(final String partyID)
	{
		this.partyID = partyID;
	}

	public String getRoleCode()
	{
		return roleCode;
	}

	public void setRoleCode(final String roleCode)
	{
		this.roleCode = roleCode;
	}
}