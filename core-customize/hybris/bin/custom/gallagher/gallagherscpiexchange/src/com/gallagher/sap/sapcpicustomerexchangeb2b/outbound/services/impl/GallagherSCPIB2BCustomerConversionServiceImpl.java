/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundB2BContactModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundB2BCustomerModel;
import de.hybris.platform.sap.sapcpicustomerexchangeb2b.outbound.services.impl.SapCpiB2BCustomerDefaultConversionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gallagher.sap.sapcpicustomerexchangeb2b.outbound.services.GallagherSCPIB2BCustomerConversionService;


/**
 * Gallagher class to convert Hybris B2B Customer to SCPI B2B Customer.
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIB2BCustomerConversionServiceImpl extends SapCpiB2BCustomerDefaultConversionService
		implements GallagherSCPIB2BCustomerConversionService
{

	/**
	 * {@inheritDoc} Added keycloakguid, contactId and ObjectID
	 */
	protected void mapB2BContactInfo(final B2BCustomerModel b2bCustomer, final String sessionLanguage,
			final SAPCpiOutboundB2BContactModel sapCpiOutboundB2BContact, final List<String> addedUnits,
			final List<String> deletedUnits)
	{
		super.mapB2BContactInfo(b2bCustomer, sessionLanguage, sapCpiOutboundB2BContact);
		sapCpiOutboundB2BContact.setKeycloakGUID(b2bCustomer.getKeycloakGUID());
		sapCpiOutboundB2BContact.setContactId(b2bCustomer.getSapContactID());
		sapCpiOutboundB2BContact.setObjectID(b2bCustomer.getObjectID());
		sapCpiOutboundB2BContact.setUnits(getUnits(b2bCustomer));
		sapCpiOutboundB2BContact.setAddedUnits(addedUnits);
		sapCpiOutboundB2BContact.setDeletedUnits(deletedUnits);
		sapCpiOutboundB2BContact.setUid(b2bCustomer.getEmailID());
	}

	/**
	 * Returns the uids of all B2B Units
	 *
	 * @return b2bunit Ids
	 */
	private List<String> getUnits(final B2BCustomerModel b2bCustomer)
	{
		final List<String> groups = new ArrayList<String>();
		for (final PrincipalGroupModel group : b2bCustomer.getGroups())
		{
			if (group instanceof B2BUnitModel && !group.equals(b2bCustomer.getDefaultB2BUnit()))
			{
				groups.add(group.getUid());
			}
		}
		return groups;
	}

	/**
	 * Overriding default functionality to avoid populating logical system information
	 */
	@Override
	protected void mapOutboundDestination(final SAPCpiOutboundB2BCustomerModel sapCpiOutboundB2BCustomer)
	{
		//Nothing to do as no outbound details required for current SCPI integration
	}

	@Override
	public SAPCpiOutboundB2BCustomerModel convertB2BCustomerToSapCpiBb2BCustomer(final B2BCustomerModel b2bCustomerModel,
			final String sessionLanguage, final List<String> addedUnits, final List<String> deletedUnits)
	{

		final SAPCpiOutboundB2BCustomerModel sapCpiOutboundB2BCustomer = new SAPCpiOutboundB2BCustomerModel();

		// Hybris B2B Unit Maps To SAP B2B Customer
		final B2BUnitModel rootB2BUnit = getB2bUnitService().getRootUnit(b2bCustomerModel.getDefaultB2BUnit());
		sapCpiOutboundB2BCustomer.setUid(rootB2BUnit.getUid());
		sapCpiOutboundB2BCustomer.setAddressUUID(readSapAddressUUID(rootB2BUnit));
		mapOutboundDestination(sapCpiOutboundB2BCustomer);

		// Hybris B2B Customers Maps To SAP B2B Contacts
		final Set<SAPCpiOutboundB2BContactModel> sapCpiOutboundB2BContacts = new HashSet<>();

		sapCpiOutboundB2BContacts
				.add(convertB2BContactToSapCpiBb2BContact(rootB2BUnit, b2bCustomerModel, sessionLanguage, addedUnits, deletedUnits));
		sapCpiOutboundB2BCustomer.setSapCpiOutboundB2BContacts(sapCpiOutboundB2BContacts);

		return sapCpiOutboundB2BCustomer;

	}

	protected SAPCpiOutboundB2BContactModel convertB2BContactToSapCpiBb2BContact(final B2BUnitModel b2bUnitModel,
			final B2BCustomerModel b2bCustomerModel, final String sessionLanguage, final List<String> addedUnits,
			final List<String> deletedUnits)
	{

		final SAPCpiOutboundB2BContactModel sapCpiOutboundB2BContact = new SAPCpiOutboundB2BContactModel();
		sapCpiOutboundB2BContact.setUid(b2bUnitModel.getUid());

		mapB2BContactInfo(b2bCustomerModel, sessionLanguage, sapCpiOutboundB2BContact, addedUnits, deletedUnits);

		return sapCpiOutboundB2BContact;

	}

}
