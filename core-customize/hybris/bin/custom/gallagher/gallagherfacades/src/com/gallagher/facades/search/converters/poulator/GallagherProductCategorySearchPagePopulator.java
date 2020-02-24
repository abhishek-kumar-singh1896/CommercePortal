/**
 *
 */
package com.gallagher.facades.search.converters.poulator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.ProductCategorySearchPagePopulator;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;

import javax.annotation.Resource;


/**
 * @author gauravkamboj
 *
 */
public class GallagherProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductData, SCAT, CATEGORY>
		extends ProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM, SCAT, CATEGORY>
{
	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ITEM, CATEGORY> target)
	{
		super.populate(source, target);
		target.setDescription(getCommerceCategoryService().getCategoryForCode(source.getCategoryCode()).getDescription());

	}

	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

}
