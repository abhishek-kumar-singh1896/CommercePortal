package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceLogEntriesDTO stores error message response from web
 * service..
 *
 * @author Vikram
 */
public class B2BAdvanceLogEntriesDTO {

	@JsonProperty("logID")
	private String logID;

	@JsonProperty("logType")
	private String logType;

	@JsonProperty("message")
	private String message;

	public String getLogID() {
		return logID;
	}

	public void setLogID(final String logID) {
		this.logID = logID;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(final String logType) {
		this.logType = logType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
