/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.externaltax;

import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;



/**
 * Base {@link DecideExternalTaxesStrategy} implementation, gives decision to call external taxes functionality.
 */
public class GallagherDecideExternalTaxesStrategy implements DecideExternalTaxesStrategy
{

	/**
	 * Return true to be overridden in specific application
	 */
	@Override
	public boolean shouldCalculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		return true;
	}
}
