package com.enterprisewide.b2badvance.outboundintegration.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Create Order Response DTO
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceCreateOrderResponse extends B2BAdvanceBaseResponse {
	@JsonProperty("ERPOrderID")
	private String erpOrderID;

	@JsonProperty("hybrisOrderID")
	private String hybrisOrderId;

	public String getErpOrderID() {
		return erpOrderID;
	}

	public void setErpOrderID(final String erpOrderID) {
		this.erpOrderID = erpOrderID;
	}

	public String getHybrisOrderId() {
		return hybrisOrderId;
	}

	public void setHybrisOrderId(final String hybrisOrderId) {
		this.hybrisOrderId = hybrisOrderId;
	}
}