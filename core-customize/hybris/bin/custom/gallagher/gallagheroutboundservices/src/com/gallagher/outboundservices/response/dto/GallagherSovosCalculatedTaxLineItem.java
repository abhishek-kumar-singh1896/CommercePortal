/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.List;


/**
 *
 */
public class GallagherSovosCalculatedTaxLineItem
{
	private String lnNm;

	private String lnId;

	private String txwLnId;

	private String txAmt;

	private String grossAmt;

	private String txRate;

	private List<GallagherSovosCalculatedTax> jurRslts;

	private String mergedResult;

	public void setLnNm(final String lnNm)
	{
		this.lnNm = lnNm;
	}

	public String getLnNm()
	{
		return this.lnNm;
	}

	public void setLnId(final String lnId)
	{
		this.lnId = lnId;
	}

	public String getLnId()
	{
		return this.lnId;
	}

	public void setTxwLnId(final String txwLnId)
	{
		this.txwLnId = txwLnId;
	}

	public String getTxwLnId()
	{
		return this.txwLnId;
	}

	public void setTxAmt(final String txAmt)
	{
		this.txAmt = txAmt;
	}

	public String getTxAmt()
	{
		return this.txAmt;
	}

	public void setGrossAmt(final String grossAmt)
	{
		this.grossAmt = grossAmt;
	}

	public String getGrossAmt()
	{
		return this.grossAmt;
	}

	public void setTxRate(final String txRate)
	{
		this.txRate = txRate;
	}

	public String getTxRate()
	{
		return this.txRate;
	}

	public void setJurRslts(final List<GallagherSovosCalculatedTax> jurRslts)
	{
		this.jurRslts = jurRslts;
	}

	public List<GallagherSovosCalculatedTax> getJurRslts()
	{
		return this.jurRslts;
	}

	public void setMergedResult(final String mergedResult)
	{
		this.mergedResult = mergedResult;
	}

	public String getMergedResult()
	{
		return this.mergedResult;
	}
}
