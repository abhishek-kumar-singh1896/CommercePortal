/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.keycloak.outboundservices.service;

import de.hybris.platform.commercefacades.user.data.CustomerData;


/**
 *
 */
public interface GallagherKeycloakService
{
	boolean sendUpdatePasswordNotification(final String customerUid);

	String createKeycloakUser(final CustomerData customerData);
}
