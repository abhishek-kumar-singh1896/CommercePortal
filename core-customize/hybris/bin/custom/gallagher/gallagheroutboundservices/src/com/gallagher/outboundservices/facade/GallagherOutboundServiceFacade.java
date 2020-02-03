/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.facade;

import com.gallagher.outboundservices.dto.inbound.customer.response.GallagherInboundCustomerInfo;


/**
*
*/
public interface GallagherOutboundServiceFacade
{
	GallagherInboundCustomerInfo getCustomerInfoFromC4C(String email);
}
