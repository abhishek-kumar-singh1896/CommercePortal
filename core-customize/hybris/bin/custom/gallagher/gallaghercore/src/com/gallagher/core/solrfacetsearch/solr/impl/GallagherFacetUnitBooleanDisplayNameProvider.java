/**
 *
 */
package com.gallagher.core.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * Facet display name provider for boolean units
 *
 * @author gauravkamboj
 */
public class GallagherFacetUnitBooleanDisplayNameProvider implements FacetDisplayNameProvider, FacetValueDisplayNameProvider
{
	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		if (Boolean.valueOf(facetValue))
		{
			return "Yes";
		}
		else
		{
			return "No";
		}
	}

	/**
	 * @deprecated Since 5.4.
	 */
	@Deprecated(since = "5.4")
	@Override
	public String getDisplayName(final SearchQuery query, final String value)
	{
		// XXX Auto-generated method stub
		return null;
	}
}
