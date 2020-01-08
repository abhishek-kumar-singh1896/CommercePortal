/**
 *
 */
package com.enterprisewide.b2badvance.core.dto;

/**
 * DTO Handle Price attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePriceCsvDTO extends B2BAdvancePDTCsvDTO
{

	private Double price;

	private boolean isNetPrice;

	private String salesUnit;

	private String userPriceGroup;

	public void setPrice(final Double price)
	{
		this.price = price;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setIsNetPrice(final boolean isNetPrice)
	{
		this.isNetPrice = isNetPrice;
	}

	public boolean isIsNetPrice()
	{
		return isNetPrice;
	}

	public void setSalesUnit(final String salesUnit)
	{
		this.salesUnit = salesUnit;
	}

	public String getSalesUnit()
	{
		return salesUnit;
	}

	public void setUserPriceGroup(final String userPriceGroup)
	{
		this.userPriceGroup = userPriceGroup;
	}

	public String getUserPriceGroup()
	{
		return userPriceGroup;
	}

}
