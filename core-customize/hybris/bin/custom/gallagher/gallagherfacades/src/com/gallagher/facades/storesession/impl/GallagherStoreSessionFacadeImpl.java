package com.gallagher.facades.storesession.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.storesession.impl.DefaultStoreSessionFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gallagher.facades.storesession.GallagherStoreSessionFacade;


/**
 * Store session facade implementation. The main purpose is to load currency and language data from existing services.
 *
 * @author Vikram Bishnoi
 */
public class GallagherStoreSessionFacadeImpl extends DefaultStoreSessionFacade implements GallagherStoreSessionFacade
{
	private static final Logger LOG = Logger.getLogger(GallagherStoreSessionFacadeImpl.class);

	private static final String SITECORE_ROOT_URL = "sitecore.root.url";

	@Resource
	private UserService userService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetEncodingAttributes()
	{
		getSessionService().removeAttribute("encodingAttributes");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<LanguageData> getAllLanguages()
	{
		Collection<LanguageModel> languages = null;
		/* This is for the smartedit */
		if (userService.getCurrentUser().getClass() == EmployeeModel.class && getBaseStoreService().getCurrentBaseStore() != null)
		{
			for (final BaseSiteModel site : getBaseStoreService().getCurrentBaseStore().getCmsSites())
			{
				final Collection<LanguageModel> catalogLanguages = new ArrayList<>(
						((CMSSiteModel) site).getContentCatalogs().get(0).getLanguages());
				if (CollectionUtils.isNotEmpty(catalogLanguages))
				{
					final List<LanguageModel> fallbacks = new ArrayList<>(catalogLanguages.size());
					catalogLanguages.forEach(language -> {
						if (CollectionUtils.isNotEmpty(language.getFallbackLanguages())
								&& (!fallbacks.contains(language.getFallbackLanguages().get(0)))
								&& (!catalogLanguages.contains(language.getFallbackLanguages().get(0))))
						{
							fallbacks.add(language.getFallbackLanguages().get(0));
						}
					});
					languages = new ArrayList<>(catalogLanguages);
					languages.addAll(fallbacks);
					break;
				}
			}
		}
		else
		{
			languages = getCommerceCommonI18NService().getAllLanguages();
		}
		if (languages.isEmpty())
		{
			languages = getCommonI18NService().getAllLanguages();
		}
		Assert.notEmpty(languages, "No supported languages found for the current site.");

		return Converters.convertAll(languages, getLanguageConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSitecoreRootUrl()
	{
		String sitecoreUrl = getSessionService().getAttribute("sitecoreRootUrl");
		if (StringUtils.isEmpty(sitecoreUrl))
		{
			sitecoreUrl = getSiteCoreConfigurationPath(SITECORE_ROOT_URL);
		}
		return sitecoreUrl;
	}

	private String getSiteCoreConfigurationPath(final String path)
	{
		return siteConfigService.getString(new StringBuilder(path).append(".").append(getCurrentLanguage().getIsocode()).toString(),
				"/");
	}

	@Override
	protected void initializeSessionLanguage(final List<Locale> preferredLocales)
	{
		if (preferredLocales != null && !preferredLocales.isEmpty())
		{
			// Find the preferred locale that is in our set of supported languages
			final Collection<LanguageData> storeLanguages = getAllLanguages();
			if (storeLanguages != null && !storeLanguages.isEmpty())
			{
				final LanguageData bestLanguage = findBestLanguage(storeLanguages, preferredLocales);
				if (bestLanguage != null)
				{
					setCurrentLanguage(StringUtils.defaultString(bestLanguage.getIsocode()));
					return;
				}
			}
		}

		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes)
		{
			final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			final String requestedUri = request.getRequestURI();
			final String languageIso = requestedUri.split("/")[3];

			final LanguageModel languageModel = getCommonI18NService().getLanguage(languageIso.trim());
			if (languageModel != null)
			{
				getCommonI18NService().setCurrentLanguage(languageModel);
				return;
			}
		}

		// Try to use the default language for the site
		final LanguageData defaultLanguage = getDefaultLanguage();
		if (defaultLanguage != null)
		{
			setCurrentLanguage(defaultLanguage.getIsocode());
		}
	}
}
