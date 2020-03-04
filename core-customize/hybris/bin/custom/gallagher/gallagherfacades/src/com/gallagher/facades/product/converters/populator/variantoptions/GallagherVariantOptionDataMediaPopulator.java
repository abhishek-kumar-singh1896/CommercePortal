/**
 *
 */
package com.gallagher.facades.product.converters.populator.variantoptions;

import de.hybris.platform.commercefacades.product.converters.populator.variantoptions.VariantOptionDataMediaPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections4.CollectionUtils;


/**
 *
 */
public class GallagherVariantOptionDataMediaPopulator<SOURCE extends VariantProductModel, TARGET extends VariantOptionData>
		extends VariantOptionDataMediaPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final VariantProductModel variantProductModel, final VariantOptionData variantOptionData)
			throws ConversionException
	{
		if (!CollectionUtils.isEmpty(variantProductModel.getGalleryImages())
				&& !CollectionUtils.isEmpty(variantProductModel.getGalleryImages().get(0).getMedias()))
		{
			final Collection<VariantOptionQualifierData> qualifierDataList = new ArrayList<>();


			for (final Iterator<MediaModel> mediaModelIter = variantProductModel.getGalleryImages().get(0).getMedias()
					.iterator(); mediaModelIter.hasNext();)
			{
				final MediaModel mediaModel = mediaModelIter.next();

				final ImageData imageData = new ImageData();
				imageData.setUrl(mediaModel.getURL());
				imageData.setFormat(getMediaFormat(mediaModel.getMediaFormat().getQualifier()));

				final VariantOptionQualifierData qualifierData = new VariantOptionQualifierData();
				qualifierData.setImage(imageData);

				qualifierDataList.add(qualifierData);

				variantOptionData.setVariantOptionQualifiers(qualifierDataList);

			}
		}
	}
}
