/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundB2BContactModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundB2BCustomerModel;
import de.hybris.platform.sap.sapcpicustomerexchangeb2b.outbound.services.impl.SapCpiB2BCustomerDefaultConversionService;


/**
 * Gallagher class to convert Hybris B2B Customer to SCPI B2B Customer.
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIB2BCustomerConversionServiceImpl extends SapCpiB2BCustomerDefaultConversionService
{

	/**
	 * {@inheritDoc} Added keycloakguid, contactId and ObjectID
	 */
	@Override
	protected void mapB2BContactInfo(final B2BCustomerModel b2bCustomer, final String sessionLanguage,
			final SAPCpiOutboundB2BContactModel sapCpiOutboundB2BContact)
	{
		super.mapB2BContactInfo(b2bCustomer, sessionLanguage, sapCpiOutboundB2BContact);
		sapCpiOutboundB2BContact.setKeycloakGUID(b2bCustomer.getKeycloakGUID());
		sapCpiOutboundB2BContact.setContactId(b2bCustomer.getSapContactID());
		sapCpiOutboundB2BContact.setObjectID(b2bCustomer.getObjectID());
	}

	/**
	 * Overriding default functionality to avoid populating logical system information
	 */
	@Override
	protected void mapOutboundDestination(final SAPCpiOutboundB2BCustomerModel sapCpiOutboundB2BCustomer)
	{
		//Nothing to do as no outbound details required for current SCPI integration
	}
}