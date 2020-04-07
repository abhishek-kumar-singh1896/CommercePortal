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

	@JsonProperty("txableAmt")
	private String txableAmt;

	@JsonProperty("xmptAmt")
	private String xmptAmt;

	@JsonProperty("recvrPercnt")
	private String recvrPercnt;

	@JsonProperty("recvrAmt")
	private String recvrAmt;

	@JsonProperty("txName")
	private String txName;

	@JsonProperty("txNameID")
	private String txNameID;

	@JsonProperty("txTp")
	private String txTp;

	@JsonProperty("thrshldLvl")
	private String thrshldLvl;

	@JsonProperty("aggrTxJurTp1")
	private String aggrTxJurTp1;

	@JsonProperty("aggrTxJurTp2")
	private String aggrTxJurTp2;

	@JsonProperty("txPaidToDt")
	private String txPaidToDt;

	@JsonProperty("txablePaidToDt")
	private String txablePaidToDt;

	@JsonProperty("leasePmtTp")
	private String leasePmtTp;

	@JsonProperty("txJurUIDCntry")
	private String txJurUIDCntry;

	@JsonProperty("txJurUIDStatePrv")
	private String txJurUIDStatePrv;

	@JsonProperty("txJurUIDJurTp")
	private String txJurUIDJurTp;

	@JsonProperty("txJurUIDTxwTJId")
	private String txJurUIDTxwTJId;

	@JsonProperty("txJurUIDStatePrvISO")
	private String txJurUIDStatePrvISO;

	@JsonProperty("txJurUIDCntryISO")
	private String txJurUIDCntryISO;

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

	public void setTxableAmt(final String txableAmt)
	{
		this.txableAmt = txableAmt;
	}

	public String getTxableAmt()
	{
		return this.txableAmt;
	}

	public void setXmptAmt(final String xmptAmt)
	{
		this.xmptAmt = xmptAmt;
	}

	public String getXmptAmt()
	{
		return this.xmptAmt;
	}

	public void setRecvrPercnt(final String recvrPercnt)
	{
		this.recvrPercnt = recvrPercnt;
	}

	public String getRecvrPercnt()
	{
		return this.recvrPercnt;
	}

	public void setRecvrAmt(final String recvrAmt)
	{
		this.recvrAmt = recvrAmt;
	}

	public String getRecvrAmt()
	{
		return this.recvrAmt;
	}

	public void setTxName(final String txName)
	{
		this.txName = txName;
	}

	public String getTxName()
	{
		return this.txName;
	}

	public void setTxNameID(final String txNameID)
	{
		this.txNameID = txNameID;
	}

	public String getTxNameID()
	{
		return this.txNameID;
	}

	public void setTxTp(final String txTp)
	{
		this.txTp = txTp;
	}

	public String getTxTp()
	{
		return this.txTp;
	}

	public void setThrshldLvl(final String thrshldLvl)
	{
		this.thrshldLvl = thrshldLvl;
	}

	public String getThrshldLvl()
	{
		return this.thrshldLvl;
	}

	public void setAggrTxJurTp1(final String aggrTxJurTp1)
	{
		this.aggrTxJurTp1 = aggrTxJurTp1;
	}

	public String getAggrTxJurTp1()
	{
		return this.aggrTxJurTp1;
	}

	public void setAggrTxJurTp2(final String aggrTxJurTp2)
	{
		this.aggrTxJurTp2 = aggrTxJurTp2;
	}

	public String getAggrTxJurTp2()
	{
		return this.aggrTxJurTp2;
	}

	public void setTxPaidToDt(final String txPaidToDt)
	{
		this.txPaidToDt = txPaidToDt;
	}

	public String getTxPaidToDt()
	{
		return this.txPaidToDt;
	}

	public void setTxablePaidToDt(final String txablePaidToDt)
	{
		this.txablePaidToDt = txablePaidToDt;
	}

	public String getTxablePaidToDt()
	{
		return this.txablePaidToDt;
	}

	public void setLeasePmtTp(final String leasePmtTp)
	{
		this.leasePmtTp = leasePmtTp;
	}

	public String getLeasePmtTp()
	{
		return this.leasePmtTp;
	}

	public void setTxJurUIDCntry(final String txJurUIDCntry)
	{
		this.txJurUIDCntry = txJurUIDCntry;
	}

	public String getTxJurUIDCntry()
	{
		return this.txJurUIDCntry;
	}

	public void setTxJurUIDStatePrv(final String txJurUIDStatePrv)
	{
		this.txJurUIDStatePrv = txJurUIDStatePrv;
	}

	public String getTxJurUIDStatePrv()
	{
		return this.txJurUIDStatePrv;
	}

	public void setTxJurUIDJurTp(final String txJurUIDJurTp)
	{
		this.txJurUIDJurTp = txJurUIDJurTp;
	}

	public String getTxJurUIDJurTp()
	{
		return this.txJurUIDJurTp;
	}

	public void setTxJurUIDTxwTJId(final String txJurUIDTxwTJId)
	{
		this.txJurUIDTxwTJId = txJurUIDTxwTJId;
	}

	public String getTxJurUIDTxwTJId()
	{
		return this.txJurUIDTxwTJId;
	}

	public void setTxJurUIDStatePrvISO(final String txJurUIDStatePrvISO)
	{
		this.txJurUIDStatePrvISO = txJurUIDStatePrvISO;
	}

	public String getTxJurUIDStatePrvISO()
	{
		return this.txJurUIDStatePrvISO;
	}

	public void setTxJurUIDCntryISO(final String txJurUIDCntryISO)
	{
		this.txJurUIDCntryISO = txJurUIDCntryISO;
	}

	public String getTxJurUIDCntryISO()
	{
		return this.txJurUIDCntryISO;
	}
}
