/**
 *
 */
package com.enterprisewide.b2badvance.core.bulkorder.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 *
 */
public interface BulkOrderService
{
	ProductModel getProductByCode(final String productCode, final CatalogVersionModel catalogVersion)
			throws IllegalArgumentException;

	List<ProductModel> getProductsForQuery(final String productCode, final CatalogVersionModel catalogVersion)
			throws IllegalArgumentException;

	public List<ProductModel> getProductsForQuery(final String queryString, final int pageSize);

	String getStockForProduct(String productCode, CatalogVersionModel catalogVersion);

	public CatalogVersionModel getCatalogVersionForBaseSite();
}
