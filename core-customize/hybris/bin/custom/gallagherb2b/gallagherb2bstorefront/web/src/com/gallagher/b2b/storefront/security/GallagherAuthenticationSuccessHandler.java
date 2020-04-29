/**
 *
 */
package com.gallagher.b2b.storefront.security;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * Wrapper for an authentication success handler that sends a redirect if a redirect URL (based on Address of
 * B2BCustomer's DefaultB2BUnit) is different.
 *
 * @author shishirkant
 */
public class GallagherAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherAuthenticationSuccessHandler.class);
	private static final String FARWORD_SLASH = "/";

	private final AuthenticationSuccessHandler fallback;
	private final UserService userService;
	private final CMSAdminSiteService cmsAdminSiteService;

	public GallagherAuthenticationSuccessHandler(final AuthenticationSuccessHandler fallback, final UserService userService,
			final CMSAdminSiteService cmsAdminSiteService)
	{
		this.fallback = fallback;
		this.userService = userService;
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		final UserModel user = userService.getCurrentUser();

		if (user instanceof B2BCustomerModel && null != ((B2BCustomerModel) user).getDefaultB2BUnit())
		{
			final Collection<AddressModel> addresses = ((B2BCustomerModel) user).getDefaultB2BUnit().getAddresses();
			if (CollectionUtils.isNotEmpty(addresses))
			{
				for (final AddressModel address : addresses)
				{
					if (null != address.getCountry())
					{
						final String regionCode = address.getCountry().getRegionCode().getCode();
						String redirectPath;
						try
						{
							final CMSSiteModel site = cmsAdminSiteService.getSiteForId("securityB2B" + regionCode.toUpperCase());
							redirectPath = site.getRedirectURL();
						}
						catch (final UnknownIdentifierException exception)
						{
							redirectPath = "/security/global/en";
						}
						LOGGER.info("Redirection based upon country " + redirectPath);
						response.sendRedirect(redirectPath);
						return;
					}
				}
			}
		}

		final String location = KeycloakCookieBasedRedirect.getRedirectUrlFromCookie(request);
		if (location == null)
		{
			if (fallback != null)
			{
				LOGGER.info("Redirection based upon fallback");
				fallback.onAuthenticationSuccess(request, response, authentication);
			}
		}
		else
		{
			try
			{
				LOGGER.info("Redirection based upon cookie " + location);
				response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(null));
				response.sendRedirect(location);
			}
			catch (final IOException e)
			{
				LOGGER.warn("Unable to redirect user after login", e);
			}
		}
	}
}
