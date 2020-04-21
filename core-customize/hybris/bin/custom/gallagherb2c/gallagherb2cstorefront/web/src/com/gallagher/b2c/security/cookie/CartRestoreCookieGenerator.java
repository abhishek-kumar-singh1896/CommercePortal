/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.security.cookie;

import de.hybris.platform.site.BaseSiteService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.net.InternetDomainName;


/**
 *
 */
public class CartRestoreCookieGenerator extends EnhancedCookieGenerator
{
	private BaseSiteService baseSiteService;

	@Override
	public String getCookieName()
	{
		return StringUtils.deleteWhitespace(getBaseSiteService().getCurrentBaseSite().getUid()) + "-cart";
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}


	public void addCookie(final HttpServletRequest request, final HttpServletResponse response, final String cookieValue)
	{
		super.addCookie(new HttpServletResponseWrapper(response) // NOSONAR
		{
			@Override
			public void addCookie(final Cookie cookie)
			{
				setEnhancedCookiePath(cookie);

				final InternetDomainName domainName = InternetDomainName.from(request.getServerName());
				if (domainName.isTopPrivateDomain() || domainName.hasPublicSuffix())
				{
					final String url = domainName.topPrivateDomain().toString();
					final InternetDomainName it = InternetDomainName.from(url);
					cookie.setDomain(it.toString());
				}

				if (isHttpOnly())
				{
					cookie.setHttpOnly(true);
				}

				super.addCookie(cookie);
			}
		}, cookieValue);
	}
}
