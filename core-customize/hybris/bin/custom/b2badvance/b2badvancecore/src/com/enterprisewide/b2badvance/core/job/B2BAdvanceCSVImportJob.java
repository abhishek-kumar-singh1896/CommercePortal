/**
 *
 */
package com.enterprisewide.b2badvance.core.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.store.BaseStoreModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.enterprisewide.b2badvance.core.constants.B2badvanceCoreConstants;
import com.enterprisewide.b2badvance.core.model.B2BAdvanceCSVImportCronJobModel;
import com.enterprisewide.b2badvance.core.product.impl.B2BAdvanceCSVProcessor;
import com.enterprisewide.b2badvance.core.sftp.B2BAdvanceSFTPFileHandler;


/**
 * Job to create impexes to import base and variant products.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceCSVImportJob extends AbstractJobPerformable<B2BAdvanceCSVImportCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceCSVImportJob.class);
	private B2BAdvanceSFTPFileHandler b2bAdvanceSFTPFileHandler;
	private B2BAdvanceCSVProcessor b2bAdvanceCSVProcessor;

	/**
	 * Job to import base and variant product data.
	 *
	 * @param productImportJob
	 */
	@Override
	public PerformResult perform(final B2BAdvanceCSVImportCronJobModel productImportJob)
	{
		PerformResult performResult = null;
		CronJobResult cronJobResult = CronJobResult.SUCCESS;
		try
		{
			Assert.notNull(productImportJob.getBaseStore(), "BaseStore cannot be null");
			final BaseStoreModel baseStoreModel = productImportJob.getBaseStore();

			Assert.notNull(baseStoreModel.getCatalogs().get(0).getId(), "Base id cannot be null");
			final String catalogId = baseStoreModel.getCatalogs().get(0).getId();

			final Date lastJobStartTime = getLastCronJobTime(productImportJob.getCronJobHistoryEntries());
			final String importType = productImportJob.getImportType();
			final Map<String, File> dirMap = new HashMap<>();
			final boolean createDirError = b2bAdvanceSFTPFileHandler.createDirectories(dirMap, importType);

			if (!createDirError)
			{
				final Map<String, List<File>> csvMap = new HashMap<>(2);
				final boolean csvDownloadHasError = b2bAdvanceSFTPFileHandler.populateCsvFiles(
						dirMap.get(B2badvanceCoreConstants.SFTP_FILES_LOCATION), importType, importType + "_", lastJobStartTime,
						csvMap);
				if (!csvMap.isEmpty())
				{
					final boolean processorHasError = b2bAdvanceCSVProcessor.processCSVFiles(csvMap.get(importType), dirMap, catalogId,
							importType);
					cronJobResult = getResultAfterProcess(csvDownloadHasError, processorHasError);
				}
			}
			else
			{
				LOG.error("Job can't be perform without directory creation");
				cronJobResult = CronJobResult.FAILURE;
			}
			b2bAdvanceSFTPFileHandler.cleanSFTPLocalDirectories(dirMap.get(B2badvanceCoreConstants.SFTP_FILES_LOCATION));
		}
		catch (final IllegalArgumentException exception)
		{
			LOG.error("Exception occured in job : ", exception);
			cronJobResult = CronJobResult.FAILURE;
		}
		finally
		{
			LOG.info("Import is completed with status : " + cronJobResult);
			performResult = new PerformResult(cronJobResult, CronJobStatus.FINISHED);
		}
		return performResult;
	}

	/**
	 * This method is used to decide the status of cron job on the basis of various steps of processes it has gone
	 * through.
	 *
	 * @param csvDownloadHasError
	 * @param invoiceProcessorHasError
	 * @return CronJobResult, which indicates final result of the job.
	 */
	private CronJobResult getResultAfterProcess(final boolean csvDownloadHasError, final boolean baseProductProcessorHasError)
	{
		CronJobResult cronJobResult = CronJobResult.SUCCESS;
		if (csvDownloadHasError)
		{
			cronJobResult = CronJobResult.FAILURE;
		}
		else if (baseProductProcessorHasError)
		{
			cronJobResult = CronJobResult.ERROR;
		}
		return cronJobResult;
	}

	/**
	 * This method is used to get time of last successful cron job.
	 *
	 * @param cronJobHistory
	 * @return Date of the last cronjob with either SUCCESS OR ERROR as cronJobResult
	 */
	private Date getLastCronJobTime(final List<CronJobHistoryModel> cronJobHistory)
	{
		Date lastJobStartTime = null;
		final List<CronJobHistoryModel> entries = new ArrayList<>(cronJobHistory);
		Collections.sort(entries, new BeanComparator(CronJobHistoryModel.STARTTIME, new ComparableComparator()).reversed());
		for (final CronJobHistoryModel cronJobHistoryModel : entries)
		{
			if (CronJobResult.SUCCESS.equals(cronJobHistoryModel.getResult())
					|| CronJobResult.ERROR.equals(cronJobHistoryModel.getResult()))
			{
				lastJobStartTime = cronJobHistoryModel.getStartTime();
				break;
			}
		}
		LOG.info("Last CronJob start time is :: " + lastJobStartTime);
		return lastJobStartTime;
	}

	public B2BAdvanceSFTPFileHandler getB2bAdvanceSFTPFileHandler()
	{
		return b2bAdvanceSFTPFileHandler;
	}

	public void setB2bAdvanceSFTPFileHandler(final B2BAdvanceSFTPFileHandler b2bAdvanceSFTPFileHandler)
	{
		this.b2bAdvanceSFTPFileHandler = b2bAdvanceSFTPFileHandler;
	}

	public B2BAdvanceCSVProcessor getB2bAdvanceCSVProcessor()
	{
		return b2bAdvanceCSVProcessor;
	}

	public void setB2bAdvanceCSVProcessor(final B2BAdvanceCSVProcessor b2bAdvanceCSVProcessor)
	{
		this.b2bAdvanceCSVProcessor = b2bAdvanceCSVProcessor;
	}



}
