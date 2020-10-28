/**
 *
 */
package com.gallagher.core.dao.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.gallagher.core.dao.GallagherSalesAreaDao;


/**
 * @author ankituniyal
 *
 */
public class GallagherSalesAreaDaoImpl extends AbstractItemDao implements GallagherSalesAreaDao
{

	private static final String GET_ALL_B2BUNITS = "SELECT DISTINCT {PK} FROM {B2BUNIT} ";

	@Override
	public List<B2BUnitModel> getAllB2BUnits()
	{
		final SearchResult<B2BUnitModel> result = search(new FlexibleSearchQuery(GET_ALL_B2BUNITS));
		return result.getResult().isEmpty() ? null : result.getResult();
	}

}
