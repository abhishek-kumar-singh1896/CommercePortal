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

import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;


public class DefaultBraintreeAccessRightsService implements BraintreeAccessRightsService
{
	private static final String CSCOCKPIT_BRAINTREE_ACTIONS = "cscockpit.braintree";
	private static final String CSCOCKPIT_BRAINTREE_CUSTOMER_FIND = "cscockpit.braintree.customer.find";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_FIND = "cscockpit.braintree.transaction.find";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_CREATE = "cscockpit.braintree.transaction.create";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_CREATE_VAULT = "cscockpit.braintree.transaction.create.vault";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_VOID = "cscockpit.braintree.transaction.void";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_CLONE = "cscockpit.braintree.transaction.clone";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_REFUND = "cscockpit.braintree.transaction.refund";
	private static final String CSCOCKPIT_BRAINTREE_TRANSACTION_SUBMITFORSETTLEMENT = "cscockpit.braintree.transaction.submitforsettlement";


	private UIAccessRightService uiAccessRightService;

	private UserService userService;

	@Override
	public boolean isBraintreeActionsAllowed()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_ACTIONS);
	}

	@Override
	public boolean canFindCustomer()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_CUSTOMER_FIND);
	}

	@Override
	public boolean canFindTransaction()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_FIND);
	}

	@Override
	public boolean canCreateNewTransaction()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_CREATE);
	}

	@Override
	public boolean canCreateNewTransactionByVault()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_CREATE_VAULT);
	}

	@Override
	public boolean canVoid()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_VOID);
	}

	@Override
	public boolean canClone()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_CLONE);
	}

	@Override
	public boolean canRefund()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_REFUND);
	}

	@Override
	public boolean canSubmitForSettlement()
	{
		return isActionAllowed(CSCOCKPIT_BRAINTREE_TRANSACTION_SUBMITFORSETTLEMENT);
	}

	protected UserModel getCurrentUser()
	{
		return getUserService().getCurrentUser();
	}

	protected boolean isActionAllowed(final String actionCode)
	{
		final UserModel currentUser = getCurrentUser();
		final boolean canRead = getUiAccessRightService().canRead(currentUser, actionCode);
		final boolean canWrite = getUiAccessRightService().canWrite(currentUser, actionCode);

		return (canRead && canWrite);
	}

	/**
	 * @return the uiAccessRightService
	 */
	public UIAccessRightService getUiAccessRightService()
	{
		return uiAccessRightService;
	}


	/**
	 * @param uiAccessRightService
	 *           the uiAccessRightService to set
	 */
	public void setUiAccessRightService(final UIAccessRightService uiAccessRightService)
	{
		this.uiAccessRightService = uiAccessRightService;
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
