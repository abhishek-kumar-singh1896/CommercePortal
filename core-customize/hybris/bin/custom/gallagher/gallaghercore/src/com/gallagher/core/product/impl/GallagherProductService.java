/**
 *
 */
package com.gallagher.core.product.impl;

import de.hybris.platform.store.BaseStoreModel;

import java.util.Set;


/**
 * @author shilpiverma
 *
 */
public interface GallagherProductService
{
	public Set<BaseStoreModel> getBaseStoresForVariant(final String variantProductCode);

	public String getFinalValueString(final StringBuffer requestURL, final String valueString);

}
