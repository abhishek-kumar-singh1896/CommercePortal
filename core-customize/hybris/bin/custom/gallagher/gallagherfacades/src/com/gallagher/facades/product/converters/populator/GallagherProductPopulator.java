/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * @author shilpiverma
 *
 */
public class GallagherProductPopulator extends ProductPopulator
{
	private Populator<ProductModel, ProductData> productPricePopulator;
	private Populator<ProductModel, ProductData> productGalleryImagesPopulator;


	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		getProductPricePopulator().populate(source, target);
		getProductGalleryImagesPopulator().populate(source, target);
		super.populate(source, target);
	}

	/**
	 * @return the productGalleryImagesPopulator
	 */
	public Populator<ProductModel, ProductData> getProductGalleryImagesPopulator()
	{
		return productGalleryImagesPopulator;
	}

	/**
	 * @param productGalleryImagesPopulator
	 *           the productGalleryImagesPopulator to set
	 */
	public void setProductGalleryImagesPopulator(final Populator<ProductModel, ProductData> productGalleryImagesPopulator)
	{
		this.productGalleryImagesPopulator = productGalleryImagesPopulator;
	}

	/**
	 * @return the productPricePopulator
	 */
	public Populator<ProductModel, ProductData> getProductPricePopulator()
	{
		return productPricePopulator;
	}

	/**
	 * @param productPricePopulator
	 *           the productPricePopulator to set
	 */
	public void setProductPricePopulator(final Populator<ProductModel, ProductData> productPricePopulator)
	{
		this.productPricePopulator = productPricePopulator;
	}

}
