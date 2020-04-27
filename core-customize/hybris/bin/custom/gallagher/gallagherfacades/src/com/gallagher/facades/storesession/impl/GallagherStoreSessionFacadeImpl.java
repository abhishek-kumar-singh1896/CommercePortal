package com.gallagher.facades.storesession.impl;

import de.hybris.platform.commercefacades.storesession.impl.DefaultStoreSessionFacade;

import org.apache.log4j.Logger;

import com.gallagher.facades.storesession.GallagherStoreSessionFacade;


/**
 * Store session facade implementation. The main purpose is to load currency and language data from existing services.
 *
 * @author Vikram Bishnoi
 */
public class GallagherStoreSessionFacadeImpl extends DefaultStoreSessionFacade implements GallagherStoreSessionFacade
{
	private static final Logger LOG = Logger.getLogger(GallagherStoreSessionFacadeImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetEncodingAttributes()
	{
		getSessionService().removeAttribute("encodingAttributes");

	}
}
