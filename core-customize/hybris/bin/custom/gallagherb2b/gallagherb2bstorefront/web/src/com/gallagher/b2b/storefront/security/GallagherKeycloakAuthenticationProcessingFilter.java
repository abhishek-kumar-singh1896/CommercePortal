/**
 *
 */
package com.gallagher.b2b.storefront.security;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.commercefacades.customer.CustomerFacade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RequestAuthenticator;
import org.keycloak.adapters.spi.AuthChallenge;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.keycloak.adapters.springsecurity.authentication.RequestAuthenticatorFactory;
import org.keycloak.adapters.springsecurity.authentication.SpringSecurityRequestAuthenticatorFactory;
import org.keycloak.adapters.springsecurity.facade.SimpleHttpFacade;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.token.AdapterTokenStoreFactory;
import org.keycloak.adapters.springsecurity.token.SpringSecurityAdapterTokenStoreFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;


/**
 * @author nagarro
 *
 */
public class GallagherKeycloakAuthenticationProcessingFilter extends KeycloakAuthenticationProcessingFilter
{

	public GallagherKeycloakAuthenticationProcessingFilter(final AuthenticationManager authenticationManager)
	{
		super(authenticationManager);
	}

	private CustomerFacade customerFacade;
	private GUIDCookieStrategy guidCookieStrategy;
	private RememberMeServices rememberMeServices;
	private AuthenticationManager authenticationManager;

	private ApplicationContext applicationContext;
	private AdapterDeploymentContext adapterDeploymentContext;
	private final AdapterTokenStoreFactory adapterTokenStoreFactory = new SpringSecurityAdapterTokenStoreFactory();
	private final RequestAuthenticatorFactory requestAuthenticatorFactory = new SpringSecurityRequestAuthenticatorFactory();

	@Override
	public void afterPropertiesSet()
	{
		adapterDeploymentContext = applicationContext.getBean(AdapterDeploymentContext.class);
		super.afterPropertiesSet();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException
	{


		final HttpFacade facade = new SimpleHttpFacade(request, response);
		final KeycloakDeployment deployment = adapterDeploymentContext.resolveDeployment(facade);

		// using Spring authenticationFailureHandler
		deployment.setDelegateBearerErrorResponseSending(true);

		final AdapterTokenStore tokenStore = adapterTokenStoreFactory.createAdapterTokenStore(deployment, request, response);
		final RequestAuthenticator authenticator = requestAuthenticatorFactory.createRequestAuthenticator(facade, request,
				deployment, tokenStore, -1);

		final AuthOutcome result = authenticator.authenticate();

		if (AuthOutcome.FAILED.equals(result))
		{
			final AuthChallenge challenge = authenticator.getChallenge();
			if (challenge != null)
			{
				challenge.challenge(facade);
			}
			throw new KeycloakAuthenticationException("Invalid authorization header, see WWW-Authenticate header for details");
		}

		if (AuthOutcome.NOT_ATTEMPTED.equals(result))
		{
			final AuthChallenge challenge = authenticator.getChallenge();
			if (challenge != null)
			{
				challenge.challenge(facade);
			}
			if (deployment.isBearerOnly())
			{
				// no redirection in this mode, throwing exception for the spring handler
				throw new KeycloakAuthenticationException("Authorization header not found,  see WWW-Authenticate header");
			}
			else
			{
				// let continue if challenged, it may redirect
				return null;
			}
		}

		else if (AuthOutcome.AUTHENTICATED.equals(result))
		{
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Assert.notNull(authentication, "Authentication SecurityContextHolder was null");
			return authenticationManager.authenticate(authentication);
		}
		else
		{
			final AuthChallenge challenge = authenticator.getChallenge();
			if (challenge != null)
			{
				challenge.challenge(facade);
			}
			return null;
		}
	}

}
