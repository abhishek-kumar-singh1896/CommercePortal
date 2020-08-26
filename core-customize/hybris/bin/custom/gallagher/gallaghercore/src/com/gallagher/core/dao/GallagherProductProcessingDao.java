/**
 *
 */
package com.gallagher.core.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.Date;
import java.util.List;


/**
 * {@link ProductModel CatalogVersionModel GenericVariantProductModel MediaContainerModel} dao
 *
 * @author shishirkant
 *
 */
public interface GallagherProductProcessingDao
{

	/**
	 * Returns the list of Products for given catalogVersion, modified after the given date where baseProductCode is not
	 * null.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @return products
	 */
	List<ProductModel> getProductsForConversion(final CatalogVersionModel catalogVersion, final Date lastStartTime);

	/**
	 * Returns the list of approved Products for given catalogVersion, modified after the given date where
	 * baseProductCode is not null.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @return products
	 */
	List<ProductModel> getVariantProductsForTransformation(final CatalogVersionModel catalogVersion, final Date lastStartTime);

	/**
	 * Returns the list of available Catalog Versions for given code of Variant Product.
	 *
	 * @param variantProductCode
	 * @return availableCatalogVersions
	 */
	List<CatalogVersionModel> getAvailableCatalogVersionForCode(final String variantProductCode);

	/**
	 * Returns the list of approved Variant Products for given Base Product and CatalogVersion.
	 *
	 * @param baseProduct
	 * @param unapprovedForCatalogVersion
	 * @return approvedVariantProducts
	 */
	List<GenericVariantProductModel> getApprovedVariantProductsForBaseProduct(final ProductModel baseProduct,
			final CatalogVersionModel unapprovedForCatalogVersion);

	/**
	 * Returns MediaContainer for given Qualifier and CatalogVersion.
	 *
	 * @param qualifier
	 * @param catalogVersion
	 * @return mediaContainer
	 */
	MediaContainerModel getMediaContainerForQualifier(final String qualifier, final CatalogVersionModel catalogVersion);

	/**
	 * Returns the list of approved base Products for given catalogVersion, If there is any modification in base product
	 * only.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @return products
	 */
	List<ProductModel> getBaseProductsForSync(final CatalogVersionModel catalogVersion, final Date lastStartTime);

	/**
	 * Returns a base Product, if some modification is done in ProductReferences attribute of given product.
	 *
	 * @param catalogVersion
	 * @param lastStartTime
	 * @param product
	 * @return product
	 */
	ProductModel getBaseProductsForProductReferenceSync(final CatalogVersionModel catalogVersion, final Date lastStartTime,
			final ProductModel product);
}
