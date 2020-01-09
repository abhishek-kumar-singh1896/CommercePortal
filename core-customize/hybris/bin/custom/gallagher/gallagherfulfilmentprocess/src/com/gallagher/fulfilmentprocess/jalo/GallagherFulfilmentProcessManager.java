/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.gallagher.fulfilmentprocess.constants.GallagherFulfilmentProcessConstants;

public class GallagherFulfilmentProcessManager extends GeneratedGallagherFulfilmentProcessManager
{
	public static final GallagherFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GallagherFulfilmentProcessManager) em.getExtension(GallagherFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
