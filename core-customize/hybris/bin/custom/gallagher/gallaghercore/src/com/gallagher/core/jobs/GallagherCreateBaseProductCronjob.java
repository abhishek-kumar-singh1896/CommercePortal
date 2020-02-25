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
 * Cronjob to create Base Product from the Product (Variant Product), pushed by C4C in Master Catalog.
 *
 * @author shishirkant
 *
 */
public class GallagherCreateBaseProductCronjob extends AbstractJobPerformable<GallagherProductProcessingCronJobModel>
{

	@Resource(name = "gallagherProductProcessingService")
	protected GallagherProductProcessingService gallagherProductProcessingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherCreateBaseProductCronjob.class);

	@Override
	public PerformResult perform(final GallagherProductProcessingCronJobModel cronJob)
	{
		final Date lastStartTime = cronJob.getLastStartTime();
		final String catalogId = cronJob.getCatalogId();

		cronJob.setLastStartTime(new Date());

		gallagherProductProcessingService.createBaseProduct(catalogId, lastStartTime);
		modelService.save(cronJob);

		LOGGER.info("Cronjob : GallagherCreateBaseProductCronjob is finished successfully.");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

}
