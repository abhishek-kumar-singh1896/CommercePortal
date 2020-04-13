/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;

import javax.annotation.Resource;

import com.gallagher.core.daos.GallagherCurrencyDao;
import com.gallagher.core.services.GallagherCurrencyService;


/**
 * {@inheritDoc}
 */
public class GallagherCurrencyServiceImpl implements GallagherCurrencyService
{

	@Resource(name = "gallagherCurrencyDao")
	GallagherCurrencyDao gallagherCurrencyDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CurrencyModel getCurrencyByCountryIsoCode(final String isoCode)
	{
		return gallagherCurrencyDao.getCurrencyByCountryIsoCode(isoCode);
	}

}
