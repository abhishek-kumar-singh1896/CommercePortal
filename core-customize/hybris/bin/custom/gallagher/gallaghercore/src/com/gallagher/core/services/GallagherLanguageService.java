/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.core.model.c2l.LanguageModel;


/**
 * Service for methods related to LanguageModel
 *
 * @author gauravkamboj
 */
public interface GallagherLanguageService
{
	/**
	 * returns LanguageModel from ISO Code
	 *
	 */
	LanguageModel getLanguageByisoCode(String isoCode);
}
