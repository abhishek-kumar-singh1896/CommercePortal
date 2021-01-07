/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractRegisterPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.authentication.KeycloakCookieBasedRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2c.controllers.ControllerConstants;


/**
 * Register Controller for mobile. Handles login and register for the account flow.
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterPageController extends AbstractRegisterPageController
{
	private static final Logger LOG = LoggerFactory.getLogger(RegisterPageController.class);
	private HttpSessionRequestCache httpSessionRequestCache;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("register");
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/";
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountRegisterPage;
	}

	@Resource(name = "httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doRegister(final Model model, @RequestParam(value = "code", required = false)
	final String code, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final String redirectURL = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSiteService().getCurrentBaseSite(),
				true, "/register");
		String redirectURI = null;
		if (StringUtils.isEmpty(code))
		{
			if (StringUtils.isNotEmpty(getRedirectUrl(request)))
			{
				response.addCookie(KeycloakCookieBasedRedirect.createCookieFromRedirectUrl(getRedirectUrl(request)));
			}
			String language = "en";
			if (redirectURL.contains("es_CL"))
			{
				language = "es";
			}

			if (redirectURL.contains("fr_CA"))
			{
				language = "ca";
			}
			redirectURI = REDIRECT_PREFIX + MessageFormat.format(
					getConfigurationService().getConfiguration().getString("keycloak.registrations.url"), redirectURL, language);
		}
		else
		{
			redirectURI = REDIRECT_PREFIX + "/login";
		}
		return redirectURI;
	}

	/**
	 * Returns the redirect Url
	 *
	 * @param request
	 *           to get the url
	 * @return redirect url
	 */
	private String getRedirectUrl(final HttpServletRequest request)
	{
		String redirectURI = null;

		if (StringUtils.isNotEmpty(request.getHeader("referer")))
		{
			redirectURI = request.getHeader("referer");
		}

		return redirectURI;
	}

	@RequestMapping(value = "/newcustomer", method = RequestMethod.POST)
	public String doRegister(final RegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getRegistrationValidator().validate(form, bindingResult);
		return processRegisterUserRequest(null, form, bindingResult, model, request, response, redirectModel);
	}
}
