/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.constants;

/**
 * Constants for Payment CSV Columns
 */
public class GallagherPaymentCsvColumns
{
	private GallagherPaymentCsvColumns()
	{
		throw new IllegalStateException("Utility class");
	}

	@SuppressWarnings("javadoc")
	public static final String AMOUNT = "amount";

	@SuppressWarnings("javadoc")
	public static final String AUTHORIZATION_TIME = "authorizationTime";

	@SuppressWarnings("javadoc")
	public static final String AUTHORIZATION_NUMBER = "authorizationNumber";

	@SuppressWarnings("javadoc")
	public static final String RESULT_TEXT = "resultText";

	@SuppressWarnings("javadoc")
	public static final String MERCHANT_ID = "merchantID";

	@SuppressWarnings("javadoc")
	public static final String AVS_CODE = "avsCode";
}
