/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.security;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationSuccessHandler;
import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * Wrapper for an authentication success handler that sends a redirect if a redirect URL was set in a cookie.
 *
 * @author <a href="mailto:scranen@gmail.com">Sjoerd Cranen</a>
 *
 * @see KeycloakCookieBasedRedirect
 * @see KeycloakAuthenticationEntryPoint#commenceLoginRedirect
 */
public class GallagherKeycloakAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{

	private static final Logger LOG = LoggerFactory.getLogger(KeycloakAuthenticationSuccessHandler.class);

	private final AuthenticationSuccessHandler fallback;
	final SiteConfigService siteConfigService;
	final StoreSessionFacade storeSessionFacade;

	public GallagherKeycloakAuthenticationSuccessHandler(final AuthenticationSuccessHandler fallback,
			final SiteConfigService siteConfigService, final StoreSessionFacade storeSessionFacade)
	{
		this.fallback = fallback;
		this.storeSessionFacade = storeSessionFacade;
		this.siteConfigService = siteConfigService;
	}

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		final String location = KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request);
		if (location == null)
		{
			if (fallback != null)
			{
				fallback.onAuthenticationSuccess(request, response, authentication);
			}
		}
		else
		{
			try
			{
				//final String siteCoreUrl = new StringBuilder(siteConfigService.getString(new StringBuilder("sitecore.root.url")
				//	.append(".").append(storeSessionFacade.getCurrentLanguage().getIsocode()).toString(), "#"))
				//		.append("/login?ReturnUrl=").toString();

				response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(null));

				//response.sendRedirect(siteCoreUrl + location);
				response.sendRedirect(location);
			}
			catch (final IOException e)
			{
				LOG.warn("Unable to redirect user after login", e);
			}
		}
	}
}
