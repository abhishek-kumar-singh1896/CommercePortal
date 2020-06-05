/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.url.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.DefaultCategoryModelUrlResolver;


/**
 * URL resolver for CategoryModel instances. The pattern could be of the form: /{category-path}/c/{category-code}. This
 * is customized to keep the URL path in lowercase (SAPP-574)
 *
 * @author Vikram Bishnoi
 */
public class GallagherCategoryModelUrlResolver extends DefaultCategoryModelUrlResolver
{

	@Override
	protected String resolveInternal(final CategoryModel source)
	{
		// Work out values

		// Replace pattern values
		String url = getPattern();
		if (url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", urlEncode(getBaseSiteUid().toString()));
		}
		if (url.contains("{category-path}"))
		{
			final String categoryPath = buildPathString(getCategoryPath(source));
			url = url.replace("{category-path}", categoryPath);
		}
		if (url.contains("{catalog-id}"))
		{
			url = url.replace("{catalog-id}", urlEncode(source.getCatalogVersion().getCatalog().getId()));
		}
		if (url.contains("{catalogVersion}"))
		{
			url = url.replace("{catalogVersion}", urlEncode(source.getCatalogVersion().getVersion()));
		}
		url = url.toLowerCase();
		if (url.contains("{category-code}"))
		{
			final String categoryCode = urlEncode(source.getCode()).replaceAll("\\+", "%20");
			url = url.replace("{category-code}", categoryCode);
		}
		return url;

	}

}
