/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;

import javax.annotation.Resource;

import com.gallagher.core.daos.GallagherLanguageDao;
import com.gallagher.core.services.GallagherLanguageService;


/**
 * {@inheritDoc}
 */
public class GallagherLanguageServiceImpl implements GallagherLanguageService
{

	@Resource(name = "gallagherLanguageDao")
	GallagherLanguageDao gallagherLanguageDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LanguageModel getLanguageByisoCode(final String isoCode)
	{
		return gallagherLanguageDao.getLanguageByIsoCode(isoCode);
	}

}
