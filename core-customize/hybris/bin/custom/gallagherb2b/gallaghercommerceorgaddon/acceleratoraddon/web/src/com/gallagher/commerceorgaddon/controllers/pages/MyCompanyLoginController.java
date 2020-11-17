/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.commerceorgaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gallagher.commerceorgaddon.controllers.GallaghercommerceorgaddonControllerConstants;


/**
 * MyCompany Login Controller. Handles login and register for the checkout flow.
 */
@Controller
@RequestMapping(value = "/login/my-company")
public class MyCompanyLoginController extends AbstractLoginPageController
{
	@Override
	protected String getView()
	{
		return GallaghercommerceorgaddonControllerConstants.Views.Pages.MyCompany.MyCompanyLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		return "/my-company";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("my-company-login");
	}
}