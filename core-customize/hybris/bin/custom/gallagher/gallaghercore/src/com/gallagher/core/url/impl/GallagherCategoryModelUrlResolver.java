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
		return super.resolveInternal(source).toLowerCase();

	}

}
