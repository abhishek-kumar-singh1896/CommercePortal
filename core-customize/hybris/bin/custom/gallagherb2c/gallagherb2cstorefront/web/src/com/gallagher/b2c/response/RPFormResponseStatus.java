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

/**
 * Enum for the response status of ajax based form submissions
 */
public enum RPFormResponseStatus
{

	/**
	 * Enum value for form submission failures.
	 */
	FAILURE("failure"),

	SUCCESS("success"),

	PRODUCTNOTFOUND("productnotfound");

	/**
	 * Code for the enum value
	 */
	private final String code;

	/**
	 * Constructor for the enum values with the code
	 *
	 * @param code
	 */
	private RPFormResponseStatus(final String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return this.code;
	}
}
