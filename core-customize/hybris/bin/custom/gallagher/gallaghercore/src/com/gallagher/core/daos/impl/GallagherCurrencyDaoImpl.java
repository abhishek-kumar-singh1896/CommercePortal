/**
 *
 */
package com.gallagher.core.daos.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.gallagher.core.daos.GallagherCurrencyDao;


/**
 * {@inheritDoc}
 */
public class GallagherCurrencyDaoImpl implements GallagherCurrencyDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CurrencyModel getCurrencyByCountryIsoCode(final String isoCode)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT {").append(CurrencyModel.PK);
		queryBuilder.append("} from {").append(CurrencyModel._TYPECODE);
		queryBuilder.append("} where {").append(CurrencyModel.ISOCODE);
		queryBuilder.append("} LIKE ?").append(CurrencyModel.ISOCODE);

		final Map<String, String> queryParams = new HashMap<>(1);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryBuilder.toString());
		queryParams.put(CurrencyModel.ISOCODE, isoCode.toUpperCase().concat("%"));
		fQuery.addQueryParameters(queryParams);

		final SearchResult<CurrencyModel> searchResult = flexibleSearchService.search(fQuery);

		return searchResult.getResult().size() > 0 ? searchResult.getResult().get(0) : null;
	}

}
