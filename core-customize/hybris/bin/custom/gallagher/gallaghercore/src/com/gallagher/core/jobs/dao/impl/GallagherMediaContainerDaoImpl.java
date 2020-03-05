/**
 *
 */
package com.gallagher.core.jobs.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.media.dao.impl.DefaultMediaContainerDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gallagher.core.jobs.dao.GallagherMediaContainerDao;


/**
 *
 */
public class GallagherMediaContainerDaoImpl extends DefaultMediaContainerDao implements GallagherMediaContainerDao
{


	private static final Logger LOGGER = Logger.getLogger(GallagherMediaContainerDaoImpl.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(GallagherMediaContainerDaoImpl.class);

	public List<MediaContainerModel> getMediaContainer(final String containerName, final PK pk)
	{

		final String query = "select {PK} from {MediaContainer} where {qualifier} IN ('" + containerName
				+ "') and {catalogversion}= '" + pk + "'";
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		LOGGER.info("QUERY " + flexibleSearchQuery.getQuery().toString());
		final SearchResult<MediaContainerModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);
		final SearchResult<ProductModel> result = flexibleSearchService.search(query);
		return searchResult.getResult();

	}


	public List<ProductModel> getProductModeList(final ArrayList<String> property_part_numbers, final PK pk)
	{

		final String query = "select {PK} from {Product} where {code} in (?code) and {catalogversion} = '" + pk + "'";
		final Map<String, Object> queryParams = new HashMap<>();
		final List productCodes = new ArrayList<>();
		for (final String ids : property_part_numbers)
		{
			productCodes.add(ids);
		}
		queryParams.put("code", productCodes);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameters(queryParams);
		LOGGER.info("Query " + flexibleSearchQuery.getQuery());
		final SearchResult<ProductModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);
		LOGGER.info("SearchResult Count " + searchResult.getCount());
		if (searchResult.getCount() > 0)
		{

			return searchResult.getResult();
		}
		return null;


	}

}
