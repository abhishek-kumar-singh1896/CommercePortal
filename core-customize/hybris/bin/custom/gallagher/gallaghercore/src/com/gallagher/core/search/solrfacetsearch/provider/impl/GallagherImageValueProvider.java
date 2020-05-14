package com.gallagher.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ImageValueProvider;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * This ValueProvider will provide the product's image url for the first gallery image that supports the requested media
 * format.
 *
 * @author shishirkant
 *
 */
public class GallagherImageValueProvider extends ImageValueProvider
{
	private static final Logger LOGGER = Logger.getLogger(GallagherImageValueProvider.class);

	@Override
	protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		MediaModel requestedMedia = null;
		if (product != null && mediaFormat != null)
		{
			final Collection<VariantProductModel> variantProducts = product.getVariants();

			if (CollectionUtils.isNotEmpty(variantProducts))
			{
				for (final VariantProductModel variantProduct : variantProducts)
				{
					if (variantProduct.isVariantForImage())
					{
						requestedMedia = getRequiredMedia(mediaFormat, variantProduct.getGalleryImages());
						break;
					}
				}
			}
			else
			{
				requestedMedia = getRequiredMedia(mediaFormat, product.getGalleryImages());
			}
		}
		return requestedMedia;
	}

	/**
	 * Returns the required format media from the gallery images. It will give the priority to Hero media and it will
	 * return the first media if Hero is not available
	 *
	 * @param mediaFormat
	 *           to be returned
	 * @param galleryImages
	 *           to get media with the required format
	 * @return required media
	 */
	private MediaModel getRequiredMedia(final MediaFormatModel mediaFormat, final List<MediaContainerModel> galleryImages)
	{
		MediaModel requestedMedia = null;
		if (CollectionUtils.isNotEmpty(galleryImages))
		{
			// Search each media container in the gallery for an image of the right format
			for (final MediaContainerModel container : galleryImages)
			{
				try
				{
					final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
					if (media != null)
					{
						if (Boolean.TRUE.equals(container.getHero()))
						{
							requestedMedia = media;
							break;
						}
						else if (requestedMedia == null)
						{
							requestedMedia = media;
						}
					}
				}
				catch (final ModelNotFoundException ignore)
				{
					// ignore
				}
			}
		}
		return requestedMedia;
	}
}
