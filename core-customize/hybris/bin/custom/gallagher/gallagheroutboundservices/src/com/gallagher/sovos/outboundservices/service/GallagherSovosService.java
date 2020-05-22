/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sovos.outboundservices.service;

import de.hybris.platform.core.model.user.AddressModel;

import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxResponse;


/**
 * Gallagher Service to integrate with Sovos REST APIs for Tax Calculation.
 *
 * @author shishirkant
 */
public interface GallagherSovosService
{

	/**
	 * Return calculated tax for an Abstract Order as Json Response.
	 *
	 * @param GallagherSovosCalculateTaxRequest
	 *
	 * @return GallagherSovosCalculatedTaxResponse
	 */
	GallagherSovosCalculatedTaxResponse calculateExternalTax(final GallagherSovosCalculateTaxRequest request);

	/**
	 * Return geoCode as Json Response
	 *
	 * @param AddressModel
	 *
	 * @return String
	 */
	String getGeoCode(final AddressModel address);
}
