/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.urlencoder.attributes.impl;


import de.hybris.platform.acceleratorservices.urlencoder.attributes.impl.AbstractUrlEncodingAttributeManager;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnProcessModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;


/**
 * Default implementation for country attribute handler.
 *
 */
public class GallagherCountryAttributeManager extends AbstractUrlEncodingAttributeManager
{
	@Override
	public Collection<String> getAllAvailableValues()
	{
		return CollectionUtils.collect(getCmsSiteService().getCurrentSite().getStores().get(0).getDeliveryCountries(),
				new Transformer()
				{
					@Override
					public Object transform(final Object object)
					{
						return ((CountryModel) object).getIsocode().toLowerCase();
					}
				});
	}

	@Override
	public void updateAndSyncForAttrChange(final String value)
	{
		//Do nothing for Storefront attribute
	}

	@Override
	public String getDefaultValue()
	{
		return getCmsSiteService().getCurrentSite().getStores().get(0).getDeliveryCountries().iterator().next().getIsocode()
				.toLowerCase();
	}

	@Override
	public String getCurrentValue()
	{
		return getCmsSiteService().getCurrentSite().getStores().get(0).getDeliveryCountries().iterator().next().getIsocode()
				.toLowerCase();
	}

	@Override
	public String getAttributeValueForEmail(final BusinessProcessModel businessProcessModel)
	{
		if (businessProcessModel instanceof StoreFrontCustomerProcessModel)
		{
			return ((StoreFrontCustomerProcessModel) businessProcessModel).getStore().getDeliveryCountries().iterator().next()
					.getIsocode().toLowerCase();
		}
		else if (businessProcessModel instanceof OrderProcessModel)
		{
			return ((OrderProcessModel) businessProcessModel).getOrder().getStore().getDeliveryCountries().iterator().next()
					.getIsocode().toLowerCase();
		}
		else if (businessProcessModel instanceof ConsignmentProcessModel)
		{
			return ((ConsignmentProcessModel) businessProcessModel).getConsignment().getOrder().getStore().getDeliveryCountries()
					.iterator().next().getIsocode().toLowerCase();
		}
		else if (businessProcessModel instanceof ReturnProcessModel)
		{
			return ((ReturnProcessModel) businessProcessModel).getReturnRequest().getOrder().getStore().getDeliveryCountries()
					.iterator().next().getIsocode().toLowerCase();
		}
		return getDefaultValue();
	}
}
