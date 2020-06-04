/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.b2b.storefront.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


public class GallagherAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{
		// Check that the response was not committed yet (this may happen when another
		// part of the Keycloak adapter sends a challenge or a redirect).
		if (!response.isCommitted())
		{
			if (KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request) != null)
			{
				response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(null));
			}
			super.onAuthenticationFailure(request, response, exception);
		}
		else
		{
			if (200 <= response.getStatus() && response.getStatus() < 300)
			{
				throw new RuntimeException("Success response was committed while authentication failed!", exception);
			}
		}
	}
}
