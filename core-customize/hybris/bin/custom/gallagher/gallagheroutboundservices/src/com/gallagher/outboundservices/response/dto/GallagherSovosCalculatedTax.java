/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Sovos Calculated Tax for a Item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherSovosCalculatedTax
{

	@JsonProperty("txAmt")
	private String txAmt;

	@JsonProperty("txRate")
	private String txRate;

	@JsonProperty("txJurUIDJurTp")
	private String txJurUIDJurTp;

	public void setTxAmt(final String txAmt)
	{
		this.txAmt = txAmt;
	}

	public String getTxAmt()
	{
		return this.txAmt;
	}

	public void setTxRate(final String txRate)
	{
		this.txRate = txRate;
	}

	public String getTxRate()
	{
		return this.txRate;
	}

	public void setTxJurUIDJurTp(final String txJurUIDJurTp)
	{
		this.txJurUIDJurTp = txJurUIDJurTp;
	}

	public String getTxJurUIDJurTp()
	{
		return this.txJurUIDJurTp;
	}
}
