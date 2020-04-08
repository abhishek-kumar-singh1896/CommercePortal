/**
 *
 */
package com.gallagher.facades.usergroups;

import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUserGroupFacade;

import java.util.List;

import javax.annotation.Resource;

import com.gallagher.core.usergroups.strategy.GallagherB2BUserGroupsStrategy;


/**
 * A facade for handling user groups within b2b Commerce.
 *
 * @author abhishekkumar05
 */
public class GallagherB2BUserGroupFacade extends DefaultB2BUserGroupFacade
{
	@Resource(name = "gallagherB2BUserGroupsStrategy")
	private GallagherB2BUserGroupsStrategy gallagherB2BUserGroupsStrategy;

	@Override
	public List<String> getUserGroups()
	{
		return gallagherB2BUserGroupsStrategy.getUserGroups();

	}
}
