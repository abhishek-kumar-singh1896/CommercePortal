package com.gallagher.core.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.ManagedResource;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrStandaloneSearchProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;


/**
 * Custom Search provider to export configs
 *
 * @author Vikram Bishnoi
 *
 */
public class GallagherSolrStandaloneSearchProvider extends SolrStandaloneSearchProvider
{
	@Override
	protected void exportConfig(final Index index, final SolrClient solrClient)
			throws SolrServiceException, SolrServerException, IOException
	{
		final Map<String, ManagedResource> managedResources = this.loadManagedResourcesFromServer(index, solrClient);
		final List<String> languages = this.collectLanguages(index.getFacetSearchConfig());
		/* Make the languages in lowercase */
		languages.replaceAll(String::toLowerCase);
		this.exportSynonyms(index, solrClient, managedResources, languages);
		this.exportStopWords(index, solrClient, managedResources, languages);
	}


	@Override
	protected Map<String, Map<String, Set<String>>> loadSynonymsFromConfiguration(final FacetSearchConfig facetSearchConfig,
			final Collection<String> languages)
	{
		final Map<String, Map<String, Set<String>>> synonyms = new HashMap();
		languages.forEach(language -> synonyms.put(language, new HashMap()));

		final SolrFacetSearchConfigModel solrFacetSearchConfigModel = getSolrFacetSearchConfigDao()
				.findFacetSearchConfigByName(facetSearchConfig.getName());

		solrFacetSearchConfigModel.getSynonyms().forEach(solrSynonymConfigModel -> {
			final String key = solrSynonymConfigModel.getLanguage().getIsocode().toLowerCase();
			final Map<String, Set<String>> values = synonyms.get(key);
			if (values != null)
			{
				this.buildSynonyms(values, solrSynonymConfigModel.getSynonymFrom(), solrSynonymConfigModel.getSynonymTo());
			}
		});

		return synonyms;
	}

	@Override
	protected Map<String, Set<String>> loadStopWordsFromConfiguration(final FacetSearchConfig facetSearchConfig,
			final Collection<String> languages)
	{
		final Map<String, Set<String>> stopWords = new HashMap();
		languages.forEach(language -> stopWords.put(language, new HashSet()));


		final SolrFacetSearchConfigModel solrFacetSearchConfigModel = this.getSolrFacetSearchConfigDao()
				.findFacetSearchConfigByName(facetSearchConfig.getName());

		solrFacetSearchConfigModel.getStopWords().forEach(solrStopWordModel -> {
			final String key = solrStopWordModel.getLanguage().getIsocode().toLowerCase();
			final Set<String> values = stopWords.get(key);

			if (values != null)
			{
				values.add(solrStopWordModel.getStopWord());
			}
		});

		return stopWords;
	}
}
