/**
 *
 */
package com.enterprisewide.b2badvance.core.dto;

/**
 * DTO Handle Discount attributes
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceDiscountCsvDTO extends B2BAdvancePDTCsvDTO
{

	private Double discount;

	private String discountType;

	private String discountCode;

	private String userDiscountGroup;

	public void setDiscount(final Double discount)
	{
		this.discount = discount;
	}

	public Double getDiscount()
	{
		return discount;
	}

	public void setDiscountType(final String discountType)
	{
		this.discountType = discountType;
	}

	public String getDiscountType()
	{
		return discountType;
	}

	public void setDiscountCode(final String discountCode)
	{
		this.discountCode = discountCode;
	}

	public String getDiscountCode()
	{
		return discountCode;
	}

	public void setUserDiscountGroup(final String userDiscountGroup)
	{
		this.userDiscountGroup = userDiscountGroup;
	}

	public String getUserDiscountGroup()
	{
		return userDiscountGroup;
	}
}
