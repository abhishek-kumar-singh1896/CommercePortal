/**
 *
 */
package com.gallagher.core.solrfacetsearch.solr.impl;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 * Facet display name provider for boolean units
 *
 * @author gauravkamboj
 */
public class GallagherFacetUnitBooleanDisplayNameProvider implements FacetDisplayNameProvider, FacetValueDisplayNameProvider
{
	private static final Logger LOG = Logger.getLogger(GallagherFacetUnitBooleanDisplayNameProvider.class);

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		if (Boolean.valueOf(facetValue))
		{
			if (commerceCommonI18NService.getCurrentLanguage().getIsocode().equals("es_CL"))
			{
				return "Si";
			}
			else if (commerceCommonI18NService.getCurrentLanguage().getIsocode().equals("fr_CA"))
			{
				return "Oui";
			}
			else
			{
				return "Yes";
			}
		}
		else
		{
			if (commerceCommonI18NService.getCurrentLanguage().getIsocode().equals("fr_CA"))
			{
				return "Non";
			}
			else
			{
				return "No";
			}
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
