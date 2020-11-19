/**
 *
 */
package com.gallagher.core.dao.impl;

import de.hybris.platform.b2b.dao.impl.DefaultB2BUnitDao;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.gallagher.core.dao.GallagherB2BUnitDao;


/**
 * @author ankituniyal
 *
 */
public class GallagherB2BUnitDaoImpl extends DefaultB2BUnitDao implements GallagherB2BUnitDao
{

	private static final String GET_ALL_B2BUNITS = "SELECT DISTINCT {PK} FROM {B2BUNIT} ";

	public List<B2BUnitModel> getAllB2BUnits()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_B2BUNITS);

		final SearchResult<B2BUnitModel> result = this.getFlexibleSearchService().search(query);
		return result.getResult().isEmpty() ? null : result.getResult();
	}

}
