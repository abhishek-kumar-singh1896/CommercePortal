package com.enterprisewide.b2badvance.outboundintegration.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds Customer details for Order Request
 * 
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceOrderContactDTO {

	@JsonProperty("contactName")
	private String contactName;

	@JsonProperty("contactEmailAddress")
	private String contactEmailAddress;

	@JsonProperty("contactCustomerID")
	private String contactCustomerID;

	@JsonProperty("contactOrganization")
	private List<B2BAdvanceOrganizationDTO> contactOrganization;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmailAddress() {
		return contactEmailAddress;
	}

	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

	public String getContactCustomerID() {
		return contactCustomerID;
	}

	public void setContactCustomerID(String contactCustomerID) {
		this.contactCustomerID = contactCustomerID;
	}

	public List<B2BAdvanceOrganizationDTO> getContactOrganization() {
		return contactOrganization;
	}

	public void setContactOrganization(List<B2BAdvanceOrganizationDTO> contactOrganization) {
		this.contactOrganization = contactOrganization;
	}

}
