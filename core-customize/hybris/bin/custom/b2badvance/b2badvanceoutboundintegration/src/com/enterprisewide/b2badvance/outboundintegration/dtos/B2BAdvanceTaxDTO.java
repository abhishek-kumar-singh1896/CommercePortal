package com.enterprisewide.b2badvance.outboundintegration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds the tax details
 * 
 * @author Enterprise Wide
 */
@JsonInclude(Include.NON_NULL)
public class B2BAdvanceTaxDTO {
	@JsonProperty("id")
	private String id;

	@JsonProperty("percentage")
	private Double percentage;

	@JsonProperty("total")
	private Double total;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the percentage
	 */
	public Double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(final Double percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(final Double total) {
		this.total = total;
	}
}
