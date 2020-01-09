/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.gallagher.core.constants.GallagherCoreConstants;
import com.gallagher.core.setup.CoreSystemSetup;


/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
public class GallagherCoreManager extends GeneratedGallagherCoreManager
{
	public static final GallagherCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GallagherCoreManager) em.getExtension(GallagherCoreConstants.EXTENSIONNAME);
	}
}
