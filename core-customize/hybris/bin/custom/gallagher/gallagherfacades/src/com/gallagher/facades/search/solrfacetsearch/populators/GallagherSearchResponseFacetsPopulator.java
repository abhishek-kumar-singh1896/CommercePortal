/**
 *
 */
package com.gallagher.facades.search.solrfacetsearch.populators;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseFacetsPopulator;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Collection;

import javax.annotation.Resource;


/**
 *
 */
public class GallagherSearchResponseFacetsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		extends
		SearchResponseFacetsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
{

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Override
	@SuppressWarnings("unused")
	protected FacetValueData<SolrSearchQueryData> buildFacetValue(final FacetData<SolrSearchQueryData> facetData,
			final Facet facet, final FacetValue facetValue, final SearchResult searchResult,
			final SolrSearchQueryData searchQueryData)
	{
		if (facetData.isCategory() && null != searchQueryData.getCategoryCode())
		{
			final CategoryModel currentCategory = getCategoryModelInstance(
					categoryService.getCategoriesForCode(searchQueryData.getCategoryCode()));
			final CategoryModel facetCategory = getCategoryModelInstance(categoryService.getCategoriesForCode(facetValue.getName()));
			if (currentCategory != null && facetCategory != null && facetCategory.getSupercategories().contains(currentCategory))
			{
				final FacetValueData<SolrSearchQueryData> facetValueData = createFacetValueData();
				facetValueData.setCode(facetValue.getName());
				facetValueData.setName(facetValue.getDisplayName());
				facetValueData.setCount(facetValue.getCount());

				// Check if the facet is selected
				facetValueData.setSelected(isFacetSelected(searchQueryData, facet.getName(), facetValue.getName()));

				if (facetValueData.isSelected())
				{
					// Query to remove, rather than add facet
					facetValueData.setQuery(refineQueryRemoveFacet(searchQueryData, facet.getName(), facetValue.getName()));
				}
				else
				{
					// Query to add the facet
					facetValueData.setQuery(refineQueryAddFacet(searchQueryData, facet.getName(), facetValue.getName()));
				}
				return facetValueData;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return super.buildFacetValue(facetData, facet, facetValue, searchResult, searchQueryData);
		}
	}

	protected CategoryModel getCategoryModelInstance(final Collection<CategoryModel> categories)
	{
		for (final CategoryModel category : categories)
		{
			if (!(category instanceof ClassificationClassModel))
			{
				return category;
			}
		}
		return null;
	}
}
