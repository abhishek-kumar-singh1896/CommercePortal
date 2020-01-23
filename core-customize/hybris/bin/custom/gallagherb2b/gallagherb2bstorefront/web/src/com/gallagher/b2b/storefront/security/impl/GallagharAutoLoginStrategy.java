/**
 *
 */
package com.gallagher.b2b.storefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.commercefacades.customer.CustomerFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;


/**
 * @author nagarro
 *
 */
public class GallagharAutoLoginStrategy extends DefaultAutoLoginStrategy
{
	private AuthenticationManager authenticationManager;
	private CustomerFacade customerFacade;
	private GUIDCookieStrategy guidCookieStrategy;
	private RememberMeServices rememberMeServices;

	@Override
	public void login(final Authentication token, final HttpServletRequest request, final HttpServletResponse response)
	{
		try
		{
			//			final Authentication authentication = getAuthenticationManager().authenticate(token);
			//			SecurityContextHolder.getContext().setAuthentication(authentication);
			getCustomerFacade().loginSuccess();
			getGuidCookieStrategy().setCookie(request, response);
		}
		catch (final Exception e)
		{

		}
	}

	@Override
	protected AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	@Override
	@Required
	public void setAuthenticationManager(final AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Override
	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	@Override
	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Override
	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	@Override
	protected RememberMeServices getRememberMeServices()
	{
		return rememberMeServices;
	}

	@Override
	@Required
	public void setRememberMeServices(final RememberMeServices rememberMeServices)
	{
		this.rememberMeServices = rememberMeServices;
	}
}
