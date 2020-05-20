/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sapcustomerb2c.outbound;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Set;

import com.sap.hybris.sapcustomerb2c.outbound.DefaultCustomerInterceptor;


/**
 *
 *
 */
public class GallagherCustomerInterceptor extends DefaultCustomerInterceptor
{

	@Override
	protected Set<String> getMonitoredAttributes()
	{

		final Set<String> monitoredAttributes = super.getMonitoredAttributes();
		monitoredAttributes.add(CustomerModel.NEWSLETTERS);
		monitoredAttributes.add(CustomerModel.EVENTS);
		monitoredAttributes.add(CustomerModel.PRODUCTRELEASE);
		monitoredAttributes.add(CustomerModel.PRODUCTUPDATE);
		monitoredAttributes.add(CustomerModel.PRODUCTPROMO);

		return monitoredAttributes;

	}
}
