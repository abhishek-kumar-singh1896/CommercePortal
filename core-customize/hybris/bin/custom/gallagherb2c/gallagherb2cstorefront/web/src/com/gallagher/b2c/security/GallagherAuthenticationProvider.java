/**
 *
 */
package com.gallagher.b2c.security;

import de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * @author nagarro
 *
 */
public class GallagherAuthenticationProvider extends AbstractAcceleratorAuthenticationProvider
{
	private static final Logger LOGGER = Logger.getLogger(GallagherAuthenticationProvider.class);
	private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

	public GallagherAuthenticationProvider()
	{
		super();
	}

	@Override
	public boolean supports(final Class authentication)
	{
		return KeycloakAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{

		final String username = getUserEmailFromAuthentication(authentication);
		final KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

		UserDetails userDetails = null;
		try
		{
			userDetails = retrieveUser(username);
		}
		catch (final UsernameNotFoundException arg5)
		{
			throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"),
					arg5);
		}
		final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
		JaloSession.getCurrentSession().setUser(user);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		for (final String role : token.getAccount().getRoles())
		{
			grantedAuthorities.add(new KeycloakRole(role));
		}
		//		return super.createSuccessAuthentication(authentication, userDetails);
		return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), mapAuthorities(grantedAuthorities));
	}

	public void setGrantedAuthoritiesMapper(final GrantedAuthoritiesMapper grantedAuthoritiesMapper)
	{
		this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
	}

	private Collection<? extends GrantedAuthority> mapAuthorities(final Collection<? extends GrantedAuthority> authorities)
	{
		return grantedAuthoritiesMapper != null ? grantedAuthoritiesMapper.mapAuthorities(authorities) : authorities;
	}

	private String getUserEmailFromAuthentication(final Authentication authentication)
	{
		final KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
		if (null != token)
		{
			return token.getAccount().getKeycloakSecurityContext().getIdToken().getEmail();
		}
		return "NONE_PROVIDED";
	}

}
