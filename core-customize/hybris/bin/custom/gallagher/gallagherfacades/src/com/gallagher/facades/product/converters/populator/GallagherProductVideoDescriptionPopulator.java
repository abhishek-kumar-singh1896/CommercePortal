/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;


/**
 * Populate the product data with the product's summary description
 */
public class GallagherProductVideoDescriptionPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setVideoDescription(safeToString(getProductAttribute(productModel, ProductModel.VIDEODESCRIPTION)));
		populateApplicableVideos(productModel, productData);
	}

	/**
	 * Populates video according to the regions assigned
	 *
	 * @param productModel
	 *           to get the media
	 * @param productData
	 *           to be populated with Media
	 */
	private void populateApplicableVideos(final SOURCE productModel, final TARGET productData)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();
		if (MapUtils.isNotEmpty(productModel.getVideos()) && currentSite != null && currentSite.getRegionCode() != null)
		{
			final String regionCode = currentSite.getRegionCode().getCode();
			final Map<String, String> videos = new HashMap<>(productModel.getVideos().size());
			productModel.getVideos().forEach((key, value) -> {
				final String[] regionThumbArray = value.split("\\|");
				if (regionThumbArray.length == 2 && regionThumbArray[0].contains(regionCode))
				{
					videos.put(key, regionThumbArray[1]);
				}
			});
		}
	}
}
