package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceErrorResponseDTO stores error message response from web
 * service..
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceErrorResponseDTO {

	@JsonProperty("message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
