package com.enterprisewide.b2badvance.outboundintegration.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceErrorResponseDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceHeaderResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceBaseResponse gets populated from response received in
 * header by web service.
 *
 * @author Enterprise Wide
 *
 */
public class B2BAdvanceBaseResponse {
	@JsonProperty("header")
	private B2BAdvanceHeaderResponseDTO header;

	@JsonProperty("errors")
	private List<B2BAdvanceErrorResponseDTO> errors;

	@XmlElement(name = "type")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public B2BAdvanceHeaderResponseDTO getHeader() {
		return header;
	}

	public void setHeader(final B2BAdvanceHeaderResponseDTO header) {
		this.header = header;
	}

	public List<B2BAdvanceErrorResponseDTO> getErrors() {
		return errors;
	}

	public void setErrors(final List<B2BAdvanceErrorResponseDTO> errors) {
		this.errors = errors;
	}
}
