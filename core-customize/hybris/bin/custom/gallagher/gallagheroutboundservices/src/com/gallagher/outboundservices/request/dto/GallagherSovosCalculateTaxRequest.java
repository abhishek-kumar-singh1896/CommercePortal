/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * Representation of Sovos Calculate Tax Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GallagherSovosCalculateTaxRequest
{
	@JsonProperty("usrname")
	private String usrname;

	@JsonProperty("pswrd")
	private String pswrd;

	@JsonProperty("rsltLvl")
	private int rsltLvl;

	@JsonProperty("trnId")
	private String trnId;

	@JsonProperty("currn")
	private String currn;

	@JsonProperty("docDt")
	private String docDt;

	@JsonProperty("txCalcTp")
	private int txCalcTp;

	@JsonProperty("trnDocNum")
	private String trnDocNum;

	@JsonProperty("trnSrc")
	private String trnSrc;

	@JsonProperty("dlvrAmt")
	private double dlvrAmt;

	@JsonProperty("lines")
	private List<GallagherSovosCalculateTaxLineItem> lines;

	public String getUsrname()
	{
		return usrname;
	}

	public void setUsrname(final String usrname)
	{
		this.usrname = usrname;
	}

	public String getPswrd()
	{
		return pswrd;
	}

	public void setPswrd(final String pswrd)
	{
		this.pswrd = pswrd;
	}

	public int getRsltLvl()
	{
		return rsltLvl;
	}

	public void setRsltLvl(final int rsltLvl)
	{
		this.rsltLvl = rsltLvl;
	}

	public String getTrnId()
	{
		return trnId;
	}

	public void setTrnId(final String trnId)
	{
		this.trnId = trnId;
	}

	public String getCurrn()
	{
		return currn;
	}

	public void setCurrn(final String currn)
	{
		this.currn = currn;
	}

	public String getDocDt()
	{
		return docDt;
	}

	public void setDocDt(final String docDt)
	{
		this.docDt = docDt;
	}

	public int getTxCalcTp()
	{
		return txCalcTp;
	}

	public void setTxCalcTp(final int txCalcTp)
	{
		this.txCalcTp = txCalcTp;
	}

	public String getTrnDocNum()
	{
		return trnDocNum;
	}

	public void setTrnDocNum(final String trnDocNum)
	{
		this.trnDocNum = trnDocNum;
	}

	public String getTrnSrc()
	{
		return trnSrc;
	}

	public void setTrnSrc(final String trnSrc)
	{
		this.trnSrc = trnSrc;
	}

	public double getDlvrAmt()
	{
		return dlvrAmt;
	}

	public void setDlvrAmt(final double dlvrAmt)
	{
		this.dlvrAmt = dlvrAmt;
	}

	public List<GallagherSovosCalculateTaxLineItem> getLines()
	{
		return lines;
	}

	public void setLines(final List<GallagherSovosCalculateTaxLineItem> lines)
	{
		this.lines = lines;
	}

}