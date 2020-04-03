/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sovos.outboundservices.service;

import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxResponse;


/**
 *
 */
public interface GallagherSovosService
{

	/**
	 * @param GallagherSovosCalculateTaxRequest
	 *
	 * @return GallagherSovosCalculatedTaxResponse
	 */
	GallagherSovosCalculatedTaxResponse calculateExternalTax(final GallagherSovosCalculateTaxRequest request);

}
