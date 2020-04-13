/**
 *
 */
package com.gallagher.core.daos;

import de.hybris.platform.core.model.c2l.CurrencyModel;


/**
 * DAO for methods related to CurrencyModel
 *
 * @author gauravkamboj
 */
public interface GallagherCurrencyDao
{

	/**
	 * get CurrencyModel by ISO Code
	 *
	 */
	CurrencyModel getCurrencyByCountryIsoCode(String isoCode);
}
