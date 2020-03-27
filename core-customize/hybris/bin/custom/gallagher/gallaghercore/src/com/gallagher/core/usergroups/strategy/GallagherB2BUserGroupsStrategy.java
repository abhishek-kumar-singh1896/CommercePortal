/**
 *
 */
package com.gallagher.core.usergroups.strategy;

import java.util.List;


/**
 * A strategy for getting available user group codes that a B2BCustomer
 *
 * can be assigned to.
 *
 * @author abhishekkumar05
 */
public interface GallagherB2BUserGroupsStrategy
{
	List<String> getUserGroups();
}
