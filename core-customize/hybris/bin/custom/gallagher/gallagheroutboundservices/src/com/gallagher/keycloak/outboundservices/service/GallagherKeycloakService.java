/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.keycloak.outboundservices.service;

import de.hybris.platform.commercefacades.user.data.CustomerData;


/**
 * Gallagher Service to integrate with Keycloak REST APIs.
 *
 * @author shishirkant
 */
public interface GallagherKeycloakService
{
	/**
	 * Returns status of Email Notification, sent to customer for Password Update.
	 *
	 * @param customerUid
	 *
	 * @return status
	 */
	boolean sendUpdatePasswordNotification(final String customerUid);

	/**
	 * Returns Keycloak GUID once user is created in Keycloak successfully.
	 *
	 * @param customerData
	 *
	 * @return keycloakGUID
	 */
	String createKeycloakUser(final CustomerData customerData);

	/**
	 * Returns Keycloak GUID if user exists in Keycloak.
	 *
	 * @param email
	 *
	 * @return keycloakGUID
	 */
	String getKeycloakUserFromEmail(String email);

	void updateKeyCloakUserProfile(CustomerData customerData);

	void updateKeycloakUserEmail(CustomerData customerData);
}
