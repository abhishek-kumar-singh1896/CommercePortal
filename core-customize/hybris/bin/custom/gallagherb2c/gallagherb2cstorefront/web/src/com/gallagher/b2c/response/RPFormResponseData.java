/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.gallagher.b2c.response;

import java.util.Map;

import com.gallagher.b2c.form.RegisterProductForm;


/**
 * Ajax based form submissions response data
 */
public class RPFormResponseData
{

	/**
	 * Status of the result
	 */
	private RPFormResponseStatus responseStatus;

	private Map<String, String> errorsMap;

	private String productCode;

	private String productName;

	private String productImage;

	private String productaltText;

	private RegisterProductForm registerProductForm;

	/**
	 * @return the registerProductForm
	 */
	public RegisterProductForm getRegisterProductForm()
	{
		return registerProductForm;
	}

	/**
	 * @param registerProductForm
	 *           the registerProductForm to set
	 */
	public void setRegisterProductForm(final RegisterProductForm registerProductForm)
	{
		this.registerProductForm = registerProductForm;
	}

	/**
	 * @return the productaltText
	 */
	public String getProductaltText()
	{
		return productaltText;
	}

	/**
	 * @param productaltText
	 *           the productaltText to set
	 */
	public void setProductaltText(final String productaltText)
	{
		this.productaltText = productaltText;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the productName
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * @param productName
	 *           the productName to set
	 */
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

	/**
	 * @return the productImage
	 */
	public String getProductImage()
	{
		return productImage;
	}

	/**
	 * @param productImage
	 *           the productImage to set
	 */
	public void setProductImage(final String productImage)
	{
		this.productImage = productImage;
	}

	public Map<String, String> getErrorsMap()
	{
		return errorsMap;
	}

	public void setErrorsMap(final Map<String, String> errorsMap)
	{
		this.errorsMap = errorsMap;
	}


	public RPFormResponseStatus getResponseStatus()
	{
		return responseStatus;
	}

	public void setResponseStatus(final RPFormResponseStatus responseStatus)
	{
		this.responseStatus = responseStatus;
	}
}
