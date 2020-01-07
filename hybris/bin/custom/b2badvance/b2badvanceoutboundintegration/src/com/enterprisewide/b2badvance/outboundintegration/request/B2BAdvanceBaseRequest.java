package com.enterprisewide.b2badvance.outboundintegration.request;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceHeaderRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceBaseRequest stores header for all the webservice request.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceBaseRequest {
	@JsonProperty("header")
	private B2BAdvanceHeaderRequestDTO header;

	public B2BAdvanceHeaderRequestDTO getHeader() {
		return header;
	}

	public void setHeader(final B2BAdvanceHeaderRequestDTO header) {
		this.header = header;
	}
}
