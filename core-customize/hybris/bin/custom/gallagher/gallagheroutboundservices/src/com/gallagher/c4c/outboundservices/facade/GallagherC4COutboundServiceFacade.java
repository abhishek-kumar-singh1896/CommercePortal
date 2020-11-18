/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade;

import java.util.List;

import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;
import com.gallagher.outboundservices.response.dto.GallagherRegisterProductErrorResponse;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;


/**
 * Gallagher Facade to integrate with RESTful endpoint.
 *
 * @author shishirkant
 */
public interface GallagherC4COutboundServiceFacade
{
	/**
	 * Returns contact information for an keycloakGUID/email from C4C via SCPI
	 *
	 * @param customerEmail
	 * @param keycloakGUID
	 *           guid returned by keycloak
	 *
	 * @return List of contact entries for the email/keycloakGUID provided
	 */
	/* List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(final String email, final String keycloakGUID); */


	List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(final String email, final String keycloakGUID,
			final String businessUnit);


	/**
	 * Send Registered Product Information to C4C via SCPI
	 *
	 * @param request
	 * @return status
	 */
	GallagherRegisterProductErrorResponse registerProduct(final RegisterProductRequest request);

	/**
	 * Returns Registered Product information for an email from C4C via SCPI
	 *
	 * @param customerID
	 * @param accountID
	 *           Sap account ID
	 *
	 * @return List of Registered Product for the customerID provided
	 */
	List<GallagherRegisteredProduct> getRegisteredProductFromC4C(final String customerID, String accountID);
}
