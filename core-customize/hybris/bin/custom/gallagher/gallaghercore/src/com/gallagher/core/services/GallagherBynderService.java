/**
 *
 */
package com.gallagher.core.services;

import com.gallagher.core.cronjob.model.GallagherBynderSyncCronJobModel;
import com.gallagher.outboundservices.response.dto.GallagherBynderResponse;


/**
 *
 */
public interface GallagherBynderService
{

	public boolean updateMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse);

	public boolean deleteMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse);

	public boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel model,
			final GallagherBynderResponse gallagherBynderResponse);

	public boolean deleteDocumentMedia(GallagherBynderSyncCronJobModel cronModel, GallagherBynderResponse gallagherBynderResponse);

	public boolean updateVideoMedia(final GallagherBynderSyncCronJobModel model,
			final GallagherBynderResponse gallagherBynderResponse);

	public boolean deleteVideoMedia(final GallagherBynderSyncCronJobModel model,
			final GallagherBynderResponse gallagherBynderResponse);
}
