package com.enterprisewide.b2badvance.outboundintegration.dtos;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceHeaderResponseDTO has attributes for header sent in
 * webservice response.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceHeaderResponseDTO {

	@JsonProperty("responseCode")
	private String responseCode;

	private HttpStatus httpStatus;

	@JsonProperty("logEntries")
	private List<B2BAdvanceLogEntriesDTO> logEntries;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(final String responseCode) {
		this.responseCode = responseCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(final HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public List<B2BAdvanceLogEntriesDTO> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(final List<B2BAdvanceLogEntriesDTO> logEntries) {
		this.logEntries = logEntries;
	}
}
