/**
 *
 */
package com.gallagher.core.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Facet display name provider to add unit to the value
 *
 * @author gauravkamboj
 */
public class GallagherFacetUnitSuffixDisplayNameProvider implements FacetDisplayNameProvider, FacetValueDisplayNameProvider
{
	private static final Logger LOG = Logger.getLogger(GallagherFacetUnitSuffixDisplayNameProvider.class);
	private final Map<String, Locale> localeCache = new HashMap<>();

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		final Locale locale = getLocale(query.getLanguage());
		final String unit = property.getClassAttributeAssignment().getUnit().getSymbol();
		LOG.info("unit>>" + unit + "- name value>>" + property.getClassAttributeAssignment().getUnit().getName(locale));
		return facetValue + " " + unit;
	}

	/**
	 * @deprecated Since 5.4.
	 */
	@Deprecated(since = "5.4")
	@Override
	public String getDisplayName(final SearchQuery var1, final String var2)
	{
		// XXX Auto-generated method stub
		return null;
	}

	protected Locale getLocale(final String isoCode)
	{
		Locale result;
		result = localeCache.get(isoCode);
		if (result == null)
		{
			final String[] splitted_code = isoCode.split("_");
			if (splitted_code.length == 1)
			{
				result = new Locale(splitted_code[0]);
			}
			else
			{
				result = new Locale(splitted_code[0], splitted_code[1]);
			}

			localeCache.put(isoCode, result);
		}
		return result;
	}

}
