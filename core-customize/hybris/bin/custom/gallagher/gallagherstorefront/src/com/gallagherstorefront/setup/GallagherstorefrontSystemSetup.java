/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagherstorefront.setup;

import static com.gallagherstorefront.constants.GallagherstorefrontConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.gallagherstorefront.constants.GallagherstorefrontConstants;
import com.gallagherstorefront.service.GallagherstorefrontService;


@SystemSetup(extension = GallagherstorefrontConstants.EXTENSIONNAME)
public class GallagherstorefrontSystemSetup
{
	private final GallagherstorefrontService gallagherstorefrontService;

	public GallagherstorefrontSystemSetup(final GallagherstorefrontService gallagherstorefrontService)
	{
		this.gallagherstorefrontService = gallagherstorefrontService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gallagherstorefrontService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return GallagherstorefrontSystemSetup.class.getResourceAsStream("/gallagherstorefront/sap-hybris-platform.png");
	}
}
