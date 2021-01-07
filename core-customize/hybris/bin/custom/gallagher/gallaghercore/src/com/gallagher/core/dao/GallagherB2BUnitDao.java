/**
 *
 */
package com.gallagher.core.dao;

import de.hybris.platform.b2b.dao.B2BUnitDao;
import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.List;


/**
 * @author ankituniyal
 *
 */
public interface GallagherB2BUnitDao extends B2BUnitDao
{

	List<B2BUnitModel> getAllB2BUnits();
}
