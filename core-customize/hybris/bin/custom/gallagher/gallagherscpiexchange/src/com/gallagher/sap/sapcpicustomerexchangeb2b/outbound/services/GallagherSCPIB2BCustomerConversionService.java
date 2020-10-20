/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundB2BCustomerModel;
import de.hybris.platform.sap.sapcpicustomerexchangeb2b.outbound.services.SapCpiB2BCustomerConversionService;

import java.util.List;


/**
 * Gallagher class to convert Hybris B2B Customer to SCPI B2B Customer.
 *
 * @author Vikram Bishnoi
 */
public interface GallagherSCPIB2BCustomerConversionService extends SapCpiB2BCustomerConversionService
{
	/**
	 * Convert Hybris B2B Customer to SCPI B2B Customers.
	 *
	 * @param b2bCustomerModel
	 *           B2BCustomerModel
	 * @param sessionLanguage
	 *           String
	 * @return SAPCpiOutboundB2BCustomerModel
	 */
	SAPCpiOutboundB2BCustomerModel convertB2BCustomerToSapCpiBb2BCustomer(B2BCustomerModel b2bCustomerModel,
			String sessionLanguage, final List<String> addedUnits, final List<String> deletedUnits);

}
