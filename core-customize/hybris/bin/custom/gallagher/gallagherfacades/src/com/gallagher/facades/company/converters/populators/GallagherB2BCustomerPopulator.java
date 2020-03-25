/**
 *
 */
package com.gallagher.facades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

import com.gallagher.core.usergroups.strategy.GallagherB2BUserGroupsStrategy;


/**
 * Gallagher Populator to populate CustomerData with values from B2BCustomerModel
 *
 * @author abhishekkumar05
 */
public class GallagherB2BCustomerPopulator extends B2BCustomerPopulator
{
	@Resource(name = "gallagherB2BUserGroupsStrategy")
	private GallagherB2BUserGroupsStrategy gallagherB2BUserGroupsStrategy;

	@Override
	public void populateRoles(final B2BCustomerModel source, final CustomerData target)
	{
		//super.populateRoles(source, target);

		final List<String> roles = new ArrayList<String>();
		final Set<PrincipalGroupModel> roleModels = new HashSet<>(source.getGroups());
		CollectionUtils.filter(roleModels, PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUnitModel.class)));
		CollectionUtils.filter(roleModels,
				PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUserGroupModel.class)));
		for (final PrincipalGroupModel role : roleModels)
		{
			// only display allowed usergroups
			if (gallagherB2BUserGroupsStrategy.getUserGroups().contains(role.getUid()))
			{
				roles.add(role.getUid());
			}
		}
		target.setRoles(roles);
	}
}
