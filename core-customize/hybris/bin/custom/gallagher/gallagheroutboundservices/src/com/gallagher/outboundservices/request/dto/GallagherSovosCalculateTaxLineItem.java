/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

/**
*
*/
public class GallagherSovosCalculateTaxLineItem
{
	private int lnItmNum;

	private String lnItmId;

	private double grossAmt;

	private int qnty;

	private int trnTp;

	private int dropShipInd;

	private String origTrnDt;

	private String goodSrvCd;

	private String orgCd;

	private int debCredIndr;

	private String qntyUMCd;

	private String sTStNameNum;

	private String sTCity;

	private String sTStateProv;

	private String sTPstlCd;

	private String sTCountry;

	private String sFCity;

	private String sFStateProv;

	private String sFPstlCd;

	private String sFCountry;

	private String lOACity;

	private String lOAStateProv;

	private String lOAPstlCd;

	private String lOACountry;

	private String lORCity;

	private String lORStateProv;

	private String lORPstlCd;

	private String lORCountry;

	public void setLnItmNum(final int lnItmNum)
	{
		this.lnItmNum = lnItmNum;
	}

	public int getLnItmNum()
	{
		return this.lnItmNum;
	}

	public void setLnItmId(final String lnItmId)
	{
		this.lnItmId = lnItmId;
	}

	public String getLnItmId()
	{
		return this.lnItmId;
	}

	public void setGrossAmt(final double grossAmt)
	{
		this.grossAmt = grossAmt;
	}

	public double getGrossAmt()
	{
		return this.grossAmt;
	}

	public void setQnty(final int qnty)
	{
		this.qnty = qnty;
	}

	public int getQnty()
	{
		return this.qnty;
	}

	public void setTrnTp(final int trnTp)
	{
		this.trnTp = trnTp;
	}

	public int getTrnTp()
	{
		return this.trnTp;
	}

	public void setDropShipInd(final int dropShipInd)
	{
		this.dropShipInd = dropShipInd;
	}

	public int getDropShipInd()
	{
		return this.dropShipInd;
	}

	public void setOrigTrnDt(final String origTrnDt)
	{
		this.origTrnDt = origTrnDt;
	}

	public String getOrigTrnDt()
	{
		return this.origTrnDt;
	}

	public void setGoodSrvCd(final String goodSrvCd)
	{
		this.goodSrvCd = goodSrvCd;
	}

	public String getGoodSrvCd()
	{
		return this.goodSrvCd;
	}

	public void setOrgCd(final String orgCd)
	{
		this.orgCd = orgCd;
	}

	public String getOrgCd()
	{
		return this.orgCd;
	}

	public void setDebCredIndr(final int debCredIndr)
	{
		this.debCredIndr = debCredIndr;
	}

	public int getDebCredIndr()
	{
		return this.debCredIndr;
	}

	public void setQntyUMCd(final String qntyUMCd)
	{
		this.qntyUMCd = qntyUMCd;
	}

	public String getQntyUMCd()
	{
		return this.qntyUMCd;
	}

	public void setSTStNameNum(final String sTStNameNum)
	{
		this.sTStNameNum = sTStNameNum;
	}

	public String getSTStNameNum()
	{
		return this.sTStNameNum;
	}

	public void setSTCity(final String sTCity)
	{
		this.sTCity = sTCity;
	}

	public String getSTCity()
	{
		return this.sTCity;
	}

	public void setSTStateProv(final String sTStateProv)
	{
		this.sTStateProv = sTStateProv;
	}

	public String getSTStateProv()
	{
		return this.sTStateProv;
	}

	public void setSTPstlCd(final String sTPstlCd)
	{
		this.sTPstlCd = sTPstlCd;
	}

	public String getSTPstlCd()
	{
		return this.sTPstlCd;
	}

	public void setSTCountry(final String sTCountry)
	{
		this.sTCountry = sTCountry;
	}

	public String getSTCountry()
	{
		return this.sTCountry;
	}

	public void setSFCity(final String sFCity)
	{
		this.sFCity = sFCity;
	}

	public String getSFCity()
	{
		return this.sFCity;
	}

	public void setSFStateProv(final String sFStateProv)
	{
		this.sFStateProv = sFStateProv;
	}

	public String getSFStateProv()
	{
		return this.sFStateProv;
	}

	public void setSFPstlCd(final String sFPstlCd)
	{
		this.sFPstlCd = sFPstlCd;
	}

	public String getSFPstlCd()
	{
		return this.sFPstlCd;
	}

	public void setSFCountry(final String sFCountry)
	{
		this.sFCountry = sFCountry;
	}

	public String getSFCountry()
	{
		return this.sFCountry;
	}

	public void setLOACity(final String lOACity)
	{
		this.lOACity = lOACity;
	}

	public String getLOACity()
	{
		return this.lOACity;
	}

	public void setLOAStateProv(final String lOAStateProv)
	{
		this.lOAStateProv = lOAStateProv;
	}

	public String getLOAStateProv()
	{
		return this.lOAStateProv;
	}

	public void setLOAPstlCd(final String lOAPstlCd)
	{
		this.lOAPstlCd = lOAPstlCd;
	}

	public String getLOAPstlCd()
	{
		return this.lOAPstlCd;
	}

	public void setLOACountry(final String lOACountry)
	{
		this.lOACountry = lOACountry;
	}

	public String getLOACountry()
	{
		return this.lOACountry;
	}

	public void setLORCity(final String lORCity)
	{
		this.lORCity = lORCity;
	}

	public String getLORCity()
	{
		return this.lORCity;
	}

	public void setLORStateProv(final String lORStateProv)
	{
		this.lORStateProv = lORStateProv;
	}

	public String getLORStateProv()
	{
		return this.lORStateProv;
	}

	public void setLORPstlCd(final String lORPstlCd)
	{
		this.lORPstlCd = lORPstlCd;
	}

	public String getLORPstlCd()
	{
		return this.lORPstlCd;
	}

	public void setLORCountry(final String lORCountry)
	{
		this.lORCountry = lORCountry;
	}

	public String getLORCountry()
	{
		return this.lORCountry;
	}
}
