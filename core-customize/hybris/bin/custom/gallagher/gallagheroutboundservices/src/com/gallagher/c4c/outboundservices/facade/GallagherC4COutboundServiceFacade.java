/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade;

import java.util.List;

import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;


/**
 * Gallagher Facade to integrate with RESTful endpoint.
 */
public interface GallagherC4COutboundServiceFacade
{
	/**
	 * Returns contact information for an email from C4C via SCPI
	 *
	 * @author shishirkant
	 *
	 * @param email
	 *
	 * @return ContactInfo
	 */
	List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(String email);
}
