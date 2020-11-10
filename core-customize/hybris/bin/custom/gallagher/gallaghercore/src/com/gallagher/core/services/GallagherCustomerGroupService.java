/**
 *
 */
package com.gallagher.core.services;

import java.util.Set;


/**
 * @author ankituniyal
 *
 */
public interface GallagherCustomerGroupService
{
	/**
	 * Returns the current salesArea from session.
	 *
	 * @return the current salesArea
	 */
	String getCurrentCustomerGroup();

	/**
	 * Gets all the stores.
	 *
	 * @return a set of all the stores
	 */
	Set<String> getAllCustomerGroups();

	/**
	 * Sets the current sales area in session only and not in cart.
	 *
	 * @param salesArea
	 *           the new current store in session
	 */
	void setCurrentCustomerGroupInSession(String customerGroup);
}
