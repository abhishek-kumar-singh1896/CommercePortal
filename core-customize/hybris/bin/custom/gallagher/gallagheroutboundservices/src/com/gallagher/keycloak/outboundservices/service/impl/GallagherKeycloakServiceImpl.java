/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.keycloak.outboundservices.service.impl;

import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.gallagher.outboundservices.request.dto.GallagherKeycloakUserRequest;
import com.gallagher.outboundservices.response.dto.GallagherKeycloakResponse;


/**
 * Implementation of GallagherKeycloakService
 *
 * @author shishirkant
 */
public class GallagherKeycloakServiceImpl implements GallagherKeycloakService
{
	private final ConfigurationService configurationService;
	private final UserService userService;
	private final BaseSiteService baseSiteService;
	private final SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private final UrlEncoderService urlEncoderService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	public UrlEncoderService getUrlEncoderService()
	{
		return urlEncoderService;
	}

	@Autowired
	public GallagherKeycloakServiceImpl(final ConfigurationService configurationService, final UserService userService,
			final BaseSiteService baseSiteService, final SiteBaseUrlResolutionService siteBaseUrlResolutionService,
			final UrlEncoderService urlEncoderService)
	{
		this.configurationService = configurationService;
		this.userService = userService;
		this.baseSiteService = baseSiteService;
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
		this.urlEncoderService = urlEncoderService;
	}

	/**
	 * {@inheritDoc}
	 */
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
		final String redirectURI = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSiteService().getCurrentBaseSite(),
				true, getUrlEncoderService().getCurrentUrlEncodingPattern());

		final String url = MessageFormat.format(
				getConfigurationService().getConfiguration().getString("keycloak.reset.password.url"), keycloakGUID, redirectURI);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		if (HttpStatus.OK.equals(response.getStatusCode()))
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createKeycloakUser(final CustomerData customerData)
	{
		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final GallagherKeycloakUserRequest request = new GallagherKeycloakUserRequest();
		request.setUsername(customerData.getEmail());
		request.setFirstName(customerData.getFirstName());
		request.setLastName(customerData.getLastName());
		request.setEmail(customerData.getEmail());
		request.setEnabled(true);

		final HttpEntity<GallagherKeycloakUserRequest> entity = new HttpEntity<>(request, headers);

		final String url = getConfigurationService().getConfiguration().getString("keycloak.user.url");

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		final String keycloakGUID = StringUtils.substringAfterLast(response.getHeaders().getLocation().toString(), "/");

		sendResetPasswordNotification(keycloakGUID);

		return keycloakGUID;
	}

	@Override
	public String getKeycloakUserFromEmail(final String email)
	{
		String keyCloakGUID = null;
		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final String userDetailUrl = getConfigurationService().getConfiguration().getString("keycloak.user.url") + "?email="
				+ email;

		final ResponseEntity<GallagherKeycloakResponse[]> userDetailresponse = restTemplate.getForEntity(userDetailUrl,
				GallagherKeycloakResponse[].class);

		if (userDetailresponse.getBody().length > 0)
		{
			keyCloakGUID = userDetailresponse.getBody()[0].getId();
		}

		return keyCloakGUID;
	}

	private void sendResetPasswordNotification(final String keycloakGUID)
	{
		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String request = "[\"UPDATE_PASSWORD\"]";

		final HttpEntity<String> entity = new HttpEntity<>(request, headers);

		final String redirectURL = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSiteService().getCurrentBaseSite(),
				true, getUrlEncoderService().getCurrentUrlEncodingPattern());

		final String url = MessageFormat.format(
				getConfigurationService().getConfiguration().getString("keycloak.reset.password.url"), keycloakGUID, redirectURL);

		System.out.println("Password reset email start sending.........");

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		System.out.println("Password reset email sended.........");
	}
}
