/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.interceptors.beforeview;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.gallagher.b2c.util.UiThemeUtils;
import com.gallagher.core.product.impl.GallagherProductService;
import com.gallagher.facades.storesession.GallagherStoreSessionFacade;


/**
 * Interceptor to setup the paths to the UI resource paths in the model before passing it to the view. Sets up the path
 * to the web accessible UI resources for the following: * The current site * The current theme * The common resources
 * All of these paths are qualified by the current UiExperienceLevel
 */
public class UiThemeResourceBeforeViewHandler implements BeforeViewHandler
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UiThemeResourceBeforeViewHandler.class);

	protected static final String COMMON = "common";
	protected static final String SHARED = "shared";

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "uiThemeUtils")
	private UiThemeUtils uiThemeUtils;

	@Resource(name = "uiExperienceService")
	private UiExperienceService uiExperienceService;

	@Resource(name = "configurationService")
	private ConfigurationService configService;

	@Resource(name = "storeSessionFacade")
	protected GallagherStoreSessionFacade storeSessionFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "gallagherProductService")
	private GallagherProductService gallagherProductService;

	@Resource(name = "productService")
	private ProductService productService;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		final String siteName = currentSite.getUid();
		final String regionCode = currentSite.getRegionCode().getCode();
		final String themeName = uiThemeUtils.getThemeNameForCurrentSite();
		final String uiExperienceCode = uiExperienceService.getUiExperienceLevel().getCode();
		final String uiExperienceCodeLower = uiThemeUtils.getUiExperience();

		final String contextPath = uiThemeUtils.getContextPathFromRequest(request);

		final String siteRootUrl = contextPath + "/_ui/" + uiExperienceCodeLower;
		final String sharedResourcePath = contextPath + "/_ui/" + SHARED;
		final String siteResourcePath = siteRootUrl + "/site-" + siteName;
		final String themeResourcePath = siteRootUrl + "/theme-" + themeName;
		final String commonResourcePath = siteRootUrl + "/" + COMMON;
		final String encodedContextPath = request.getContextPath();
		final LanguageModel currentLanguage = commerceCommonI18NService.getCurrentLanguage();

		modelAndView.addObject("contextPath", contextPath);
		modelAndView.addObject("sharedResourcePath", sharedResourcePath);
		modelAndView.addObject("siteResourcePath", siteResourcePath);
		modelAndView.addObject("themeResourcePath", themeResourcePath);
		modelAndView.addObject("commonResourcePath", commonResourcePath);
		modelAndView.addObject("encodedContextPath", encodedContextPath);
		modelAndView.addObject("siteRootUrl", siteRootUrl);
		modelAndView.addObject("language", currentLanguage != null ? currentLanguage.getIsocode() : "en");
		modelAndView.addObject("themeName", themeName);
		modelAndView.addObject("uiExperienceLevel", uiExperienceCode);
		modelAndView.addObject("regionCode", regionCode);

		final String detectedUiExperienceCode = uiExperienceService.getDetectedUiExperienceLevel().getCode();
		modelAndView.addObject("detectedUiExperienceCode", detectedUiExperienceCode);
		final UiExperienceLevel overrideUiExperienceLevel = uiExperienceService.getOverrideUiExperienceLevel();
		if (overrideUiExperienceLevel == null)
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.FALSE);
		}
		else
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.TRUE);
			modelAndView.addObject("overrideUiExperienceCode", overrideUiExperienceLevel.getCode());
		}

		final DeviceData currentDetectedDevice = deviceDetectionFacade.getCurrentDetectedDevice();
		modelAndView.addObject("detectedDevice", currentDetectedDevice);

		modelAndView.addObject("addOnCommonCssPaths", uiThemeUtils.getAddOnCommonCSSPaths(request));
		modelAndView.addObject("addOnThemeCssPaths", uiThemeUtils.getAddOnThemeCSSPaths(request));
		modelAndView.addObject("addOnJavaScriptPaths", uiThemeUtils.getAddOnJSPaths(request));
		modelAndView.addObject("sitecoreHomePage", storeSessionFacade.getSitecoreRootUrl());

		final CustomerModel currentUser = (CustomerModel) userService.getCurrentUser();
		//if customer is not Anonymous and any of preferences for current customer is null
		if (!userService.isAnonymousUser(userService.getCurrentUser())
				&& (currentUser.getNewsLetters() == null || currentUser.getEvents() == null || currentUser.getProductPromo() == null
						|| currentUser.getProductRelease() == null || currentUser.getProductUpdate() == null))
		{
			modelAndView.addObject("showPreferences", true);
		}
		else
		{
			modelAndView.addObject("showPreferences", false);
		}

		modelAndView.addObject("canonicalURL", request.getRequestURL());

		final StringBuffer requestURL = request.getRequestURL();
		Map<String, String> hreflangMap = new HashMap<>();
		if (modelAndView.getModel().containsKey("product"))
		{
			final ProductData productData = (ProductData) modelAndView.getModel().get("product");
			final ProductModel product = productService.getProductForCode(productData.getCode());
			if (StringUtils.isEmpty(productData.getBaseProduct()))
			{
				final Collection<BaseStoreModel> stores = product.getBaseStores();
				hreflangMap = gethreflangURL(stores, requestURL);
			}
			else
			{
				final ProductModel baseProductModel = productService.getProductForCode(productData.getBaseProduct());
				final Set<BaseStoreModel> storesSet = gallagherProductService.getBaseStoresForVariant(productData.getCode());
				hreflangMap = gethreflangURL(storesSet, requestURL);
			}
		}
		else
		{
			final Collection<CMSSiteModel> baseSiteList = cmsSiteService.getSites();
			if (requestURL.toString().contains("/am/"))
			{
				for (final CMSSiteModel site : baseSiteList)
				{
					if (null != site.getRegionCode() && null != site.getDefaultLanguage())
					{
						final String valueString = "/am/" + site.getRegionCode().getCode() + "/"
								+ site.getDefaultLanguage().getIsocode() + "/";
						final String finalValue = gethreflangURL(requestURL, valueString);
						hreflangMap.put(site.getDefaultLanguage().getIsocode(), finalValue);
					}
				}
			}
		}
		modelAndView.addObject("hreflangMap", hreflangMap);
	}


	/**
	 * @param stores
	 * @param requestURL
	 */

	protected Map<String, String> gethreflangURL(final Collection<BaseStoreModel> stores, final StringBuffer requestURL)
	{
		final Map<String, String> hreflangMap = new HashMap<>();
		for (final BaseStoreModel base : stores)
		{
			if (requestURL.toString().contains("/am/"))
			{
				for (final BaseSiteModel site : base.getCmsSites())
				{
					final CMSSiteModel cmsSite = (CMSSiteModel) site;
					if (null != cmsSite.getRegionCode() && null != cmsSite.getDefaultLanguage())
					{
						final String valueString = "/am/" + cmsSite.getRegionCode().getCode() + "/"
								+ cmsSite.getDefaultLanguage().getIsocode() + "/";
						final String finalValue = gethreflangURL(requestURL, valueString);
						hreflangMap.put(cmsSite.getDefaultLanguage().getIsocode(), finalValue);
					}
				}
			}
		}
		return hreflangMap;
	}

	public String gethreflangURL(final StringBuffer requestURL, final String valueString)
	{
		final int index1 = requestURL.indexOf("/am/");
		final String sub1 = requestURL.substring(index1 + 4, requestURL.length());
		final int index2 = sub1.indexOf("/");
		final String sub2 = sub1.substring(index2 + 1, sub1.length());
		final int index3 = sub2.indexOf("/");
		final int initalIndex = index1;
		final int endIndex = index1 + index2 + index3 + 6;
		final String finalValue = requestURL.replace(initalIndex, endIndex, valueString).toString();
		return finalValue;
	}
}
