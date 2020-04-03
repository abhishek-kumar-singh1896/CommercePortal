/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import java.util.List;


/**
 *
 */
public class GallagherSovosCalculatedTaxResponse
{
	private String trnDocNum;

	private String txwTrnDocId;

	private String txAmt;

	private List<GallagherSovosCalculatedTaxLineItem> lnRslts;

	public void setTrnDocNum(final String trnDocNum)
	{
		this.trnDocNum = trnDocNum;
	}

	public String getTrnDocNum()
	{
		return this.trnDocNum;
	}

	public void setTxwTrnDocId(final String txwTrnDocId)
	{
		this.txwTrnDocId = txwTrnDocId;
	}

	public String getTxwTrnDocId()
	{
		return this.txwTrnDocId;
	}

	public void setTxAmt(final String txAmt)
	{
		this.txAmt = txAmt;
	}

	public String getTxAmt()
	{
		return this.txAmt;
	}

	public void setLnRslts(final List<GallagherSovosCalculatedTaxLineItem> lnRslts)
	{
		this.lnRslts = lnRslts;
	}

	public List<GallagherSovosCalculatedTaxLineItem> getLnRslts()
	{
		return this.lnRslts;
	}
}
