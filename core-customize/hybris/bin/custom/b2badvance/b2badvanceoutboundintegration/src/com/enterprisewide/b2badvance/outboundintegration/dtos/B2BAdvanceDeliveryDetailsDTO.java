package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold delivery details
 *
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceDeliveryDetailsDTO {
	@JsonProperty("deliveryAddress")
	private B2BAdvanceAddressDTO deliveryAddress;

	@JsonProperty("deliveryMethod")
	private String deliveryMethod;

	@JsonProperty("deliveryInstructions")
	private String deliveryInstructions;

	public B2BAdvanceAddressDTO getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(final B2BAdvanceAddressDTO deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}

	public void setDeliveryInstructions(final String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
}
