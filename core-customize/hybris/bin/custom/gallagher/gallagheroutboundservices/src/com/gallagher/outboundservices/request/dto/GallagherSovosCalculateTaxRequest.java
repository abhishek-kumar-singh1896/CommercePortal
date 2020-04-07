/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * Representation of Sovos Calculate Tax Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherSovosCalculateTaxRequest
{
	@JsonProperty("usrname")
	private String usrname;

	@JsonProperty("pswrd")
	private String pswrd;

	@JsonProperty("trnId")
	private String trnId;

	@JsonProperty("trnSrc")
	private String trnSrc;

	@JsonProperty("rsltLvl")
	private String rsltLvl;

	@JsonProperty("currn")
	private String currn;

	@JsonProperty("docDt")
	private String docDt;

	@JsonProperty("txCalcTp")
	private int txCalcTp;

	@JsonProperty("trnDocNum")
	private String trnDocNum;

	@JsonProperty("lines")
	private List<GallagherSovosCalculateTaxLineItem> lines;

	public void setUsrname(final String usrname)
	{
		this.usrname = usrname;
	}

	public String getUsrname()
	{
		return this.usrname;
	}

	public void setPswrd(final String pswrd)
	{
		this.pswrd = pswrd;
	}

	public String getPswrd()
	{
		return this.pswrd;
	}

	public void setTrnId(final String trnId)
	{
		this.trnId = trnId;
	}

	public String getTrnId()
	{
		return this.trnId;
	}

	public void setTrnSrc(final String trnSrc)
	{
		this.trnSrc = trnSrc;
	}

	public String getTrnSrc()
	{
		return this.trnSrc;
	}

	public void setRsltLvl(final String rsltLvl)
	{
		this.rsltLvl = rsltLvl;
	}

	public String getRsltLvl()
	{
		return this.rsltLvl;
	}

	public void setCurrn(final String currn)
	{
		this.currn = currn;
	}

	public String getCurrn()
	{
		return this.currn;
	}

	public void setDocDt(final String docDt)
	{
		this.docDt = docDt;
	}

	public String getDocDt()
	{
		return this.docDt;
	}

	public void setTxCalcTp(final int txCalcTp)
	{
		this.txCalcTp = txCalcTp;
	}

	public int getTxCalcTp()
	{
		return this.txCalcTp;
	}

	public void setTrnDocNum(final String trnDocNum)
	{
		this.trnDocNum = trnDocNum;
	}

	public String getTrnDocNum()
	{
		return this.trnDocNum;
	}

	public void setLines(final List<GallagherSovosCalculateTaxLineItem> lines)
	{
		this.lines = lines;
	}

	public List<GallagherSovosCalculateTaxLineItem> getLines()
	{
		return this.lines;
	}
}