package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to hold payment details
 *
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvancePaymentDetailsDTO {
	@JsonProperty("paymentType")
	private String paymentType;

	@JsonProperty("braintreeChannel")
	private String braintreeChannel;

	@JsonProperty("hybrisPaymentIdentifier")
	private String hybrisPaymentIdentifier;

	@JsonProperty("methodNonce")
	private String methodNonce;

	@JsonProperty("braintreeCustomerID")
	private String braintreeCustomerID;

	@JsonProperty("paymentMethodToken")
	private String paymentMethodToken;

	@JsonProperty("cardNumber")
	private String cardNumber;

	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("cardOwner")
	private String cardOwner;

	@JsonProperty("billingAddress")
	private B2BAdvanceAddressDTO billingAddress;

	public B2BAdvanceAddressDTO getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(B2BAdvanceAddressDTO billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBraintreeChannel() {
		return braintreeChannel;
	}

	public void setBraintreeChannel(final String braintreeChannel) {
		this.braintreeChannel = braintreeChannel;
	}

	public String getHybrisPaymentIdentifier() {
		return hybrisPaymentIdentifier;
	}

	public void setHybrisPaymentIdentifier(final String hybrisPaymentIdentifier) {
		this.hybrisPaymentIdentifier = hybrisPaymentIdentifier;
	}

	public String getMethodNonce() {
		return methodNonce;
	}

	public void setMethodNonce(final String methodNonce) {
		this.methodNonce = methodNonce;
	}

	public String getBraintreeCustomerID() {
		return braintreeCustomerID;
	}

	public void setBraintreeCustomerID(final String braintreeCustomerID) {
		this.braintreeCustomerID = braintreeCustomerID;
	}

	public String getPaymentMethodToken() {
		return paymentMethodToken;
	}

	public void setPaymentMethodToken(final String paymentMethodToken) {
		this.paymentMethodToken = paymentMethodToken;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(final String cardType) {
		this.cardType = cardType;
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(final String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(final String paymentType) {
		this.paymentType = paymentType;
	}
}
