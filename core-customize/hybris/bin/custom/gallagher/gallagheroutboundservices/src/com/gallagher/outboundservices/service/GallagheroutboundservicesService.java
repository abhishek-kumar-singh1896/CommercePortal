/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.service;

import com.gallagher.outboundservices.request.dto.RegisterProductRequest;


public interface GallagheroutboundservicesService
{
	String getHybrisLogoUrl(String logoCode);

	void createLogo(String logoCode);

	void postRegisterProduct(RegisterProductRequest productRequest);
}
