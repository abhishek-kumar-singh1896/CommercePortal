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

	public Boolean updateMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse);

	public Boolean deleteMedia(final GallagherBynderSyncCronJobModel model, final GallagherBynderResponse gallagherBynderResponse);

	public Boolean updateDocumentMedia(final GallagherBynderSyncCronJobModel model,
			final GallagherBynderResponse gallagherBynderResponse);

	public Boolean updateInsertIconMedia(final GallagherBynderSyncCronJobModel cronModel,
			final GallagherBynderResponse gallagherBynderResponse);

	Boolean deleteDocumentMedia(GallagherBynderSyncCronJobModel cronModel, GallagherBynderResponse gallagherBynderResponse);
}
