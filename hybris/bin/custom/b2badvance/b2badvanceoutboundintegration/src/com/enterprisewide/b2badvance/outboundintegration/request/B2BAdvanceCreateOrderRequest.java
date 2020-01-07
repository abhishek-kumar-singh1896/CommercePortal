package com.enterprisewide.b2badvance.outboundintegration.request;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * B2BAdvanceCreateOrderRequest for the order outbound
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceCreateOrderRequest extends B2BAdvanceBaseRequest {
	@JsonProperty("order")
	private B2BAdvanceOrderRequestDTO orderRequest;

	public B2BAdvanceOrderRequestDTO getOrderRequest() {
		return orderRequest;
	}

	public void setOrderRequest(final B2BAdvanceOrderRequestDTO orderRequest) {
		this.orderRequest = orderRequest;
	}
}