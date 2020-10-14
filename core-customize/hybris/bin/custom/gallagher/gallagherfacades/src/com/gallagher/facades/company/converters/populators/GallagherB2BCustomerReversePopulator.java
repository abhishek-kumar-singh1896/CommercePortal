/**
 *
 */
package com.gallagher.facades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerReversePopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gallagher.core.enums.BU;
import com.gallagher.core.usergroups.strategy.GallagherB2BUserGroupsStrategy;


/**
 * Gallagher Populator to populate B2BCustomerModel with values from CustomerData
 *
 * @author shishirkant
 */
public class GallagherB2BCustomerReversePopulator extends B2BCustomerReversePopulator
{
	@Resource(name = "gallagherB2BUserGroupsStrategy")
	private GallagherB2BUserGroupsStrategy gallagherB2BUserGroupsStrategy;

	@Override
	public void populate(final CustomerData source, final B2BCustomerModel target) throws ConversionException
	{
		super.populate(source, target);

		target.setSapContactID(source.getCustomerId());
		target.setDuplicate(source.getDuplicate());
		target.setKeycloakGUID(source.getKeycloakGUID());
		target.setObjectID(source.getObjectID());
		target.setIsUserExist(source.isIsUserExist());
		target.setBusinessUnit(BU.SEC);
		target.setEmailID(source.getEmail());

		getB2BCommerceB2BUserGroupService().updateUserGroups(gallagherB2BUserGroupsStrategy.getUserGroups(), source.getRoles(),
				target);
	}

	@Override
	protected void populateUid(final CustomerData source, final B2BCustomerModel target)
	{
		String updateUid = null;
		if (StringUtils.isNotBlank(source.getDisplayUid()))
		{
			updateUid = source.getDisplayUid();
		}
		else if (source.getEmail() != null)
		{
			updateUid = source.getEmail();
		}

		if (updateUid == null)
		{
			return;
		}

		if (StringUtils.isBlank(target.getUid()) || !updateUid.equalsIgnoreCase(target.getUid()))
		{
			if (null != target.getUid())
			{
				target.setOriginalUid(target.getUid() + updateUid);
				target.setUid(target.getUid() + updateUid.toLowerCase());
			}
			else
			{
				target.setOriginalUid(updateUid);
				target.setUid(updateUid.toLowerCase());
			}
		}
	}

	@Override
	protected void populateDefaultUnit(final CustomerData source, final B2BCustomerModel target)
	{
		final B2BUnitModel oldDefaultUnit = getB2BUnitService().getParent(target);

		final B2BUnitModel defaultUnit = getB2BUnitService().getUnitForUid(source.getUnits().get(0).getUid());
		target.setDefaultB2BUnit(defaultUnit);

		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(target.getGroups());
		if (oldDefaultUnit != null && groups.contains(oldDefaultUnit))
		{
			groups.remove(oldDefaultUnit);
		}
		groups.add(defaultUnit);

		final List<B2BUnitData> units = source.getUnits().subList(1, source.getUnits().size());
		for (final B2BUnitData unit : units)
		{
			groups.add(getB2BUnitService().getUnitForUid(unit.getUid()));
		}
		target.setGroups(groups);
	}
}
