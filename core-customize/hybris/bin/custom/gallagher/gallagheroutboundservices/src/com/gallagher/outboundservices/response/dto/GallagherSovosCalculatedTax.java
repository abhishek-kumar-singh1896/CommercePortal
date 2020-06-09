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

	@JsonProperty("txName")
	private String txName;

	@JsonProperty("txAmt")
	private Double txAmt;

	@JsonProperty("txRate")
	private Double txRate;

	@JsonProperty("aggrTxJurTp1")
	private String aggrTxJurTp1;

	@JsonProperty("txJurUIDJurTp")
	private String txJurUIDJurTp;

	@JsonProperty("txJurUIDTxwTJId")
	private String txJurUIDTxwTJId;


	public String getTxName()
	{
		return txName;
	}

	public void setTxName(final String txName)
	{
		this.txName = txName;
	}

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

	public String getAggrTxJurTp1()
	{
		return aggrTxJurTp1;
	}

	public void setAggrTxJurTp1(final String aggrTxJurTp1)
	{
		this.aggrTxJurTp1 = aggrTxJurTp1;
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
