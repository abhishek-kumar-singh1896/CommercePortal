/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.keycloak.outboundservices.service.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;


/**
 *
 */
public class GallagherKeycloakServiceImpl implements GallagherKeycloakService
{
	private final ConfigurationService configurationService;
	private final UserService userService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Autowired
	public GallagherKeycloakServiceImpl(final ConfigurationService configurationService, final UserService userService)
	{
		this.configurationService = configurationService;
		this.userService = userService;
	}

	@Override
	public boolean sendUpdatePasswordNotification(final String customerUid)
	{
		boolean status = false;

		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String request = "[\"UPDATE_PASSWORD\"]";

		final HttpEntity<String> entity = new HttpEntity<>(request, headers);

		final UserModel user = getUserService().getUserForUID(customerUid);
		final String keycloakGUID = ((CustomerModel) user).getKeycloakGUID();

		final String url = MessageFormat
				.format(getConfigurationService().getConfiguration().getString("keycloak.reset.password.url"), keycloakGUID);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		if (200 == response.getStatusCodeValue())
		{
			status = true;
		}

		return status;
	}

	private OAuth2RestTemplate getRestTemplateForKeycloak()
	{
		final String accessTokenURL = getConfigurationService().getConfiguration().getString("keycloak.token.url");
		final String grantType = getConfigurationService().getConfiguration().getString("keycloak.grant.type");
		final String username = getConfigurationService().getConfiguration().getString("keycloak.username");
		final String password = getConfigurationService().getConfiguration().getString("keycloak.password");
		final String clientID = getConfigurationService().getConfiguration().getString("keycloak.client.id");

		final ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setAccessTokenUri(accessTokenURL);
		resource.setGrantType(grantType);
		resource.setUsername(username);
		resource.setPassword(password);
		resource.setClientId(clientID);

		return new OAuth2RestTemplate(resource);
	}

	@Override
	public String createKeycloakUser(final CustomerData customerData)
	{
		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String request = "{\"username\":\"" + customerData.getEmail() + "\", \"firstName\":\"" + customerData.getFirstName()
				+ "\",\"lastName\":\"" + customerData.getLastName() + "\", \"email\":\"" + customerData.getEmail()
				+ "\", \"enabled\":\"true\"}";

		final HttpEntity<String> entity = new HttpEntity<>(request, headers);

		final String url = getConfigurationService().getConfiguration().getString("keycloak.create.user.url");

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return StringUtils.substringAfterLast(response.getHeaders().getLocation().toString(), "/");
	}
}
