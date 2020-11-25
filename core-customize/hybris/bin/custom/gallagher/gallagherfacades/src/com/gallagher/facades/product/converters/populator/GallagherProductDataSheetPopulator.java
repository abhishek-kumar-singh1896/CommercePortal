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
import de.hybris.platform.store.BaseStoreModel;

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
		final Collection<MediaModel> dataSheets = getDataSheets(productModel);
		if (dataSheets != null)
		{
			final List<ImageData> imageList = new ArrayList<ImageData>();

			// Use the first container as the primary image
			addDataSheets(dataSheets, ImageDataType.DATASHEET, imageList);

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}

			}
			productData.setDataSheet(imageList);
		}
	}

	protected Collection<MediaModel> getDataSheets(final SOURCE productModel)
	{
		final Collection<MediaModel> dataSheets = (Collection<MediaModel>) getProductAttribute(productModel,
				ProductModel.DATA_SHEET);
		//this is else part
		if (CollectionUtils.isNotEmpty(dataSheets))
		{
			return dataSheets;
		}
		return null;

	}

	protected void addDataSheets(final Collection<MediaModel> dataSheets, final ImageDataType imageType,
			final List<ImageData> list)
	{
		for (final MediaModel dataSheet : dataSheets)
		{
			final ImageData imageData = getImageConverter().convert(dataSheet);
			imageData.setDescription(dataSheet.getRealFileName());
			imageData.setMime(dataSheet.getMime());
			imageData.setImageType(imageType);
			imageData.setSize(dataSheet.getSize() / 1000000.0 + " mb");
			final List<String> baseStoreList = new ArrayList<>();
			if (null != dataSheet.getBaseStores())
			{
				for (final BaseStoreModel store : dataSheet.getBaseStores())
				{
					baseStoreList.add(store.getUid());
				}
			}
			imageData.setBaseStoreCodes(baseStoreList);
			list.add(imageData);
		}
	}
}

