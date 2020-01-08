/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.braintree.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.braintree.constants.Braintree61fulfilmentprocessConstants;

@SuppressWarnings("PMD")
public class Braintree61fulfilmentprocessManager extends GeneratedBraintree61fulfilmentprocessManager
{
	public static final Braintree61fulfilmentprocessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Braintree61fulfilmentprocessManager) em.getExtension(Braintree61fulfilmentprocessConstants.EXTENSIONNAME);
	}
	
}
