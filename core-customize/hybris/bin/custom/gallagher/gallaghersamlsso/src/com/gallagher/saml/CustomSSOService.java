/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.saml;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.DefaultSSOService;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomSSOService extends DefaultSSOService
{

	private static final Logger LOG = LoggerFactory.getLogger(CustomSSOService.class.getName());

	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
	{
		LOG.info("SSO login initiated for id=" + id + "; name=" + name);

		// TODO: This is temporary for SSOCircle prototype to work.  Production
		// IDP should actually pass the role(s) of the authenticated user.
		if (CollectionUtils.isEmpty(roles))
		{
			LOG.info("Roles are empty, setting role as 'asagent'.");
			roles.add("asagent");
		}
		return super.getOrCreateSSOUser(id, name, roles);
	}
}