/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPrimaryImagePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Populate the product data with the product's primary image
 *
 * @author shishirkant
 *
 */
public class GallagherProductPrimaryImagePopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductPrimaryImagePopulator<SOURCE, TARGET>
{
	@Override
	protected MediaContainerModel getPrimaryImageMediaContainer(final SOURCE productModel)
	{
		final Collection<VariantProductModel> variantProducts = productModel.getVariants();

		if (null != variantProducts && CollectionUtils.isNotEmpty(variantProducts))
		{
			for (final VariantProductModel variantProduct : variantProducts)
			{
				if (variantProduct.isVariantForImage())
				{
					final MediaModel picture = (MediaModel) getProductAttribute(variantProduct, ProductModel.PICTURE);
					if (picture != null)
					{
						return picture.getMediaContainer();
					}

					break;
				}
			}
		}
		else
		{
			super.getPrimaryImageMediaContainer(productModel);
		}
		return null;
	}
}
