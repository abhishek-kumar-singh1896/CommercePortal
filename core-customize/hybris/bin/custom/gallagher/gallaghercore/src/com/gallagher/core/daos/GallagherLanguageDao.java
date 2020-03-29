/**
 *
 */
package com.gallagher.core.daos;

import de.hybris.platform.core.model.c2l.LanguageModel;


/**
 * DAO for methods related to LanguageModel
 *
 * @author gauravkamboj
 */
public interface GallagherLanguageDao
{
	/**
	 * get LanguageModel by ISO Code
	 *
	 */
	LanguageModel getLanguageByIsoCode(String isoCode);
}
