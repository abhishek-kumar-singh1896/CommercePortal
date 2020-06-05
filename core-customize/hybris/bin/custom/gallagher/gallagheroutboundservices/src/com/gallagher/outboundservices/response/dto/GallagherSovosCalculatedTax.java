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
	private Double txAmt;

	@JsonProperty("txRate")
	private Double txRate;

	@JsonProperty("txJurUIDJurTp")
	private String txJurUIDJurTp;

	@JsonProperty("txJurUIDTxwTJId")
	private String txJurUIDTxwTJId;

	public void setTxAmt(final Double txAmt)
	{
		this.txAmt = txAmt;
	}

	public Double getTxAmt()
	{
		return this.txAmt;
	}

	public void setTxRate(final Double txRate)
	{
		this.txRate = txRate;
	}

	public Double getTxRate()
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

	public String getTxJurUIDTxwTJId()
	{
		return txJurUIDTxwTJId;
	}

	public void setTxJurUIDTxwTJId(final String txJurUIDTxwTJId)
	{
		this.txJurUIDTxwTJId = txJurUIDTxwTJId;
	}
}
