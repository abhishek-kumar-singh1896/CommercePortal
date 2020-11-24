/**
 *
 */
package com.gallagher.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.core.cronjob.model.GallagherProductProcessingCronJobModel;
import com.gallagher.core.services.GallagherProductProcessingService;


/**
 * Cronjob to create Regional Variant Product from the Product (Variant Product) available in Master Catalog, pushed by
 * C4C and attach them with their Base Product. This cronjob will also take care of Approval Status of Regional Base
 * Product and Variant Product
 *
 * @author shishirkant
 *
 */
public class GallagherProductTransformationCronjob extends AbstractJobPerformable<GallagherProductProcessingCronJobModel>
{
	@Resource(name = "gallagherProductProcessingService")
	protected GallagherProductProcessingService gallagherProductProcessingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherProductTransformationCronjob.class);

	@Override
	public PerformResult perform(final GallagherProductProcessingCronJobModel cronJob)
	{
		final Date lastStartTime = cronJob.getLastStartTime();
		final String catalogId = cronJob.getCatalogId();
		final PerformResult result;
		cronJob.setLastStartTime(new Date());
		final boolean success = gallagherProductProcessingService.createAndProcessVariantProduct(catalogId, lastStartTime);
		final boolean successSync = gallagherProductProcessingService.syncProductReferenceForBaseProduct(catalogId, lastStartTime);
		final boolean successSyncRec = gallagherProductProcessingService.syncRecommendationsForBaseProduct(catalogId, lastStartTime);
		if (success & successSync & successSyncRec)
		{
			modelService.save(cronJob);
			result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			LOGGER.info("Cronjob : GallagherProductTransformationCronjob is not finished successfully.");
		}
		else
		{
			result = new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			LOGGER.info("Cronjob : GallagherProductTransformationCronjob is finished successfully.");

		}
		return result;
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}
}
