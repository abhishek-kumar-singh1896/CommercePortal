/**
 *
 */
package com.gallagher.core.dtos;

/**
 * DTO to hold the merged tax values of Sovos
 *
 * @author Vikram Bishnoi
 */
public class GallagherSovosMergedTaxDTO
{
	private Double taxRate;
	private Double taxAmount;

	public Double getTaxRate()
	{
		return taxRate;
	}

	public void setTaxRate(final Double taxRate)
	{
		this.taxRate = taxRate;
	}

	public Double getTaxAmount()
	{
		return taxAmount;
	}

	public void setTaxAmount(final Double taxAmount)
	{
		this.taxAmount = taxAmount;
	}
}
