/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import java.util.List;



/**
*
*/
public class GallagherSovosCalculateTaxRequest
{
	private String usrname;

	private String pswrd;

	private String trnId;

	private String trnSrc;

	private String rsltLvl;

	private String currn;

	private String docDt;

	private int txCalcTp;

	private String trnDocNum;

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