/**
 *
 */
package com.gallagher.core.strategies;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Collections;
import java.util.List;

import com.sap.hybris.sapcustomerb2b.inbound.DefaultSAPB2BDeliveryAddressesLookupStrategy;


/**
 *
 */
public class GallagherSAPB2BDeliveryAddressesLookupStrategy extends DefaultSAPB2BDeliveryAddressesLookupStrategy
{
	@Override
	public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		// retrieve default delivery addresses for order
		final List<AddressModel> deliveryAddresses = getFallbackDeliveryAddressesLookupStrategy()
				.getDeliveryAddressesForOrder(abstractOrder, visibleAddressesOnly);

		// retrieve B2B customer of order
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) abstractOrder.getUser();

		// retrieve B2B unit of B2B customer
		final B2BUnitModel b2bUnit = super.getB2bUnitService().getParent(b2bCustomer);

		// retrieve delivery addresses for B2B unit
		final List<AddressModel> deliveryAddressesForB2BUnit = getDeliveryAddressesForB2BUnit(b2bUnit, visibleAddressesOnly);

		// merge delivery addresses for order and for B2B unit
		if (deliveryAddresses != null && !deliveryAddresses.isEmpty())
		{
			if (deliveryAddressesForB2BUnit != null && !deliveryAddressesForB2BUnit.isEmpty())
			{
				deliveryAddresses.addAll(deliveryAddressesForB2BUnit);
			}
		}
		else
		{
			if (deliveryAddressesForB2BUnit != null && !deliveryAddressesForB2BUnit.isEmpty())
			{
				return deliveryAddressesForB2BUnit;

			}
			else
			{
				return Collections.emptyList();
			}
		}

		return deliveryAddresses;
	}
}
