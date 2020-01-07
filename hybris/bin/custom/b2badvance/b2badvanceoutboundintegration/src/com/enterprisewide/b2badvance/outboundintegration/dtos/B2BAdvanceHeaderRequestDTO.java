package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceHeaderRequestDTO has attributes for header sent in
 * webservice request.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceHeaderRequestDTO {
	@JsonProperty("userID")
	private String userID;
	@JsonProperty("systemID")
	private String systemID;
	@JsonProperty("timestamp")
	private String timestamp;

	public String getUserID() {
		return userID;
	}

	public void setUserID(final String userID) {
		this.userID = userID;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(final String systemID) {
		this.systemID = systemID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final String timestamp) {
		this.timestamp = timestamp;
	}
}
