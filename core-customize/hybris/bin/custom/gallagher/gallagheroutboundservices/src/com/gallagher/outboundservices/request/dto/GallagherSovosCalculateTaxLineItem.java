/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Sovos Calculate Tax Request's Line Item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GallagherSovosCalculateTaxLineItem
{
	@JsonProperty("debCredIndr")
	private int debCredIndr;

	@JsonProperty("goodSrvCd")
	private String goodSrvCd;

	@JsonProperty("goodSrvDesc")
	private String goodSrvDesc;

	@JsonProperty("grossAmt")
	private double grossAmt;

	@JsonProperty("lnItmId")
	private int lnItmId;

	@JsonProperty("qnty")
	private double qnty;

	@JsonProperty("trnTp")
	private int trnTp;

	@JsonProperty("orgCd")
	private String orgCd;

	@JsonProperty("dropShipInd")
	private int dropShipInd;

	@JsonProperty("sFGeoCd")
	private int sFGeoCd;

	@JsonProperty("lOAGeoCd")
	private int iOAGeoCd;

	@JsonProperty("lORGeoCd")
	private int iORGeoCd;

	@JsonProperty("sTGeoCd")
	private int sTGeoCd;

	@JsonProperty("lSPGeoCd")
	private int iSPGeoCd;

	@JsonProperty("lUGeoCd")
	private int iUGeoCd;

	@JsonProperty("discnts")
	private Map<String, Double> discnts;


	public int getDebCredIndr()
	{
		return debCredIndr;
	}

	public void setDebCredIndr(final int debCredIndr)
	{
		this.debCredIndr = debCredIndr;
	}

	public String getGoodSrvCd()
	{
		return goodSrvCd;
	}

	public void setGoodSrvCd(final String goodSrvCd)
	{
		this.goodSrvCd = goodSrvCd;
	}

	public String getGoodSrvDesc()
	{
		return goodSrvDesc;
	}

	public void setGoodSrvDesc(final String goodSrvDesc)
	{
		this.goodSrvDesc = goodSrvDesc;
	}

	public double getGrossAmt()
	{
		return grossAmt;
	}

	public void setGrossAmt(final double grossAmt)
	{
		this.grossAmt = grossAmt;
	}

	public int getLnItmId()
	{
		return lnItmId;
	}

	public void setLnItmId(final int lnItmId)
	{
		this.lnItmId = lnItmId;
	}

	public double getQnty()
	{
		return qnty;
	}

	public void setQnty(final double qnty)
	{
		this.qnty = qnty;
	}

	public int getTrnTp()
	{
		return trnTp;
	}

	public void setTrnTp(final int trnTp)
	{
		this.trnTp = trnTp;
	}

	public String getOrgCd()
	{
		return orgCd;
	}

	public void setOrgCd(final String orgCd)
	{
		this.orgCd = orgCd;
	}

	public int getDropShipInd()
	{
		return dropShipInd;
	}

	public void setDropShipInd(final int dropShipInd)
	{
		this.dropShipInd = dropShipInd;
	}

	public int getsFGeoCd()
	{
		return sFGeoCd;
	}

	public void setsFGeoCd(final int sFGeoCd)
	{
		this.sFGeoCd = sFGeoCd;
	}

	public int getsTGeoCd()
	{
		return sTGeoCd;
	}

	public void setsTGeoCd(final int sTGeoCd)
	{
		this.sTGeoCd = sTGeoCd;
	}

	public int getiOAGeoCd()
	{
		return iOAGeoCd;
	}

	public void setiOAGeoCd(final int iOAGeoCd)
	{
		this.iOAGeoCd = iOAGeoCd;
	}

	public int getiORGeoCd()
	{
		return iORGeoCd;
	}

	public void setiORGeoCd(final int iORGeoCd)
	{
		this.iORGeoCd = iORGeoCd;
	}

	public int getiSPGeoCd()
	{
		return iSPGeoCd;
	}

	public void setiSPGeoCd(final int iSPGeoCd)
	{
		this.iSPGeoCd = iSPGeoCd;
	}

	public int getiUGeoCd()
	{
		return iUGeoCd;
	}

	public void setiUGeoCd(final int iUGeoCd)
	{
		this.iUGeoCd = iUGeoCd;
	}

	public Map<String, Double> getDiscnts()
	{
		return discnts;
	}

	public void setDiscnts(final Map<String, Double> discnts)
	{
		this.discnts = discnts;
	}
}