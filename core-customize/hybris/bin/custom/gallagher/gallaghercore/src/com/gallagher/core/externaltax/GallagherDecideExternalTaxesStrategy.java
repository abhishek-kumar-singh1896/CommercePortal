/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.externaltax;

import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;



/**
 * Base {@link DecideExternalTaxesStrategy} implementation, gives decision to call external taxes functionality.
 *
 * @author shishirkant
 */
public class GallagherDecideExternalTaxesStrategy implements DecideExternalTaxesStrategy
{

	/**
	 * Initially just to test if the delivery mode and address are set, than calculate the external taxes. (If External
	 * Tax is enabled in Base Store.)
	 *
	 * Products in cart, delivery mode and delivery address to determine whether or not to calculate
	 */
	@Override
	public boolean shouldCalculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		if (null != abstractOrder.getStore() && null != abstractOrder.getStore().getExternalTaxEnabled()
				&& Boolean.TRUE.equals(abstractOrder.getStore().getExternalTaxEnabled()))
		{
			return Boolean.TRUE.equals(abstractOrder.getNet()) && abstractOrder.getDeliveryMode() != null
					&& abstractOrder.getDeliveryAddress() != null;
		}

		return false;
	}
}
