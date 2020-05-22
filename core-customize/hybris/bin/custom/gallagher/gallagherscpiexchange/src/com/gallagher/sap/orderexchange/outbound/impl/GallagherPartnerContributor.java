/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultPartnerContributor;

import java.util.Map;

import com.gallagher.constants.GallagherPartnerCsvColumns;


/**
 *
 */
public class GallagherPartnerContributor extends DefaultPartnerContributor
{
	@Override
	protected Map<String, Object> mapAddressData(final OrderModel order, final AddressModel address)
	{
		final Map<String, Object> row = super.mapAddressData(order, address);
		row.put(GallagherPartnerCsvColumns.TAX_JURISDICTION_CODE, address.getGeoCode());
		return row;
	}
}
