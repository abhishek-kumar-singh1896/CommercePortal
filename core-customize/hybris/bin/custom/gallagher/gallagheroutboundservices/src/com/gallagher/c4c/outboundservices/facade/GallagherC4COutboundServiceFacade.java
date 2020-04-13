/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;


/**
 * Gallagher Facade to integrate with RESTful endpoint.
 *
 * @author shishirkant
 */
public interface GallagherC4COutboundServiceFacade
{
	/**
	 * Returns contact information for an email from C4C via SCPI
	 *
	 * @param customerEmail
	 *
	 * @return List of contact entries for the email provided
	 */
	List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(final String email);

	/**
	 * Send Registered Product Information to C4C via SCPI
	 *
	 * @param request
	 * @return status
	 */
	HttpStatus registerProduct(final RegisterProductRequest request);

	/**
	 * Returns Registered Product information for an email from C4C via SCPI
	 *
	 * @param customerEmail
	 *
	 * @return List of Registered Product for the email provided
	 */
	List<GallagherRegisteredProduct> getRegisteredProductFromC4C(final String email);
}
