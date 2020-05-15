/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;


/**
 *
 */
public interface GallagherBynderService
{

	boolean updateMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus, final CatalogVersionModel catalog);

	boolean deleteMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final CatalogVersionModel catalog);

	boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus, final CatalogVersionModel catalog);

	boolean deleteDocumentMedia(GallagherBynderSyncCronJobModel cronModel, GallagherBynderResponse gallagherBynderResponse,
			final CatalogVersionModel catalog);

	boolean updateVideoMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus, final CatalogVersionModel catalog);

	boolean deleteVideoMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus, final CatalogVersionModel catalog);
}
