/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Representation of Sovos GeoCode response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GallagherSovosGeoCodeResponse
{
	@JsonProperty("stateProv")
	private String stateProv;

	@JsonProperty("stateISOCd")
	private String stateISOCd;

	@JsonProperty("city")
	private String city;

	@JsonProperty("country")
	private String country;

	@JsonProperty("countryISOCd")
	private String countryISOCd;

	@JsonProperty("pstlCd")
	private String pstlCd;

	@JsonProperty("oOCLmtInd")
	private boolean oOCLmtInd;

	@JsonProperty("geoCd")
	private String geoCd;

	@JsonProperty("pstlCdStrt")
	private String pstlCdStrt;

	@JsonProperty("pstlCdEnd")
	private String pstlCdEnd;

	public String getStateProv()
	{
		return stateProv;
	}

	public void setStateProv(final String stateProv)
	{
		this.stateProv = stateProv;
	}

	public String getStateISOCd()
	{
		return stateISOCd;
	}

	public void setStateISOCd(final String stateISOCd)
	{
		this.stateISOCd = stateISOCd;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(final String country)
	{
		this.country = country;
	}

	public String getCountryISOCd()
	{
		return countryISOCd;
	}

	public void setCountryISOCd(final String countryISOCd)
	{
		this.countryISOCd = countryISOCd;
	}

	public String getPstlCd()
	{
		return pstlCd;
	}

	public void setPstlCd(final String pstlCd)
	{
		this.pstlCd = pstlCd;
	}

	public boolean getoOCLmtInd()
	{
		return oOCLmtInd;
	}

	public void setoOCLmtInd(final boolean oOCLmtInd)
	{
		this.oOCLmtInd = oOCLmtInd;
	}

	public String getGeoCd()
	{
		return geoCd;
	}

	public void setGeoCd(final String geoCd)
	{
		this.geoCd = geoCd;
	}

	public String getPstlCdStrt()
	{
		return pstlCdStrt;
	}

	public void setPstlCdStrt(final String pstlCdStrt)
	{
		this.pstlCdStrt = pstlCdStrt;
	}

	public String getPstlCdEnd()
	{
		return pstlCdEnd;
	}

	public void setPstlCdEnd(final String pstlCdEnd)
	{
		this.pstlCdEnd = pstlCdEnd;
	}

}
