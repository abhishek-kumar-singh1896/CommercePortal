/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.gallagher.core.enums.Animal;


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
		target.setDescription(source.getDescription());
		if (null != source.getPromoSticker())
		{
			target.setPromoSticker(source.getPromoSticker().getCode());
		}
		if (CollectionUtils.isNotEmpty(source.getAnimalCompatibility()))
		{
			final List<String> animals = new ArrayList<String>();
			for (final Animal a : source.getAnimalCompatibility())
			{
				System.out.println("animalCode>>" + a.getCode());
				//				animals.add(enumerationService.getEnumerationValue(Animal.class, source.getAnimalCompatibility()));
			}
			target.setAnimalCompatibility(animals);
		}
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
