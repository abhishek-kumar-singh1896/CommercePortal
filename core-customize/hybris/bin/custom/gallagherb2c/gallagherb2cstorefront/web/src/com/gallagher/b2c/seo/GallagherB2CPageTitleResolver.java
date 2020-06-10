/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.b2c.seo;

import de.hybris.platform.acceleratorservices.storefront.util.PageTitleResolver;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.helper.ProductAndCategoryHelper;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Resolves page title according to page, search text, current category or product
 */
public class GallagherB2CPageTitleResolver extends PageTitleResolver
{
	protected static final String TITLE_WORD_SEPARATOR = " | ";

	/**
	 * creates page title for given code
	 */
	public String resolveProductPageTitle(final ProductModel product)
	{
		// Lookup categories
		final List<CategoryModel> path = getCategoryPath(getProductAndCategoryHelper().getBaseProduct(product));
		// Lookup site (or store)
		final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();

		// Construct page title
		final String identifier = product.getName();
		final String mktDescription = product.getMarketingDescription();
		final String articleNumber = product.getCode();
		final String productName = StringUtils.isEmpty(mktDescription) ? articleNumber : mktDescription;
		final StringBuilder builder = new StringBuilder(productName);

		for (final CategoryModel pathElement : path)
		{
			builder.append(TITLE_WORD_SEPARATOR).append(pathElement.getName());
		}

		if (currentSite != null)
		{
			builder.append(TITLE_WORD_SEPARATOR).append(currentSite.getName());
		}

		return StringEscapeUtils.escapeHtml(builder.toString());
	}

	public String resolveProductPageTitle(final String productCode)
	{
		// Lookup the product
		final ProductModel product = getProductService().getProductForCode(productCode);
		return resolveProductPageTitle(product);
	}

	protected List<CategoryModel> getCategoryPath(final ProductModel product)
	{
		final CategoryModel category = getPrimaryCategoryForProduct(product);
		if (category != null)
		{
			return getCategoryPath(category);
		}
		return Collections.emptyList();
	}

	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> paths = getCommerceCategoryService().getPathsForCategory(category);
		// Return first - there will always be at least 1
		final List<CategoryModel> cat2ret = paths.iterator().next();
		Collections.reverse(cat2ret);
		return cat2ret;
	}

	protected CategoryModel getPrimaryCategoryForProduct(final ProductModel product)
	{
		// Get the first super-category from the product that isn't a classification category
		for (final CategoryModel category : product.getSupercategories())
		{
			if (getProductAndCategoryHelper().isValidProductCategory(category))
			{
				return category;
			}
		}
		return null;
	}



}
