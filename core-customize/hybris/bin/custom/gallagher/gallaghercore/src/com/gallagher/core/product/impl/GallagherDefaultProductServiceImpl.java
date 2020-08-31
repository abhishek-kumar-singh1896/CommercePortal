/**
 *
 */
package com.gallagher.core.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.gallagher.core.dao.GallagherProductProcessingDao;


/**
 * @author shilpiverma
 *
 */
public class GallagherDefaultProductServiceImpl implements GallagherProductService
{

	@Resource(name = "gallagherProductProcessingDao")
	public GallagherProductProcessingDao gallagherProductProcessingDao;

	/**
	 * Returns the Map of BaseStoreModel & CatalogVersionModel and will process the Variant Product for Approval Status
	 *
	 * @param variantProductCode
	 * @param baseProductCode
	 * @param baseStores
	 * @param catalogId
	 * @return storeCatalogMap
	 */

	@Override
	public Set<BaseStoreModel> getBaseStoresForVariant(final String variantProductCode)
	{
		final Set<BaseStoreModel> baseStores = new HashSet<>();
		final List<CatalogVersionModel> allAvailableCatalogVersionForCode = gallagherProductProcessingDao
				.getAvailableCatalogVersionForCode(variantProductCode);


		for (final CatalogVersionModel catalogVer : allAvailableCatalogVersionForCode)
		{
			if (catalogVer.getCatalog().getBaseStores().size() >= 1)
			{
				for (final BaseStoreModel store : catalogVer.getCatalog().getBaseStores())
				{
					baseStores.add(store);
				}
			}
			catalogVer.getCatalog().getBaseStores();
		}
		return baseStores;
	}

	@Override
	public String getFinalValueString(final StringBuffer requestURL, final String valueString)
	{
		final int index1 = requestURL.indexOf("/am/");
		final String sub1 = requestURL.substring(index1 + 4, requestURL.length());
		final int index2 = sub1.indexOf("/");
		final String sub2 = sub1.substring(index2 + 1, sub1.length());
		final int index3 = sub2.indexOf("/");
		final int initalIndex = index1;
		final int endIndex = index1 + index2 + index3 + 6;
		final String finalValue = requestURL.replace(initalIndex, endIndex, valueString).toString();
		return finalValue;
	}
}
