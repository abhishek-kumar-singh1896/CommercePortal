/**
 *
 */
package com.gallagher.core.services;

import java.util.Set;


/**
 * @author ankituniyal
 *
 */
public interface GallagherSalesAreaService
{
	/**
	 * Returns the current salesArea from session.
	 *
	 * @return the current salesArea
	 */
	String getCurrentSalesArea();

	/**
	 * Gets all the stores.
	 *
	 * @return a set of all the stores
	 */
	Set<String> getAllSalesAreas();

	/**
	 * Sets the current sales area in session only and not in cart.
	 *
	 * @param salesArea
	 *           the new current store in session
	 */
	void setCurrentSalesAreaInSession(String salesArea);
}
