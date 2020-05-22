/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.request.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Sovos GeoCode Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GallagherSovosGeoCodeRequest
{
	@JsonProperty("usrname")
	private String usrname;

	@JsonProperty("pswrd")
	private String pswrd;

	@JsonProperty("city")
	private String city;

	@JsonProperty("stateProv")
	private String stateProv;

	@JsonProperty("pstlCd")
	private String pstlCd;

	@JsonProperty("country")
	private String country;

	@JsonProperty("oOCLmtInd")
	private boolean oOCLmtInd;

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

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getStateProv()
	{
		return stateProv;
	}

	public void setStateProv(final String stateProv)
	{
		this.stateProv = stateProv;
	}

	public String getPstlCd()
	{
		return pstlCd;
	}

	public void setPstlCd(final String pstlCd)
	{
		this.pstlCd = pstlCd;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(final String country)
	{
		this.country = country;
	}

	public boolean getoOCLmtInd()
	{
		return oOCLmtInd;
	}

	public void setoOCLmtInd(final boolean oOCLmtInd)
	{
		this.oOCLmtInd = oOCLmtInd;
	}
}
