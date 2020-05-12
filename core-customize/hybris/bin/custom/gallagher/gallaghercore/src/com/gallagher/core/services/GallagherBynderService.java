/**
 *
 */
package com.gallagher.core.services;

import java.util.List;

import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;


/**
 *
 */
public interface GallagherBynderService
{

	boolean updateMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus);

	boolean deleteMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse);

	boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus);

	boolean deleteDocumentMedia(GallagherBynderSyncCronJobModel cronModel, GallagherBynderResponse gallagherBynderResponse);

	boolean updateVideoMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus);

	boolean deleteVideoMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse,
			final List<String> skus);
}
