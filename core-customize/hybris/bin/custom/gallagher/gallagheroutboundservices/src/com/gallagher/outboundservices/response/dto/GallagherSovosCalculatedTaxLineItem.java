/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Sovos Calculated Tax Response's Line Item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherSovosCalculatedTaxLineItem
{
	@JsonProperty("lnId")
	private String lnId;

	@JsonProperty("txAmt")
	private String txAmt;

	@JsonProperty("jurRslts")
	private List<GallagherSovosCalculatedTax> jurRslts;

	public void setLnId(final String lnId)
	{
		this.lnId = lnId;
	}

	public String getLnId()
	{
		return this.lnId;
	}

	public String getTxAmt()
	{
		return txAmt;
	}

	public void setTxAmt(final String txAmt)
	{
		this.txAmt = txAmt;
	}

	public void setJurRslts(final List<GallagherSovosCalculatedTax> jurRslts)
	{
		this.jurRslts = jurRslts;
	}

	public List<GallagherSovosCalculatedTax> getJurRslts()
	{
		return this.jurRslts;
	}
}
