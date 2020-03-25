/**
 *
 */
package com.gallagher.core.usergroups.strategy.impl;

import java.util.List;

import com.gallagher.core.usergroups.strategy.GallagherB2BUserGroupsStrategy;


/**
 * Implementation strategy for getting available user group codes that a B2BCustomer
 *
 * can be assigned to
 *
 */
public class GallagherB2BUserGroupsStrategyImpl implements GallagherB2BUserGroupsStrategy
{

	private List<String> groups;

	@Override
	public List<String> getUserGroups()
	{
		return getGroups();
	}

	public List<String> getGroups()
	{
		return groups;
	}

	public void setGroups(final List<String> groups)
	{
		this.groups = groups;
	}

}
