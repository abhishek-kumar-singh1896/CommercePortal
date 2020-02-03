/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.service;

public interface GallagheroutboundservicesService
{
	String getHybrisLogoUrl(String logoCode);

	void createLogo(String logoCode);
}
