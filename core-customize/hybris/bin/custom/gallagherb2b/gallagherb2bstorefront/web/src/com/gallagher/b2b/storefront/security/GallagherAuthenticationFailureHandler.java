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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


public class GallagherAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
	private final boolean forwardToDestination = false;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{
		// Check that the response was not committed yet (this may happen when another
		// part of the Keycloak adapter sends a challenge or a redirect).
		if (!response.isCommitted())
		{
			if (exception instanceof DisabledException)
			{
				handleFailure(request, response, exception);
			}

			else if (KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request) != null)
			{
				response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(null));
				super.onAuthenticationFailure(request, response, exception);
			}
		}
		else
		{
			if (200 <= response.getStatus() && response.getStatus() < 300)
			{
				throw new RuntimeException("Success response was committed while authentication failed!", exception);
			}
		}
	}

	/**
    * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise returns a 401 error code.
    * <p>
    * If redirecting or forwarding, {@code saveException} will be called to cache the exception for use in the target
    * view.
    */
   public void handleFailure(final HttpServletRequest request, final HttpServletResponse response,
           final AuthenticationException exception) throws IOException, ServletException
   {
        final String disabledUrl = "/logout?disabled=true";
       if (disabledUrl == null)
       {
           logger.debug("No failure URL set, sending 401 Unauthorized error");
           response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
       }
       else
       {
           saveException(request, exception);
           if (forwardToDestination)
           {
               logger.debug("Forwarding to " + disabledUrl);
               request.getRequestDispatcher(disabledUrl).forward(request, response);
           }
           else
           {
               logger.debug("Redirecting to " + disabledUrl);
               redirectStrategy.sendRedirect(request, response, disabledUrl);
           }
       }
   }
}