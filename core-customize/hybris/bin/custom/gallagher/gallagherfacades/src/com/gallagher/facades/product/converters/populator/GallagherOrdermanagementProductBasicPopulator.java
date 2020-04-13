/**
 *
 */
package com.gallagher.facades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordermanagementfacades.product.converters.populator.OrdermanagementProductBasicPopulator;

import com.microsoft.sqlserver.jdbc.StringUtils;


/**
 *
 */
public class GallagherOrdermanagementProductBasicPopulator extends OrdermanagementProductBasicPopulator
{
	@Override
	public void populate(final ProductModel productModel, final ProductData productData)
	{
		super.populate(productModel, productData);
		final String marketingDescription = (String) getProductAttribute(productModel, ProductModel.MARKETINGDESCRIPTION);
		if (productModel != null && productData != null && !StringUtils.isEmpty(marketingDescription))
		{
			productData.setName(marketingDescription);
		}
	}
}
