package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * B2BAdvanceAddressDTO holds delivery address
 *
 * @author Enterprisewide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceAddressDTO {

	@JsonProperty("addressID")
	private String addressID;

	@JsonProperty("addressKind")
	private String addressKind;

	@JsonProperty("dpid")
	private String dpid;

	@JsonProperty("city")
	private String city;

	@JsonProperty("postcode")
	private String postcode;

	@JsonProperty("street")
	private String street;

	@JsonProperty("streetNumber")
	private String streetNumber;

	@JsonProperty("houseNumber")
	private String houseNumber;

	@JsonProperty("suburb")
	private String suburb;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;

	public String getAddressID() {
		return addressID;
	}

	public void setAddressID(final String addressID) {
		this.addressID = addressID;
	}

	public String getAddressKind() {
		return addressKind;
	}

	public void setAddressKind(final String addressKind) {
		this.addressKind = addressKind;
	}

	public String getDpid() {
		return dpid;
	}

	public void setDpid(final String dpid) {
		this.dpid = dpid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(final String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(final String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(final String suburb) {
		this.suburb = suburb;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}
}