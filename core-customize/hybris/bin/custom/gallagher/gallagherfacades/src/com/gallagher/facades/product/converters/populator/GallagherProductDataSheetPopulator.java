/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductImagePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Populate the product data with the product's summary description
 */
public class GallagherProductDataSheetPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductImagePopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final Collection<MediaModel> logos = getPrimaryImageMediaContainer(productModel);
		if (logos != null)
		{
			final List<ImageData> imageList = new ArrayList<ImageData>();

			// Use the first container as the primary image
			addLogos(logos, ImageDataType.DATASHEET, imageList);

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}
			}
			productData.setLogo(imageList);
		}
	}

	protected Collection<MediaModel> getPrimaryImageMediaContainer(final SOURCE productModel)
	{
		final Collection<MediaModel> logos = (Collection<MediaModel>) getProductAttribute(productModel, ProductModel.DATA_SHEET);
		//this is else part
		if (CollectionUtils.isNotEmpty(logos))
		{
			return logos;
		}
		return null;
	}

	protected void addLogos(final Collection<MediaModel> logos, final ImageDataType imageType, final List<ImageData> list)
	{
		for (final MediaModel logo : logos)
		{
			final ImageData imageData = getImageConverter().convert(logo);
			imageData.setDescription(logo.getDescription());
			imageData.setImageType(imageType);
			list.add(imageData);
		}
	}
}

