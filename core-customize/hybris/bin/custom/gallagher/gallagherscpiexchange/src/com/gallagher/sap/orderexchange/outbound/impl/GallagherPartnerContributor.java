/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultPartnerContributor;

import java.util.Map;

import javax.annotation.Resource;

import com.gallagher.constants.GallagherPartnerCsvColumns;


/**
 * Custom contributor to populate the Partner info like addresses etc.
 * 
 * @author Vikram BishnoiF
 */
public class GallagherPartnerContributor extends DefaultPartnerContributor
{
	@Resource
	private CustomerEmailResolutionService customerEmailResolutionService;

	@Override
	protected Map<String, Object> mapAddressData(final OrderModel order, final AddressModel address)
	{
		final Map<String, Object> row = super.mapAddressData(order, address);
		row.put(GallagherPartnerCsvColumns.TAX_JURISDICTION_CODE, address.getGeoCode());
		if (address.getEmail() == null)
		{
			final String email = customerEmailResolutionService.getEmailForCustomer(((CustomerModel) order.getUser()));
			row.put(PartnerCsvColumns.EMAIL, email);
		}
		return row;
	}
}
