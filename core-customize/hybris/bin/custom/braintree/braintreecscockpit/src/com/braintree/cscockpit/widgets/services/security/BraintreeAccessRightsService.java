/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.services.security;

public interface BraintreeAccessRightsService
{

	boolean isBraintreeActionsAllowed();

	boolean canFindCustomer();

	boolean canFindTransaction();

	boolean canCreateNewTransaction();

	boolean canCreateNewTransactionByVault();

	boolean canVoid();

	boolean canClone();

	boolean canRefund();

	boolean canSubmitForSettlement();

}
