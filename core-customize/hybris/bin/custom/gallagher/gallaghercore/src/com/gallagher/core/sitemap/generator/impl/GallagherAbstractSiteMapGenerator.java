package com.gallagher.core.sitemap.generator.impl;


import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.acceleratorservices.sitemap.generator.impl.AbstractSiteMapGenerator;
import de.hybris.platform.acceleratorservices.sitemap.renderer.SiteMapContext;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;


public abstract class GallagherAbstractSiteMapGenerator<T> extends AbstractSiteMapGenerator<T>
{

	@Resource
	private I18NService i18NService;

	private ApplicationContext applicationContext;

	@Override
	public File render(final CMSSiteModel site, final CurrencyModel currencyModel, final LanguageModel languageModel,
			final RendererTemplateModel rendererTemplateModel, final List<T> models, final String filePrefix, final Integer index)
			throws IOException
	{
		final String prefix = (index != null)
				? String.format(filePrefix + "-%s-%s-%s-", languageModel.getIsocode(), currencyModel.getIsocode(), index)
				: String.format(filePrefix + "-%s-%s-", languageModel.getIsocode(), currencyModel.getIsocode());
		final File siteMap = File.createTempFile(prefix, ".xml");

		final ImpersonationContext context = new ImpersonationContext();
		context.setSite(site);
		context.setCurrency(currencyModel);
		context.setLanguage(languageModel);

		return getImpersonationService().executeInContext(context, () -> {
			i18NService.setLocalizationFallbackEnabled(true);
			final List<SiteMapUrlData> siteMapUrlDataList = getSiteMapUrlData(models);
			final SiteMapContext siteMapContext = (SiteMapContext) applicationContext.getBean("siteMapContext");
			siteMapContext.init(site, getSiteMapPageEnum());
			siteMapContext.setSiteMapUrlData(siteMapUrlDataList);
			try (final BufferedWriter output = new BufferedWriter(new FileWriter(siteMap)))
			{
				// the template media is loaded only for english language.
				getCommonI18NService().setCurrentLanguage(getCommonI18NService().getLanguage("en"));
				getRendererService().render(rendererTemplateModel, siteMapContext, output);
			}
			return siteMap;
		});
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
