/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.enterprisewide.b2badvance.outboundintegration.setup;

import static com.enterprisewide.b2badvance.outboundintegration.constants.B2badvanceoutboundintegrationConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.enterprisewide.b2badvance.outboundintegration.constants.B2badvanceoutboundintegrationConstants;
import com.enterprisewide.b2badvance.outboundintegration.service.B2badvanceoutboundintegrationService;


@SystemSetup(extension = B2badvanceoutboundintegrationConstants.EXTENSIONNAME)
public class B2badvanceoutboundintegrationSystemSetup
{
	private final B2badvanceoutboundintegrationService b2badvanceoutboundintegrationService;

	public B2badvanceoutboundintegrationSystemSetup(final B2badvanceoutboundintegrationService b2badvanceoutboundintegrationService)
	{
		this.b2badvanceoutboundintegrationService = b2badvanceoutboundintegrationService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		b2badvanceoutboundintegrationService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return B2badvanceoutboundintegrationSystemSetup.class.getResourceAsStream("/b2badvanceoutboundintegration/sap-hybris-platform.png");
	}
}
