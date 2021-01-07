/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;

import java.util.List;


/**
 * Class to enable sending outbound B2B Customer to SCPI.
 */
public interface GallagherSCPIB2BCustomerOutboundService
{

	/**
	 * Prepare and send outbound B2B Customer to SCPI.
	 *
	 * @param b2bCustomerModel
	 *           B2BCustomerModel
	 * @param language
	 *           String
	 */
	void prepareAndSend(final B2BCustomerModel b2bCustomerModel, final String language, final List<String> addedUnits,
			final List<String> deletedUnits);
}
