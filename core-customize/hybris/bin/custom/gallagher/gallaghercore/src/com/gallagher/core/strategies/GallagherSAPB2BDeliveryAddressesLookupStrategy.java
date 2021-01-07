/**
 *
 */
package com.gallagher.core.strategies;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.services.GallagherB2BUnitService;
import com.sap.hybris.sapcustomerb2b.inbound.DefaultSAPB2BDeliveryAddressesLookupStrategy;


/**
 *
 */
public class GallagherSAPB2BDeliveryAddressesLookupStrategy extends DefaultSAPB2BDeliveryAddressesLookupStrategy
{
	@Autowired
	private SessionService sessionService;

	@Resource(name = "gallagherB2BUnitService")
	protected GallagherB2BUnitService gallagherB2bUnitService;

	@Override
	public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		// retrieve default delivery addresses for order
		List<AddressModel> deliveryAddresses = getFallbackDeliveryAddressesLookupStrategy()
				.getDeliveryAddressesForOrder(abstractOrder, visibleAddressesOnly);

		// retrieve B2B customer of order
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) abstractOrder.getUser();

		// retrieve B2B unit of B2B customer
		final List<B2BUnitModel> b2bUnits = gallagherB2bUnitService.getAllB2BData(b2bCustomer);
		final List<AddressModel> deliveryAddressesForB2BUnit = new ArrayList<>();

		for (final B2BUnitModel b2bUnit : b2bUnits)
		{
			// retrieve delivery addresses for B2B unit
			final List<AddressModel> addressesForB2BUnit = getDeliveryAddressesForB2BUnit(b2bUnit, visibleAddressesOnly);
			addressesForB2BUnit.stream().forEach(add -> deliveryAddressesForB2BUnit.add(add));
		}
		// merge delivery addresses for order and for B2B unit
		if (CollectionUtils.isNotEmpty(deliveryAddresses))
		{
			if (CollectionUtils.isNotEmpty(deliveryAddressesForB2BUnit))
			{
				deliveryAddressesForB2BUnit.addAll(deliveryAddresses);
				deliveryAddresses = deliveryAddressesForB2BUnit;
			}
		}
		else
		{
			if (CollectionUtils.isNotEmpty(deliveryAddressesForB2BUnit))
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

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}
