/**
 *
 */
package com.gallagher.facades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
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

	@Override
	protected void populateUnit(final B2BCustomerModel customer, final CustomerData target)
	{
		final List<B2BUnitData> units = new ArrayList<>();
		// minimal properties are populated, as require by customer paginated page.
		final Set<PrincipalGroupModel> allGroups = customer.getAllGroups();
		for (final PrincipalGroupModel group : allGroups)
		{
			if (group instanceof B2BUnitModel)
			{

				final B2BUnitData b2BUnitData = new B2BUnitData();
				b2BUnitData.setUid(((B2BUnitModel) group).getUid());
				b2BUnitData.setName(((B2BUnitModel) group).getLocName());
				b2BUnitData.setActive(Boolean.TRUE.equals(((B2BUnitModel) group).getActive()));

				// unit's cost centers
				if (CollectionUtils.isNotEmpty(((B2BUnitModel) group).getCostCenters()))
				{
					final List<B2BCostCenterData> costCenterDataCollection = new ArrayList<>();
					for (final B2BCostCenterModel costCenterModel : ((B2BUnitModel) group).getCostCenters())
					{
						final B2BCostCenterData costCenterData = new B2BCostCenterData();
						costCenterData.setCode(costCenterModel.getCode());
						costCenterData.setName(costCenterModel.getName());
						costCenterDataCollection.add(costCenterData);
					}
					b2BUnitData.setCostCenters(costCenterDataCollection);
				}
				units.add(b2BUnitData);
			}
			target.setUnits(units);
		}
	}
}
