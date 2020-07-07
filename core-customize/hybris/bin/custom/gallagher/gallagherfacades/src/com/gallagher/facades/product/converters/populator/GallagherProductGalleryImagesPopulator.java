/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductGalleryImagesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Populate the product data with the product's gallery images
 *
 * @author shishirkant
 *
 */
public class GallagherProductGalleryImagesPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductGalleryImagesPopulator<SOURCE, TARGET>
{
	@Override
	protected void collectMediaContainers(final ProductModel productModel, final List<MediaContainerModel> list)
	{
		final Collection<VariantProductModel> variantProducts = productModel.getVariants();

		if (null != variantProducts && CollectionUtils.isNotEmpty(variantProducts))
		{
			for (final VariantProductModel variantProduct : variantProducts)
			{
				if (variantProduct.isVariantForImage())
				{
					final List<MediaContainerModel> galleryImages = (List<MediaContainerModel>) getProductAttribute(variantProduct,
							ProductModel.GALLERYIMAGES);

					if (galleryImages != null)
					{
						collectHeroMediaContainers(galleryImages, list);
					}
					break;
				}
			}
		}
		else
		{
			final List<MediaContainerModel> galleryImages = (List<MediaContainerModel>) getProductAttribute(productModel,
					ProductModel.GALLERYIMAGES);
			if (galleryImages != null)
			{
				collectHeroMediaContainers(galleryImages, list);

				if (galleryImages.isEmpty() && productModel instanceof VariantProductModel)
				{
					collectMediaContainers(((VariantProductModel) productModel).getBaseProduct(), list);
				}
			}
		}
	}

	protected void collectHeroMediaContainers(final List<MediaContainerModel> galleryImages, final List<MediaContainerModel> list)
	{
		for (final MediaContainerModel galleryImage : galleryImages)
		{
			if (!list.contains(galleryImage) && Boolean.TRUE.equals(galleryImage.getHero()))
			{
				list.add(galleryImage);
				break;
			}
		}
		for (final MediaContainerModel galleryImage : galleryImages)
		{

			if (!list.contains(galleryImage) && !Boolean.TRUE.equals(galleryImage.getHero()))
			{
				list.add(galleryImage);
			}
		}
	}

}
