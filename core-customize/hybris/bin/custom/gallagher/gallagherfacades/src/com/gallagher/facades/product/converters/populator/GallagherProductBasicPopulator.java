/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import com.microsoft.sqlserver.jdbc.StringUtils;


/**
 *
 */
public class GallagherProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductBasicPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData)
	{
		super.populate(productModel, productData);
		final String productMarktingDescription = (String) getProductAttribute(productModel, ProductModel.MARKETINGDESCRIPTION);
		if (!StringUtils.isEmpty(productMarktingDescription))
		{
			productData.setName(productMarktingDescription);
		}
		productData.setPartNumber((String) getProductAttribute(productModel, ProductModel.PARTNUMBER));
	}
}
