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
		if (product != null && mediaFormat != null)
		{
			final Collection<VariantProductModel> variantProducts = product.getVariants();

			if (null != variantProducts && CollectionUtils.isNotEmpty(variantProducts))
			{
				for (final VariantProductModel variantProduct : variantProducts)
				{
					if (variantProduct.isVariantForImage())
					{
						final List<MediaContainerModel> galleryImages = variantProduct.getGalleryImages();
						if (galleryImages != null && !galleryImages.isEmpty())
						{
							for (final MediaContainerModel container : galleryImages)
							{
								try
								{
									final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
									if (media != null)
									{
										return media;
									}
								}
								catch (final ModelNotFoundException ignore)
								{
									// ignore
								}
							}
						}

						break;
					}
				}
			}
			else
			{
				return super.findMedia(product, mediaFormat);
			}
		}
		return null;
	}
}
