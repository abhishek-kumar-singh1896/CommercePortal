/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.enterprisewide.b2badvance.fulfilmentprocess.constants.B2badvanceFulfilmentProcessConstants;

public class B2badvanceFulfilmentProcessManager extends GeneratedB2badvanceFulfilmentProcessManager
{
	public static final B2badvanceFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2badvanceFulfilmentProcessManager) em.getExtension(B2badvanceFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
