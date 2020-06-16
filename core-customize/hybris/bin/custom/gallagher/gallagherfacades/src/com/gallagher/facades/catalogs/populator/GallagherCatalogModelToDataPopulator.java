/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.catalogs.populator;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.catalogs.populator.CatalogModelToDataPopulator;
import de.hybris.platform.cmsfacades.data.CatalogData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;


/**
 * Populates a {@Link CatalogData} DTO from a {@Link CatalogModel}.
 */
public class GallagherCatalogModelToDataPopulator extends CatalogModelToDataPopulator
{
	@Resource
	private CMSAdminSiteService cmsAdminSiteService;

	@Override
	public void populate(final CatalogModel source, final CatalogData target) throws ConversionException
	{

		final Map<String, String> nameMap = Optional.ofNullable(target.getName()).orElseGet(() -> getNewNameMap(target));
		getLocalizedPopulator().populate( //
				(locale, value) -> nameMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> source.getName(locale) + getCountry());

		target.setCatalogId(source.getId());
	}

	/**
	 * Returns country of the current site to be used for Smart Edit
	 *
	 * @param catalog
	 *           Model
	 * @return Country code
	 */
	private String getCountry()
	{
		String country = Strings.EMPTY;
		final CMSSiteModel site = cmsAdminSiteService.getActiveSite();
		if (site != null && site.getRegionCode() != null && CollectionUtils.isNotEmpty(site.getContentCatalogs()))
		{
			country = " " + site.getRegionCode().getCode().toUpperCase(Locale.ENGLISH);
		}
		return country;
	}

}
