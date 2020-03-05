/**
 *
 */
package com.gallagher.core.search.solrfacetsearch.strategy.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;


/**
 *
 */
public class GallagherDefaultFacetSearchStrategy extends DefaultFacetSearchStrategy
{

	private static final Logger LOG = Logger.getLogger(GallagherDefaultFacetSearchStrategy.class);

	public static String priceRange = null;

	@Override
	public SearchResult search(final SearchQuery searchQuery, final Map<String, String> searchHints) throws FacetSearchException
	{
		this.checkQuery(searchQuery);
		SolrClient solrClient = null;

		try
		{
			final FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
			final IndexedType indexedType = searchQuery.getIndexedType();
			final FacetSearchContext facetSearchContext = this.getFacetSearchContextFactory().createContext(facetSearchConfig,
					indexedType, searchQuery);
			facetSearchContext.getSearchHints().putAll(searchHints);
			this.getFacetSearchContextFactory().initializeContext();
			this.checkContext(facetSearchContext);
			final SolrSearchProvider solrSearchProvider = this.getSolrSearchProviderFactory().getSearchProvider(facetSearchConfig,
					indexedType);
			final SolrIndexModel activeIndex = this.getSolrIndexService().getActiveIndex(facetSearchConfig.getName(),
					indexedType.getIdentifier());
			final Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, activeIndex.getQualifier());
			solrClient = solrSearchProvider.getClient(index);
			final SearchQueryConverterData searchQueryConverterData = new SearchQueryConverterData();
			searchQueryConverterData.setFacetSearchContext(facetSearchContext);
			searchQueryConverterData.setSearchQuery(searchQuery);
			final SolrQuery solrQuery = this.getFacetSearchQueryConverter().convert(searchQueryConverterData);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(solrQuery);
			}

			solrQuery.setGetFieldStatistics(true);
			solrQuery.setGetFieldStatistics("priceValue_aud_double");
			filterPriceQuery(solrQuery);
			final METHOD method = this.resolveQueryMethod(facetSearchConfig);
			final QueryResponse queryResponse = solrClient.query(index.getName(), solrQuery, method);
			final SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
			searchResultConverterData.setFacetSearchContext(facetSearchContext);
			searchResultConverterData.setQueryResponse(queryResponse);
			final SearchResult searchResult = this.getFacetSearchResultConverter().convert(searchResultConverterData);
			this.getFacetSearchContextFactory().getContext().setSearchResult(searchResult);
			this.getFacetSearchContextFactory().destroyContext();
			return searchResult;
		}
		catch (SolrServerException | IOException | RuntimeException | SolrServiceException arg19)
		{
			this.getFacetSearchContextFactory().destroyContext(arg19);
			throw new FacetSearchException(arg19.getMessage(), arg19);
		}
		finally
		{
			IOUtils.closeQuietly(solrClient);
		}

	}

	/**
	 * This method is used to update the price filter and add a new query for price
	 *
	 * @param query
	 *           SolrQuery
	 */
	private void filterPriceQuery(final SolrQuery query)
	{
		final String[] filters = query.getFilterQueries();
		if (filters != null)
		{
			int counter = 0;
			// look for price filter
			for (final String tempFilter : filters)
			{

				if (tempFilter.contains("price"))
				{
					final String filter = tempFilter.replace("}", "}(") + ")";
					// get currency
					final Pattern pattern = Pattern.compile("(\\{.*\\})\\((.*)\\)$");
					final Matcher matcher = pattern.matcher(filter);
					if (matcher.find())
					{
						// modify filter condition

						// add the first part of the filter - removes the facet from the facet list
						final StringBuilder newFilter = new StringBuilder();
						newFilter.append(matcher.group(1));
						// now the actual filter
						newFilter.append("(");

						final String filterBit = matcher.group(2);
						// fix the field name
						final Pattern pattern2 = Pattern.compile("price_(\\w\\w\\w)_string");
						final Matcher matcher2 = pattern2.matcher(filterBit);
						matcher2.find();
						final String fieldName = "priceValue_" + matcher2.group(1) + "_double_mv";

						String fieldReplace = filterBit.replaceAll("price_\\w\\w\\w_string",
								"priceValue_" + matcher2.group(1) + "_double");
						fieldReplace = fieldReplace.replaceAll("\\$", "").replaceAll("\\\\", "");

						if (filterBit.contains(" OR "))
						{
							// multiple price ranges
							fieldReplace = fieldReplace.replaceAll("\\:\\(", ":[");
							fieldReplace = fieldReplace.replaceAll("\\sOR\\s", "] OR " + fieldName + ":[");
							fieldReplace = fieldReplace.substring(0, fieldReplace.length() - 1);
						}
						else
						{
							// single price range
							fieldReplace = fieldReplace.replaceAll("\\:", ":[");
						}

						fieldReplace = fieldReplace.replaceAll("-", " TO ");
						newFilter.append(fieldReplace).append("]");
						newFilter.append(")");

						filters[counter] = newFilter.toString();

						// Handle Price Without Range
						if (!filters[counter].contains(" TO ") && priceRange != null)
						{
							filters[counter] = newFilter.toString().substring(0, newFilter.toString().indexOf(":[")) + ":[" + priceRange
									+ "])";
						}

					}
					break;
				}
				counter++;
			}
		}
	}
}
