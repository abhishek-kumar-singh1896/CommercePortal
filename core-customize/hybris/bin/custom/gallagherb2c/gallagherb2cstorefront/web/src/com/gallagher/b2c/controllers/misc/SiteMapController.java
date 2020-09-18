/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.controllers.misc;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.b2c.controllers.ControllerConstants;


@Controller
public class SiteMapController extends AbstractController
{
	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;


	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
	public String getSitemapXml(final Model model, final HttpServletResponse response)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		final String mediaUrlForSite = siteBaseUrlResolutionService.getMediaUrlForSite(currentSite, false, "");

		final List<String> siteMapUrls = new ArrayList<>();
		final LanguageModel language = commonI18NService.getCurrentLanguage();
		final CurrencyModel currency = commonI18NService.getCurrentCurrency();
		String langCurrency = "";
		if (language != null && currency != null)
		{
			langCurrency = language.getIsocode() + "-" + currency.getIsocode();
		}

		final Collection<MediaModel> siteMaps = currentSite.getSiteMaps();
		for (final MediaModel siteMap : siteMaps)
		{
			if (siteMap.getCode().contains(langCurrency))
			{
				siteMapUrls.add(mediaUrlForSite + siteMap.getURL());
			}
		}
		model.addAttribute("siteMapUrls", siteMapUrls);

		return ControllerConstants.Views.Pages.Misc.MiscSiteMapPage;
	}
}
