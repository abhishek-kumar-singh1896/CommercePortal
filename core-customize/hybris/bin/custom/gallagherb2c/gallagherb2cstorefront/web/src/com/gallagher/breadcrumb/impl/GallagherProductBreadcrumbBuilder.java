package com.gallagher.breadcrumb.impl;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.core.model.product.ProductModel;

import org.apache.commons.lang.StringUtils;


/**
 * @author shilpiverma
 *
 *         Override ProductBreadcrumbBuilder class. If marketing description is not empty show marketing description
 *         else show name
 */
public class GallagherProductBreadcrumbBuilder extends ProductBreadcrumbBuilder
{
	@Override
	protected Breadcrumb getProductBreadcrumb(final ProductModel product)
	{
		final String productUrl = getProductModelUrlResolver().resolve(product);
		String productName = product.getName();
		if (StringUtils.isNotEmpty(product.getMarketingDescription()))
		{
			productName = product.getMarketingDescription();
		}
		return new Breadcrumb(productUrl, productName, null);
	}

}
