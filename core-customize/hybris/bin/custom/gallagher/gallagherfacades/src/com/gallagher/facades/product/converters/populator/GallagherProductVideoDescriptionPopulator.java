/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashMap;
import java.util.Map;


/**
 * Populate the product data with the product's summary description
 */
public class GallagherProductVideoDescriptionPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setVideoDescription(safeToString(getProductAttribute(productModel, ProductModel.VIDEODESCRIPTION)));
		Map<String, String> videoMap = new HashMap<>();
		final Map<String, String> videoMapData = new HashMap<>();
		videoMap = productModel.getVideos();
		if (null != videoMap)
		{
			for (final Map.Entry<String, String> entry : videoMap.entrySet())
			{
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				videoMapData.put(entry.getKey(), entry.getValue());
			}

		}
		productData.setVideos(videoMapData);
	}
}
