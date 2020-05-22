/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.interceptors;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;

import com.gallagher.sovos.outboundservices.service.GallagherSovosService;
import com.sap.hybris.sapcustomerb2c.outbound.CustomerAddressReplicationUtilityService;


/**
 *
 */
public class GallagherAddressInterceptor implements ValidateInterceptor<AddressModel>
{
	private static final Logger LOGGER = Logger.getLogger(GallagherAddressInterceptor.class);

	private CustomerAddressReplicationUtilityService customerAddressReplicationUtilityService;
	private GallagherSovosService gallagherSovosService;
	private BaseStoreService baseStoreService;

	@Override
	public void onValidate(final AddressModel addressModel, final InterceptorContext ctx) throws InterceptorException
	{
		// we only replicate the address of a CustomerModel
		if (addressModel.getOwner().getClass() != CustomerModel.class)
		{
			return;
		}

		if (getBaseStoreService().getCurrentBaseStore() == null || !baseStoreService.getCurrentBaseStore().getExternalTaxEnabled())
		{
			return;
		}

		if (StringUtil.isEmpty(addressModel.getGeoCode()))
		{
			return;
		}

		final CustomerModel customerModel = ((CustomerModel) addressModel.getOwner());

		// check if one of the supported fields was modified
		if (getCustomerAddressReplicationUtilityService().isAddressReplicationRequired(addressModel, getMonitoredAttributes(), ctx))
		{
			addressModel.setGeoCode(getGallagherSovosService().getGeoCode(addressModel));
		}

	}


	protected Set<String> getMonitoredAttributes()
	{

		final Set<String> monitoredAttributes = new HashSet<>();

		monitoredAttributes.add(AddressModel.COUNTRY);
		monitoredAttributes.add(AddressModel.TOWN);
		monitoredAttributes.add(AddressModel.POSTALCODE);
		monitoredAttributes.add(AddressModel.REGION);

		return monitoredAttributes;

	}

	/**
	 * @return baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * set baseStoreService
	 *
	 * @param baseStoreService
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return customerAddressReplicationUtilityService
	 */

	protected CustomerAddressReplicationUtilityService getCustomerAddressReplicationUtilityService()
	{
		return customerAddressReplicationUtilityService;
	}

	/**
	 * set customerAddressReplicationUtilityService
	 *
	 * @param customerAddressReplicationUtilityService
	 */
	public void setCustomerAddressReplicationUtilityService(final CustomerAddressReplicationUtilityService customerAddressReplicationUtilityService)
	{
		this.customerAddressReplicationUtilityService = customerAddressReplicationUtilityService;
	}

	/**
	 * @return gallagherSovosService
	 */
	public GallagherSovosService getGallagherSovosService()
	{
		return gallagherSovosService;
	}

	/**
	 * set gallagherSovosService
	 *
	 * @param gallagherSovosService
	 */
	public void setGallagherSovosService(final GallagherSovosService gallagherSovosService)
	{
		this.gallagherSovosService = gallagherSovosService;
	}
}
