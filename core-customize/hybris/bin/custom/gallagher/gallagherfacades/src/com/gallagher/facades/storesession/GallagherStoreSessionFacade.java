package com.gallagher.facades.storesession;

import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;


/**
 * Store session facade implementation. The main purpose is to load currency and language data from existing services.
 *
 * @author Vikram Bishnoi
 */
public interface GallagherStoreSessionFacade extends StoreSessionFacade
{

	/**
	 * Resets the website's url encoding as website could be changed
	 */
	void resetEncodingAttributes();

}
