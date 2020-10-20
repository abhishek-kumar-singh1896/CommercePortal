/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.sap.sapcpicustomerexchangeb2b.outbound.services.impl.SapCpiB2BCustomerOutboundService;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.GallagherSCPIB2BCustomerConversionService;
import com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.GallagherSCPIB2BCustomerOutboundService;


/**
 * Class to enable sending outbound B2B Customer to SCPI.
 */
public class GallagherSCPIB2BCustomerOutboundServiceImpl extends SapCpiB2BCustomerOutboundService
		implements GallagherSCPIB2BCustomerOutboundService
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherSCPIB2BCustomerOutboundServiceImpl.class);



	/**
	 * Prepare and send outbound B2B Customer to SCPI.
	 *
	 * @param b2bCustomerModel
	 *           B2BCustomerModel
	 * @param language
	 *           String
	 */
	public void prepareAndSend(final B2BCustomerModel b2bCustomerModel, final String language, final List<String> addedUnits,
			final List<String> deletedUnits)
	{
		if (Objects.isNull(b2bCustomerModel.getDefaultB2BUnit()))
		{

			LOG.error("B2B customer [{}] cannot be replicated to SCPI because it is missing the default B2B unit!",
					b2bCustomerModel.getUid());
			return;

		}

		sendB2BCustomerToSCPI(((GallagherSCPIB2BCustomerConversionService) getSapCpiB2BCustomerConversionService())
				.convertB2BCustomerToSapCpiBb2BCustomer(b2bCustomerModel, language, addedUnits, deletedUnits));

	}
}
