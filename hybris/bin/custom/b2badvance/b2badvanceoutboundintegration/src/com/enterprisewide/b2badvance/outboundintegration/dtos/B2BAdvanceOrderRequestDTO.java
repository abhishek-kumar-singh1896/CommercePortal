package com.enterprisewide.b2badvance.outboundintegration.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceOrderRequestDTO has attributes related to Order.
 *
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceOrderRequestDTO {

	@JsonProperty("hybrisOrderNumber")
	private String hybrisOrderNumber;

	@JsonProperty("contact")
	private B2BAdvanceOrderContactDTO contact;

	@JsonProperty("date")
	private String date;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("entries")
	private List<B2BAdvanceOrderEntryDTO> entries;

	@JsonProperty("taxes")
	private List<B2BAdvanceTaxDTO> taxes;

	@JsonProperty("deliveryCost")
	private Double deliveryCost;

	@JsonProperty("totalTax")
	private Double totalTax;

	@JsonProperty("total")
	private Double total;

	@JsonProperty("paymentTransactions")
	private List<B2BAdvancePaymentTransactionDTO> paymentTransactions;

	@JsonProperty("deliveryDetails")
	private B2BAdvanceDeliveryDetailsDTO deliveryDetails;

	public String getHybrisOrderNumber() {
		return hybrisOrderNumber;
	}

	public void setHybrisOrderNumber(final String hybrisOrderNumber) {
		this.hybrisOrderNumber = hybrisOrderNumber;
	}

	public B2BAdvanceOrderContactDTO getContact() {
		return contact;
	}

	public void setContact(final B2BAdvanceOrderContactDTO contact) {
		this.contact = contact;
	}

	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public List<B2BAdvanceOrderEntryDTO> getEntries() {
		return entries;
	}

	public void setEntries(final List<B2BAdvanceOrderEntryDTO> entries) {
		this.entries = entries;
	}

	public List<B2BAdvanceTaxDTO> getTaxes() {
		return taxes;
	}

	public void setTaxes(final List<B2BAdvanceTaxDTO> taxes) {
		this.taxes = taxes;
	}

	public Double getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(final Double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(final Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(final Double total) {
		this.total = total;
	}

	public B2BAdvanceDeliveryDetailsDTO getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(final B2BAdvanceDeliveryDetailsDTO deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
}
