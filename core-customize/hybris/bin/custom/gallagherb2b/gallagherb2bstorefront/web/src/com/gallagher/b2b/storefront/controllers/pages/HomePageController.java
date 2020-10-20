/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

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
	private static final String ACCOUNT_NOT_CREATED = "account.not.created";
	private static final String ACCOUNT_CONFIRMATION_SIGNOUT_TITLE = "account.confirmation.signout.title";
	private static final String ACCOUNT_CONFIRMATION_CLOSE_TITLE = "account.confirmation.close.title";
	private static final String ACCOUNT_DISABLED = "account.login.disable";


	@RequestMapping(method = RequestMethod.GET)
	public String home(@RequestParam(value = WebConstants.CLOSE_ACCOUNT, defaultValue = "false")
	final boolean closeAcc, @RequestParam(value = LOGOUT, defaultValue = "false")
	final boolean logout, @RequestParam(value = "error", defaultValue = "false")
	final boolean error, @RequestParam(value = "disable", defaultValue = "false")
	final boolean disable, final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (logout)
		{
			String message = ACCOUNT_CONFIRMATION_SIGNOUT_TITLE;
			if (closeAcc)
			{
				message = ACCOUNT_CONFIRMATION_CLOSE_TITLE;
			}
			if (error)
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ACCOUNT_NOT_CREATED);
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, message);
			}
			return REDIRECT_PREFIX + ROOT;
		}

	 	if (error)
		{
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ACCOUNT_NOT_CREATED);
			return REDIRECT_PREFIX + ROOT;
		}
	 	
		if (disable)
		{
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, ACCOUNT_DISABLED);
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		updatePageTitle(model, getContentPageForLabelOrId(null));

		return getViewForPage(model);
	}

	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}
}