/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.setup;

import static com.gallagher.outboundservices.constants.GallagheroutboundservicesConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.gallagher.outboundservices.constants.GallagheroutboundservicesConstants;
import com.gallagher.outboundservices.service.GallagheroutboundservicesService;


@SystemSetup(extension = GallagheroutboundservicesConstants.EXTENSIONNAME)
public class GallagheroutboundservicesSystemSetup
{
	private final GallagheroutboundservicesService gallagheroutboundservicesService;

	public GallagheroutboundservicesSystemSetup(final GallagheroutboundservicesService gallagheroutboundservicesService)
	{
		this.gallagheroutboundservicesService = gallagheroutboundservicesService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		gallagheroutboundservicesService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return GallagheroutboundservicesSystemSetup.class.getResourceAsStream("/gallagheroutboundservices/sap-hybris-platform.png");
	}
}
