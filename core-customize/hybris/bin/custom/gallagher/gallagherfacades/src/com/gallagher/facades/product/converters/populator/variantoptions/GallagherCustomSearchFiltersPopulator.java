/**
 *
 */
package com.gallagher.facades.product.converters.populator.variantoptions;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchFiltersPopulator;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 *
 */
public class GallagherCustomSearchFiltersPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE>
		extends SearchFiltersPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE>
{

	@Override
	public void populate(final SearchQueryPageableData source, final SolrSearchRequest target)
	{

		super.populate(source, target); //<-------------- this is the default populating method

		final SearchQuery searchQuery = (SearchQuery) target.getSearchQuery(); //<-------then we get the target object, the result of the populator job
		final List<FacetValueField> fields = searchQuery.getFacetValues();
		//see the class javadoc
		for (final FacetValueField field : fields)
		{
			if (field.getField().equals("priceValue"))
			{ //<----- we work on the priceValue parameter
				System.out.println("Intercepted a range price filter...");
				String userQuery = null;
				final Set<String> values = field.getValues();
				final Set<String> newValues = new HashSet<String>();
				for (final String value : values)
				{
					if (value != null)
					{
						System.out.println("The search query " + field.getField() + " : " + value + " is transformed in ");
						userQuery = StringUtils.remove(value, '\\'); //<------ we revert the work of the ClientUtils.escapeQueryChars(indexedPropertyValue.getValue()) by removing the backslash
						System.out.println(field.getField() + " : " + userQuery);
						newValues.add(userQuery);
					}
					else
					{
						newValues.add(value);
					}
				}
				field.setValues(newValues);
				searchQuery.setUserQuery("priceValue:" + userQuery); //<-----Finally we add anything in the userQuery. I added the query. We only need it is not empty.


			}
		}
	}
}
