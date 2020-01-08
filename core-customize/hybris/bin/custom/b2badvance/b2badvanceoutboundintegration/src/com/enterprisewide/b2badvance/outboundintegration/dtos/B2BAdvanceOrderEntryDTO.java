package com.enterprisewide.b2badvance.outboundintegration.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class B2BAdvanceOrderEntryDTO has attributes related to Order Entries.
 * 
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceOrderEntryDTO {
	@JsonProperty("entryID")
	private String entryID;

	@JsonProperty("productCode")
	private String productCode;

	@JsonProperty("image")
	private String image;

	@JsonProperty("quantity")
	private BigDecimal quantity;

	@JsonProperty("basePrice")
	private Double basePrice;

	@JsonProperty("totalPrice")
	private Double totalPrice;

	@JsonProperty("originalTotalPrice")
	private Double originalTotalPrice;

	@JsonProperty("taxValue")
	private Double taxValue;

	public String getEntryID() {
		return entryID;
	}

	public void setEntryID(final String entryID) {
		this.entryID = entryID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(final BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(final Double basePrice) {
		this.basePrice = basePrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(final Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTaxValue() {
		return taxValue;
	}

	public void setTaxValue(final Double taxValue) {
		this.taxValue = taxValue;
	}

	public Double getOriginalTotalPrice() {
		return originalTotalPrice;
	}

	public void setOriginalTotalPrice(final Double originalTotalPrice) {
		this.originalTotalPrice = originalTotalPrice;
	}
}
