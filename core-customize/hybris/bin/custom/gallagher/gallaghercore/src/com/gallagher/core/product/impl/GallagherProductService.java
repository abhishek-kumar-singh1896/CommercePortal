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

}
