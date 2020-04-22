/**
 *
 */
package com.gallagher.converters.populators.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUnitPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.converters.Converters;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.gallagher.converters.populators.GallagherB2BUnitPopulator;


/**
 * @author anujmanocha
 *
 */
public class GallagherB2BUnitPopulatorImpl extends B2BUnitPopulator implements GallagherB2BUnitPopulator
{

	@Override
	public void populateTechnicians(final B2BUnitModel source, final B2BUnitData target)
	{
		final Collection<B2BCustomerModel> managers = getB2BUnitService().getUsersOfUserGroup(source,
				B2BConstants.B2BTECHNICIANGROUP, false);
		if (CollectionUtils.isNotEmpty(managers))
		{
			target.setTechnicians(Converters.convertAll(managers, getB2BCustomerConverter()));
		}
	}

	@Override
	public void populate(final B2BUnitModel source, final B2BUnitData target)
	{
		validateParameterNotNull(source, "Parameter source cannot be null.");
		validateParameterNotNull(target, "Parameter target cannot be null.");

		populateUnit(source, target);
		populateUnitRelations(source, target);
		populateParentUnit(source, target);
		populateChildUnits(source, target);
	}

	@Override
	protected void populateUnitRelations(final B2BUnitModel source, final B2BUnitData target)
	{
		populateBudgets(source, target);
		populateCostCenters(source, target);
		populateAddresses(source, target);
		populateCustomers(source, target);
		populateManagers(source, target);
		populateAdministrators(source, target);
		populateAccountManagers(source, target);
		populateTechnicians(source, target);
	}

}
