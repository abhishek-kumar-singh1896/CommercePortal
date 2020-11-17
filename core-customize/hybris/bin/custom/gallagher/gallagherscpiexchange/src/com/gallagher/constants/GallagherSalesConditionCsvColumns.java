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
 * Constants for Sales Conditions CSV Columns
 */
public class GallagherSalesConditionCsvColumns
{
	private GallagherSalesConditionCsvColumns() {
		throw new IllegalStateException("Utility class");
	}

	@SuppressWarnings("javadoc")
	public static final String TAX_JURISDICTION_CODE = "taxJurisdictionCode";

	@SuppressWarnings("javadoc")
	public static final String CONDITION_TOTAL_AMOUNT = "totalAmt";
}