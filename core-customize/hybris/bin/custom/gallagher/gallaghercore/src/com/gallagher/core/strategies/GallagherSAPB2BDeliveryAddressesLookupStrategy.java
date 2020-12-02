/**
 *
 */
package com.gallagher.core.strategies;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.hybris.sapcustomerb2b.inbound.DefaultSAPB2BDeliveryAddressesLookupStrategy;


/**
 *
 */
public class GallagherSAPB2BDeliveryAddressesLookupStrategy extends DefaultSAPB2BDeliveryAddressesLookupStrategy
{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		// retrieve default delivery addresses for order
		final List<AddressModel> deliveryAddresses = getFallbackDeliveryAddressesLookupStrategy()
				.getDeliveryAddressesForOrder(abstractOrder, visibleAddressesOnly);

		final List<AddressModel> addresses = new ArrayList<>();

		// retrieve B2B customer of order
		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) abstractOrder.getUser();

		// retrieve B2B unit of B2B customer
		final Set<B2BUnitModel> b2bUnits = getAllB2BData(b2bCustomer);
		final List<AddressModel> deliveryAddressesForB2BUnit = new ArrayList<>();

		for (final B2BUnitModel b2bUnit : b2bUnits)
		{
			// retrieve delivery addresses for B2B unit
			final List<AddressModel> addressesForB2BUnit = getDeliveryAddressesForB2BUnit(b2bUnit, visibleAddressesOnly);
			addressesForB2BUnit.stream().forEach(add -> deliveryAddressesForB2BUnit.add(add));
		}
		// merge delivery addresses for order and for B2B unit
		if (deliveryAddresses != null && !deliveryAddresses.isEmpty())
		{
			if (CollectionUtils.isNotEmpty(deliveryAddressesForB2BUnit))
			{
				addresses.addAll(Stream.of(deliveryAddressesForB2BUnit, deliveryAddresses).flatMap(Collection::stream)
						.collect(Collectors.toList()));
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

		return addresses;
	}

	public Set<B2BUnitModel> getAllB2BData(final B2BCustomerModel b2bCustomer)
	{
		int i = 0;
		boolean flag = false;
		String selectedUnit = null;
		final B2BUnitModel unitModel = getSessionService().getAttribute("selectedB2BUnit");
		if (unitModel != null)
		{
			selectedUnit = unitModel.getUid();
		}
		final List<B2BUnitModel> rootNodes = getAllB2BUnits(b2bCustomer);
		;
		final Set<PrincipalGroupModel> customerGroups = b2bCustomer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		if (rootNodes != null && rootNodes.size() == 0)//If Units From Same Hirearachy then add all the units to display in dropdown,since popup doesn't appear
		{
			final B2BUnitModel defaultUnit = b2bCustomer.getDefaultB2BUnit();
			if (defaultUnit != null)
			{
				defaultUnit.getUid();
			}
			for (final PrincipalGroupModel group : customerGroups)
			{
				if (group instanceof B2BUnitModel && group.equals(defaultUnit))
				{
					flag = true;
					b2bUnitList.add(0, ((B2BUnitModel) group));
				}
				else if (group instanceof B2BUnitModel)
				{
					if (flag)
					{
						b2bUnitList.add(++i, ((B2BUnitModel) group));
					}
					else
					{
						b2bUnitList.add(i++, ((B2BUnitModel) group));
					}
				}
			}
		}
		else
		{
			for (final B2BUnitModel unit : rootNodes)//If Units were from diiferenct hireachy then check if parent or child unit then add accordingly in list
			{
				if (StringUtils.equals(unit.getUid(), selectedUnit) && getB2bUnitService().getRootUnit(unit).equals(unit))//If parent unit then add child unit also in list
				{
					for (final PrincipalGroupModel group : customerGroups)
					{
						if (group instanceof B2BUnitModel && getB2bUnitService().getRootUnit((B2BUnitModel) group).equals(unit))
						{
							b2bUnitList.add((B2BUnitModel) group);
						}
					}
					break;
				}
				else if (StringUtils.equals(unit.getUid(), selectedUnit))//If only child unit then add what is selected in login
				{
					b2bUnitList.add(unit);
					break;
				}
			}
		}
		return b2bUnitList.stream().collect(Collectors.toSet());
	}

	public List<B2BUnitModel> getAllB2BUnits(final CustomerModel customer)
	{
		final Set<PrincipalGroupModel> customerGroups = customer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final Set<B2BUnitModel> rootB2BUnits = new HashSet<>();
		for (final PrincipalGroupModel group : customerGroups)
		{
			if (group instanceof B2BUnitModel)
			{
				rootB2BUnits.add(getB2bUnitService().getRootUnit((B2BUnitModel) group));
				b2bUnitList.add((B2BUnitModel) group);
			}
		}
		if (rootB2BUnits.size() > 1)
		{
			return b2bUnitList;
		}
		else
		{
			b2bUnitList.clear();
			return b2bUnitList;
		}
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
