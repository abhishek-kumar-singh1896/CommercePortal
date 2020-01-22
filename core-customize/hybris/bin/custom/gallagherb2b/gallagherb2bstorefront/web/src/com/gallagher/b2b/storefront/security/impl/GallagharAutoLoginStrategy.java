/**
 *
 */
package com.gallagher.b2b.storefront.security.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * @author nagarro
 *
 */
public class GallagharAutoLoginStrategy extends DefaultAutoLoginStrategy
{
	@Override
	public void login(final Authentication token, final HttpServletRequest request, final HttpServletResponse response)
	{
		try
		{
			final Authentication authentication = getAuthenticationManager().authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			getCustomerFacade().loginSuccess();
			getGuidCookieStrategy().setCookie(request, response);
		}
		catch (final Exception e)
		{

		}
	}
}
