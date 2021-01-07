/**
 *
 */
package com.gallagher.core.services;

import java.util.Date;


/**
 * Service to read and update Regional Products and Variant Product on the basis of Msster Catalog.
 *
 * @author shishirkant
 *
 */
public interface GallagherProductProcessingService
{

	/**
	 * Create Base Product from the Product (Variant Product), pushed by C4C in Master Catalog.
	 *
	 * @param catalogId
	 * @param lastStartTime
	 */
	void createBaseProduct(final String catalogId, final Date lastStartTime);

	/**
	 * Create Regional Variant Product from the Product (Variant Product) available in Master Catalog, pushed by C4C and
	 * attach them with their Base Product. This cronjob will also take care of Approval Status of Regional Base Product
	 * and Variant Product
	 *
	 * @param catalogId
	 * @param lastStartTime
	 * @return process result
	 */
	boolean createAndProcessVariantProduct(final String catalogId, final Date lastStartTime);

	/**
	 * It will sync productReferences attribute of base product only.
	 *
	 * @param catalogId
	 * @param lastStartTime
	 * @return process result
	 */
	boolean syncProductReferenceForBaseProduct(final String catalogId, final Date lastStartTime);

	/**
	 * It will sync recommendedProducts and recommendedCategories attribute of base product only.
	 *
	 * @param catalogId
	 * @param lastStartTime
	 * @return process result
	 */
	boolean syncRecommendationsForBaseProduct(final String catalogId, final Date lastStartTime);
}
