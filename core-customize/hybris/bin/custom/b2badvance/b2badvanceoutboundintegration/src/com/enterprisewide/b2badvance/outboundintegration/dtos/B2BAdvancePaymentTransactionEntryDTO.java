package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold payment transaction entry
 *
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvancePaymentTransactionEntryDTO {
	@JsonProperty("code")
	private String code;

	@JsonProperty("time")
	private String time;

	@JsonProperty("amount")
	private String amount;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("requestID")
	private String requestID;

	@JsonProperty("requestToken")
	private String requestToken;

	@JsonProperty("transactionStatus")
	private String transactionStatus;

	@JsonProperty("transactionStatusDetails")
	private String transactionStatusDetails;

	@JsonProperty("type")
	private String type;

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getTime() {
		return time;
	}

	public void setTime(final String time) {
		this.time = time;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(final String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(final String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionStatusDetails() {
		return transactionStatusDetails;
	}

	public void setTransactionStatusDetails(final String transactionStatusDetails) {
		this.transactionStatusDetails = transactionStatusDetails;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
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
}
