/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade;

import java.util.List;

import com.gallagher.outboundservices.dto.inbound.customer.response.GallagherInboundCustomerEntry;


/**
 * Gallagher Facade to integrate with RESTful endpoint.
 */
public interface GallagherC4COutboundServiceFacade
{
	List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(String email);
}
