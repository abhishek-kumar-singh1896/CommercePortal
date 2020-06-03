/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.core.url.impl;

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
		return super.resolveInternal(source).toLowerCase();
	}

}
