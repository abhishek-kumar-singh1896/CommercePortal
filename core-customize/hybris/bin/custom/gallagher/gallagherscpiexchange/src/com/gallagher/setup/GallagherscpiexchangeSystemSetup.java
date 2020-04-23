/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.setup;

import static com.gallagher.constants.GallagherscpiexchangeConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.gallagher.constants.GallagherscpiexchangeConstants;
import com.gallagher.service.GallagherscpiexchangeService;


@SystemSetup(extension = GallagherscpiexchangeConstants.EXTENSIONNAME)
public class GallagherscpiexchangeSystemSetup
{
	private final GallagherscpiexchangeService gallagherscpiexchangeService;

	public GallagherscpiexchangeSystemSetup(final GallagherscpiexchangeService gallagherscpiexchangeService)
	{
		this.gallagherscpiexchangeService = gallagherscpiexchangeService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gallagherscpiexchangeService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return GallagherscpiexchangeSystemSetup.class.getResourceAsStream("/gallagherscpiexchange/sap-hybris-platform.png");
	}
}
