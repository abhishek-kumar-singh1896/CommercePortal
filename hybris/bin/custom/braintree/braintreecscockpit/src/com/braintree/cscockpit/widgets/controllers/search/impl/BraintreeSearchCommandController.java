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
package com.braintree.cscockpit.widgets.controllers.search.impl;

import de.hybris.platform.cscockpit.services.search.CsSearchResult;
import de.hybris.platform.cscockpit.widgets.controllers.search.impl.DefaultSearchCommandController;

import java.util.Collections;
import java.util.List;


public class BraintreeSearchCommandController extends DefaultSearchCommandController
{
	@Override
	public List getCurrentPageResults()
	{
		final CsSearchResult result = this.getSearchResult();
		if (result != null)
		{
			return result.getResult();
		}
		return Collections.emptyList();
	}
}
