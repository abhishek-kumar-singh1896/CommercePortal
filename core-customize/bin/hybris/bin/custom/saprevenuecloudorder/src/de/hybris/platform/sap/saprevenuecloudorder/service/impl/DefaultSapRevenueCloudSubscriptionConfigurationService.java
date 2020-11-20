/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.saprevenuecloudorder.service.impl;


import org.apache.log4j.Logger;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.services.ApiRegistryClientService;
import de.hybris.platform.sap.saprevenuecloudorder.clients.SapRevenueCloudSubscriptionClient;
import de.hybris.platform.sap.saprevenuecloudorder.service.SapRevenueCloudSubscriptionConfigurationService;
import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Configuration Service for SubscriptionClient Object.
 *
 */
public class DefaultSapRevenueCloudSubscriptionConfigurationService implements SapRevenueCloudSubscriptionConfigurationService
{
	private static final Logger LOG = Logger.getLogger(DefaultSapRevenueCloudSubscriptionConfigurationService.class);
    private ApiRegistryClientService apiRegistryClientService;

	@Override
    public SapRevenueCloudSubscriptionClient getSapSubscriptionClient() {

		try
		{
			return getApiRegistryClientService().lookupClient(SapRevenueCloudSubscriptionClient.class);
		}
		catch (final CredentialException e)
		{
			LOG.error("Error occured while fetching SapRevenueCloudSubscriptionClient Configuration");
			throw new SystemException(e);
		}
    }

    public ApiRegistryClientService getApiRegistryClientService() {
		return apiRegistryClientService;
	}

	public void setApiRegistryClientService(ApiRegistryClientService apiRegistryClientService) {
		this.apiRegistryClientService = apiRegistryClientService;
	}

}
