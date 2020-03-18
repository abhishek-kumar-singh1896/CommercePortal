/**
 *
 */
package com.gallagher.b2c.forms;

/**
 * @author shilpiverma
 *
 */
public class RegisterProductForm
{
	private String productSku;
	private Integer serialNumber;
	private String datePurchased;
	private String addressLine1;
	private String addressLine2;
	private String townCity;
	private String postCode;
	private String country;
	private String phoneNumber;

	/**
	 * @return the productSku
	 */
	public String getProductSku()
	{
		return productSku;
	}

	/**
	 * @param productSku
	 *           the productSku to set
	 */
	public void setProductSku(final String productSku)
	{
		this.productSku = productSku;
	}

	/**
	 * @return the serialNumber
	 */
	public Integer getSerialNumber()
	{
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *           the serialNumber to set
	 */
	public void setSerialNumber(final Integer serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the datePurchased
	 */
	public String getDatePurchased()
	{
		return datePurchased;
	}

	/**
	 * @param datePurchased
	 *           the datePurchased to set
	 */
	public void setDatePurchased(final String datePurchased)
	{
		this.datePurchased = datePurchased;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *           the addressLine1 to set
	 */
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *           the addressLine2 to set
	 */
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the townCity
	 */
	public String getTownCity()
	{
		return townCity;
	}

	/**
	 * @param townCity
	 *           the townCity to set
	 */
	public void setTownCity(final String townCity)
	{
		this.townCity = townCity;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode()
	{
		return postCode;
	}

	/**
	 * @param postCode
	 *           the postCode to set
	 */
	public void setPostCode(final String postCode)
	{
		this.postCode = postCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *           the phoneNumber to set
	 */
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
}
