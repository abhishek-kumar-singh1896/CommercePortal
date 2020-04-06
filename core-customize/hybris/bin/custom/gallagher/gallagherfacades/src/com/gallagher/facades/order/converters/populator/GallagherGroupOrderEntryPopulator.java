/**
 *
 */
package com.gallagher.facades.order.converters.populator;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.commercefacades.order.converters.populator.GroupOrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import javax.annotation.Resource;


/**
 *
 */
public class GallagherGroupOrderEntryPopulator extends GroupOrderEntryPopulator<AbstractOrderModel, AbstractOrderData>
{

	@Resource(name = "productService")
	private ProductService productService;

	@Override
	protected ProductData createBaseProduct(final ProductData variant)
	{
		final ProductData productData = new ProductData();

		productData.setUrl(variant.getUrl());
		productData.setPurchasable(variant.getPurchasable());
		productData.setMultidimensional(Boolean.TRUE);
		productData.setImages(variant.getImages());

		final ProductModel productModel = productService.getProductForCode(variant.getBaseProduct());
		productData.setCode(productModel.getCode());
		productData.setName(productModel.getMarketingDescription());
		productData.setDescription(productModel.getDescription());

		productData.setPriceRange(new PriceRangeData());

		return productData;
	}

}
