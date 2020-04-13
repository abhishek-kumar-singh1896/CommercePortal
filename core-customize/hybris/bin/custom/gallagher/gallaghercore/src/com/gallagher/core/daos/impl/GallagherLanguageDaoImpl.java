package com.gallagher.core.daos.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.gallagher.core.daos.GallagherLanguageDao;


/**
 * {@inheritDoc}
 */
public class GallagherLanguageDaoImpl implements GallagherLanguageDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LanguageModel getLanguageByIsoCode(final String isoCode)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT {").append(LanguageModel.PK);
		queryBuilder.append("} from {").append(LanguageModel._TYPECODE);
		queryBuilder.append("} where {").append(LanguageModel.ISOCODE);
		queryBuilder.append("} = ?").append(LanguageModel.ISOCODE);

		final Map<String, String> queryParams = new HashMap<>(1);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryBuilder.toString());
		queryParams.put(LanguageModel.ISOCODE, isoCode.toLowerCase());
		fQuery.addQueryParameters(queryParams);

		final SearchResult<LanguageModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult().size() > 0 ? searchResult.getResult().get(0) : null;
	}

}
