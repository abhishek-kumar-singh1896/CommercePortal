/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.keycloak.outboundservices.service.impl;

import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestClientException;

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

	private static final Logger LOGGER = Logger.getLogger(GallagherKeycloakServiceImpl.class);

	private static final String FARWORD_SLASH = "/";
	private static final String KEYCLOAK_USER_URL = "keycloak.user.url";

	private final UserService userService;
	private final BaseSiteService baseSiteService;
	private final UrlEncoderService urlEncoderService;
	private final ConfigurationService configurationService;
	private final SiteBaseUrlResolutionService siteBaseUrlResolutionService;

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
				true, null);

		final String url = MessageFormat.format(
				getConfigurationService().getConfiguration().getString("keycloak.reset.password.url"), keycloakGUID, redirectURI);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		LOGGER.error("Response status code for update password from Keycloak : " + response.getStatusCode());
		if (HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.NO_CONTENT.equals(response.getStatusCode()))
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
		final String clientID = getConfigurationService().getConfiguration().getString("keycloak.security.client.id");

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

		final String url = getConfigurationService().getConfiguration().getString(KEYCLOAK_USER_URL);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		final String keycloakGUID = StringUtils.substringAfterLast(response.getHeaders().getLocation().toString(), "/");

		sendResetPasswordNotification(keycloakGUID);

		return keycloakGUID;
	}

	@Override
	public String getKeycloakUserFromEmail(final String email)
	{
		String keyCloakGUID = null;
		try
		{
			final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

			final String userDetailUrl = getConfigurationService().getConfiguration().getString(KEYCLOAK_USER_URL) + "?email="
					+ email;

			final ResponseEntity<GallagherKeycloakResponse[]> userDetailresponse = restTemplate.getForEntity(userDetailUrl,
					GallagherKeycloakResponse[].class);

			if (userDetailresponse.getBody().length > 0)
			{
				keyCloakGUID = userDetailresponse.getBody()[0].getId();
			}
		}
		catch (final RestClientException | OAuth2Exception exception)
		{
			LOGGER.error("Exception occured while getting user from Keycloak : " + exception);
			keyCloakGUID = null;
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


		if (getBaseSiteService().getCurrentBaseSite() == null)
		{
			getBaseSiteService().setCurrentBaseSite("securityB2BGlobal", false);
		}

		final BaseSiteModel site = getBaseSiteService().getCurrentBaseSite();
		final String redirectURL = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(site, true,
				StringUtils.isEmpty(getUrlEncoderService().getUrlEncodingPattern())
						? FARWORD_SLASH + ((CMSSiteModel) site).getRegionCode() + FARWORD_SLASH
								+ ((CMSSiteModel) site).getDefaultLanguage().getIsocode()
						: null);


		final String url = MessageFormat.format(
				getConfigurationService().getConfiguration().getString("keycloak.reset.password.url"), keycloakGUID, redirectURL);

		final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
	}

	@Override
	public void updateKeyCloakUserProfile(final CustomerData customerData)
	{
		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final GallagherKeycloakUserRequest request = new GallagherKeycloakUserRequest();
		request.setUsername(customerData.getEmail());
		request.setEmail(customerData.getEmail());
		request.setFirstName(customerData.getFirstName());
		request.setLastName(customerData.getLastName());
		request.setEnabled(true);

		final HttpEntity<GallagherKeycloakUserRequest> entity = new HttpEntity<>(request, headers);

		final String userDetailUrl = getConfigurationService().getConfiguration().getString(KEYCLOAK_USER_URL) + "/"
				+ customerData.getKeycloakGUID();

		final ResponseEntity<String> response = restTemplate.exchange(userDetailUrl, HttpMethod.PUT, entity, String.class);

	}

	@Override
	public void updateKeycloakUserEmail(final CustomerData customerData)
	{

		final OAuth2RestTemplate restTemplate = getRestTemplateForKeycloak();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final GallagherKeycloakUserRequest request = new GallagherKeycloakUserRequest();
		request.setEmail(customerData.getEmail());
		request.setUsername(customerData.getEmail());
		request.setEnabled(true);

		final HttpEntity<GallagherKeycloakUserRequest> entity = new HttpEntity<>(request, headers);

		final String userDetailUrl = getConfigurationService().getConfiguration().getString(KEYCLOAK_USER_URL) + "/"
				+ customerData.getKeycloakGUID();

		final ResponseEntity<String> response = restTemplate.exchange(userDetailUrl, HttpMethod.PUT, entity, String.class);
	}
}
