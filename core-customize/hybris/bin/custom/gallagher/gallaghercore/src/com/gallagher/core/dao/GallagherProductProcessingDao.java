/**
 *
 */
package com.gallagher.core.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.Date;
import java.util.List;


/**
 * {@link ProductModel CatalogVersionModel GenericVariantProductModel} dao
 *
 * @author shishirkant
 *
 */
public interface GallagherProductProcessingDao
{

	/**
	 * Returns for the list of Products for given catalogVersion, modified after the given date where baseProductCode is
	 * not null.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @return products
	 */
	List<ProductModel> getProductsForConversion(final CatalogVersionModel catalogVersion, final Date lastStartTime);

	/**
	 * Returns for the list of approved Products for given catalogVersion, modified after the given date where
	 * baseProductCode is not null.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @return products
	 */
	List<ProductModel> getVariantProductsForTransformation(final CatalogVersionModel catalogVersion, final Date lastStartTime);

	/**
	 * Returns for the list of available Catalog Versions for given code of Variant Product.
	 *
	 * @param variantProductCode
	 * @return availableCatalogVersions
	 */
	List<CatalogVersionModel> getAvailableCatalogVersionForCode(final String variantProductCode);

	/**
	 * Returns for the list of approved Variant Products for given Base Product and CatalogVersion.
	 *
	 * @param baseProduct
	 * @param unapprovedForCatalogVersion
	 * @return approvedVariantProducts
	 */
	List<GenericVariantProductModel> getApprovedVariantProductsForBaseProduct(final ProductModel baseProduct,
			final CatalogVersionModel unapprovedForCatalogVersion);

}
