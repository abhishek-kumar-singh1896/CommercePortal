/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.enterprisewide.b2badvance.outboundintegration.service;

public interface B2badvanceoutboundintegrationService
{
	String getHybrisLogoUrl(String logoCode);

	void createLogo(String logoCode);
}
