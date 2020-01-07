package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceOrganizationDTO has attributes related to Organization.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceOrganizationDTO {

	@JsonProperty("parentBusinessUnitID")
	private String parentBusinessUnitID;

	@JsonProperty("parentBusinessUnitName")
	private String parentBusinessUnitName;

	@JsonProperty("businessUnitName")
	private String businessUnitName;

	@JsonProperty("businessUnitID")
	private String businessUnitID;

	public String getParentBusinessUnitID() {
		return parentBusinessUnitID;
	}

	public void setParentBusinessUnitID(String parentBusinessUnitID) {
		this.parentBusinessUnitID = parentBusinessUnitID;
	}

	public String getParentBusinessUnitName() {
		return parentBusinessUnitName;
	}

	public void setParentBusinessUnitName(String parentBusinessUnitName) {
		this.parentBusinessUnitName = parentBusinessUnitName;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getBusinessUnitID() {
		return businessUnitID;
	}

	public void setBusinessUnitID(String businessUnitID) {
		this.businessUnitID = businessUnitID;
	}
}
