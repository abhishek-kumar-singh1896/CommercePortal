/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.url.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * URL resolver for ProductModel instances. The pattern could be of the form:
 * /{category-path}/{product-name}/p/{product-code}. This is customized to keep the URL path in lowercase (SAPP-574)
 *
 * @author Vikram Bishnoi
 */
public class GallagherProductModelUrlResolver extends DefaultProductModelUrlResolver
{

	@Override
	protected String resolveInternal(final ProductModel source)
	{
		final ProductModel baseProduct = getProductAndCategoryHelper().getBaseProduct(source);

		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		String url = getPattern();

		if (currentBaseSite != null && url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", urlEncode(currentBaseSite.getUid()));
		}
		if (url.contains("{category-path}"))
		{
			url = url.replace("{category-path}", buildPathString(getCategoryPath(baseProduct)));
		}

		if (url.contains("{product-name}"))
		{
			url = url.replace("{product-name}", urlSafe(baseProduct.getName()));
		}
		url = url.toLowerCase();
		if (url.contains("{product-code}"))
		{
			url = url.replace("{product-code}", urlEncode(source.getCode()));
		}

		return url;
	}

}
