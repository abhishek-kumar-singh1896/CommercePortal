/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/")
public class HomePageController extends AbstractPageController
{
	private static final String LOGOUT = "logout";
	private static final String ACCOUNT_CONFIRMATION_SIGNOUT_TITLE = "account.confirmation.signout.title";
	private static final String ACCOUNT_CONFIRMATION_CLOSE_TITLE = "account.confirmation.close.title";

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "storeSessionFacade")
	StoreSessionFacade storeSessionFacade;;

	@Resource(name = "siteConfigService")
	SiteConfigService siteConfigService;

	@RequestMapping(method = RequestMethod.GET)
	public String home(@RequestParam(value = WebConstants.CLOSE_ACCOUNT, defaultValue = "false")
	final boolean closeAcc, @RequestParam(value = LOGOUT, defaultValue = "false")
	final boolean logout, final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{

		final String siteCoreUrl = new StringBuilder(siteConfigService.getString(new StringBuilder("sitecore.root.url").append(".")
				.append(storeSessionFacade.getCurrentLanguage().getIsocode()).toString(), "#")).toString();
		return REDIRECT_PREFIX + siteCoreUrl;
	}


	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}
}
