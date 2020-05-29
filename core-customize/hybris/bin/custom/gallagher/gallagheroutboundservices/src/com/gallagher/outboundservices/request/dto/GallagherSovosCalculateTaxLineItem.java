/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

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

	@JsonProperty("sFCountry")
	private String sFCountry;

	@JsonProperty("sFCity")
	private String sFCity;

	@JsonProperty("sFStateProv")
	private String sFStateProv;

	@JsonProperty("sFPstlCd")
	private String sFPstlCd;

	@JsonProperty("sTCity")
	private String sTCity;

	@JsonProperty("sTStateProv")
	private String sTStateProv;

	@JsonProperty("sTCountry")
	private String sTCountry;

	@JsonProperty("sTPstlCd")
	private String sTPstlCd;

	@JsonProperty("sTStNameNum")
	private String sTStNameNum;

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

	public String getsFCountry()
	{
		return sFCountry;
	}

	public void setsFCountry(final String sFCountry)
	{
		this.sFCountry = sFCountry;
	}

	public String getsFCity()
	{
		return sFCity;
	}

	public void setsFCity(final String sFCity)
	{
		this.sFCity = sFCity;
	}

	public String getsFStateProv()
	{
		return sFStateProv;
	}

	public void setsFStateProv(final String sFStateProv)
	{
		this.sFStateProv = sFStateProv;
	}

	public String getsFPstlCd()
	{
		return sFPstlCd;
	}

	public void setsFPstlCd(final String sFPstlCd)
	{
		this.sFPstlCd = sFPstlCd;
	}

	public String getsTCity()
	{
		return sTCity;
	}

	public void setsTCity(final String sTCity)
	{
		this.sTCity = sTCity;
	}

	public String getsTStateProv()
	{
		return sTStateProv;
	}

	public void setsTStateProv(final String sTStateProv)
	{
		this.sTStateProv = sTStateProv;
	}

	public String getsTCountry()
	{
		return sTCountry;
	}

	public void setsTCountry(final String sTCountry)
	{
		this.sTCountry = sTCountry;
	}

	public String getsTPstlCd()
	{
		return sTPstlCd;
	}

	public void setsTPstlCd(final String sTPstlCd)
	{
		this.sTPstlCd = sTPstlCd;
	}

	public String getsTStNameNum()
	{
		return sTStNameNum;
	}

	public void setsTStNameNum(final String sTStNameNum)
	{
		this.sTStNameNum = sTStNameNum;
	}
}
