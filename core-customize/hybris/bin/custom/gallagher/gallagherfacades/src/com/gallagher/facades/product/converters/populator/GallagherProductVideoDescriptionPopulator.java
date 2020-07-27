/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductVideoData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;


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
			final List<ProductVideoData> videos = new ArrayList<>(productModel.getVideos().size());
			productData.setVideos(videos);
			productModel.getVideos().forEach((key, value) -> {
				if (StringUtils.isNotEmpty(value))
				{
					final String[] regionThumbArray = value.split("\\|");
					if (regionThumbArray.length >= 2 && regionThumbArray[0].contains(regionCode))
					{
						final ProductVideoData video = new ProductVideoData();
						video.setId(key);
						video.setUrl(regionThumbArray[1]);
						if (regionThumbArray.length > 2)
						{
							video.setDescription(regionThumbArray[2]);
						}
						videos.add(video);
					}
				}
			});
		}
	}
}
