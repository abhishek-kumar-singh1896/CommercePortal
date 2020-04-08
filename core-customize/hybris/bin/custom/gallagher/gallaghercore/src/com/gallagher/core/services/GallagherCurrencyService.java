/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.core.model.c2l.CurrencyModel;


/**
 * Service for methods related to CurrencyModel
 *
 * @author gauravkamboj
 */
public interface GallagherCurrencyService
{
	/**
	 * returns CurrencyModel from ISO Code
	 */
	CurrencyModel getCurrencyByCountryIsoCode(String isoCode);
}
