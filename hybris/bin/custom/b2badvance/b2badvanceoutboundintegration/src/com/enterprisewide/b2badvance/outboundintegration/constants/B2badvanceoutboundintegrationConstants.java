/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.enterprisewide.b2badvance.outboundintegration.constants;

import java.util.regex.Pattern;

/**
 * Global class for all B2badvanceoutboundintegration constants. You can add
 * global constants for your extension into this class.
 */
public final class B2badvanceoutboundintegrationConstants extends GeneratedB2badvanceoutboundintegrationConstants {
	public static final String EXTENSIONNAME = "b2badvanceoutboundintegration";

	private B2badvanceoutboundintegrationConstants() {
		// empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public static final String PLATFORM_LOGO_CODE = "b2badvanceoutboundintegrationPlatformLogo";
	public static final Pattern SUCCESS_RESPONSE_PATTERN = Pattern.compile("^2([0-9]{2}$)");

	public static final String SAP_ROOT_URL = "sap.root.url";

	public static final String SAP_USERNAME = "sap.username";

	public static final String SAP_PASSWORD = "sap.password";
}
