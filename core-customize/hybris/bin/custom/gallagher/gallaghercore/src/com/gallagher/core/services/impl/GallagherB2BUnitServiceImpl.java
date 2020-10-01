/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gallagher.core.services.GallagherB2BUnitService;

/**
 * @author ankituniyal
 *
 */
public class GallagherB2BUnitServiceImpl extends DefaultB2BUnitService implements GallagherB2BUnitService
{


	@Override
	public List<B2BUnitModel> getAllB2BUnits(final CustomerModel customer)
	{
		final Set<PrincipalGroupModel> customerGroups = customer.getGroups();
		final List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		final Set<B2BUnitModel> rootB2BUnits = new HashSet<>();
		for (final PrincipalGroupModel group : customerGroups)
		{
			if (group instanceof B2BUnitModel)
			{
				rootB2BUnits.add(getRootUnit((B2BUnitModel) group));
				b2bUnitList.add((B2BUnitModel) group);
			}
		}
		if (rootB2BUnits.size() > 1)
		{
			return b2bUnitList;
		}
		else
		{
			b2bUnitList.clear();
			return b2bUnitList;
		}
	}

}
