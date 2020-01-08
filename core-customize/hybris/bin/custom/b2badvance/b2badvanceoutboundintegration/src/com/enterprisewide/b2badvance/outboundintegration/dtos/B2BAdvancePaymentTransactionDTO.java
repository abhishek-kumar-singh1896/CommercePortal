package com.enterprisewide.b2badvance.outboundintegration.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold payment details
 *
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvancePaymentTransactionDTO {

	@JsonProperty("code")
	private String code;

	@JsonProperty("paymentProvider")
	private String paymentProvider;

	@JsonProperty("plannedAmount")
	private String plannedAmount;

	@JsonProperty("requestID")
	private String requestID;

	@JsonProperty("requestToken")
	private String requestToken;

	@JsonProperty("paymentTransactionEntries")
	private List<B2BAdvancePaymentTransactionEntryDTO> paymentTransactionEntries;

	private B2BAdvancePaymentDetailsDTO braintreePaymentInfo;

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getPaymentProvider() {
		return paymentProvider;
	}

	public void setPaymentProvider(final String paymentProvider) {
		this.paymentProvider = paymentProvider;
	}

	public String getPlannedAmount() {
		return plannedAmount;
	}

	public void setPlannedAmount(final String plannedAmount) {
		this.plannedAmount = plannedAmount;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(final String requestID) {
		this.requestID = requestID;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(final String requestToken) {
		this.requestToken = requestToken;
	}

	public List<B2BAdvancePaymentTransactionEntryDTO> getPaymentTransactionEntries() {
		return paymentTransactionEntries;
	}

	public void setPaymentTransactionEntries(
			final List<B2BAdvancePaymentTransactionEntryDTO> paymentTransactionEntries) {
		this.paymentTransactionEntries = paymentTransactionEntries;
	}

	public B2BAdvancePaymentDetailsDTO getBraintreePaymentInfo() {
		return braintreePaymentInfo;
	}

	public void setBraintreePaymentInfo(final B2BAdvancePaymentDetailsDTO braintreePaymentInfo) {
		this.braintreePaymentInfo = braintreePaymentInfo;
	}
}
