/**
 *
 */
package com.gallagher.converters.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.converters.Populator;


/**
 * @author anujmanocha
 *
 */
public interface GallagherB2BUnitPopulator extends Populator<B2BUnitModel, B2BUnitData>
{
	void populateTechnicians(final B2BUnitModel source, final B2BUnitData target);

}
