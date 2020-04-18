/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.core.jobs.maintenance.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.processengine.enums.BooleanOperator;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;


/**
 * Copy of the OOTB implementation with additional logging and null checks.
 *
 * @author Vikram Bishnoi
 */
public class CleanUpLogsStrategy implements MaintenanceCleanupStrategy<CronJobModel, CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(CleanUpLogsStrategy.class.getName());

	public static final String JOB_LOGS_QUERY = "SELECT " + JobLogModel.PK + ",{" + JobLogModel.CREATIONTIME + "} FROM {"
			+ JobLogModel._TYPECODE + "} WHERE {" + JobLogModel.CRONJOB + "} in (?cronjob) ORDER BY {" + JobLogModel.CREATIONTIME
			+ "} ASC";
	public static final String LOG_FILES_QUERY = "SELECT " + LogFileModel.PK + " FROM {" + LogFileModel._TYPECODE + "} WHERE {"
			+ LogFileModel.OWNER + "} IN (?cronjob) ORDER BY {" + LogFileModel.CREATIONTIME + "} ASC";

	public static final String CRON_JOBS_QUERY = new StringBuilder().append("SELECT {cj.pk} from {").append(LogFileModel._TYPECODE)
			.append(" AS lf JOIN ").append(CronJobModel._TYPECODE).append(" AS cj ON {cj.pk} = {lf.owner}}")
			.append(" group by {cj.pk} having COUNT({lf.owner}) > ?logCount").toString();

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;
	private int logCount = 5;

	@Override
	public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
	{
		checkJobParameters(cjm);
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("logCount", this.logCount);
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(CRON_JOBS_QUERY, params);

		final int rowCount = Config.getInt("cleanup.old.logs.strategy.query.result.count", 10);
		fsq.setCount(rowCount);
		LOG.info("Result count of old cronjob logs cleanup fetch query is set to: " + rowCount);

		fsq.setResultClassList(Arrays.asList(CronJobModel.class));
		return fsq;
	}

	@Override
	public void process(final List<CronJobModel> elements)
	{
		LOG.info("Searching " + elements.size() + " cron jobs for log entries eligible for deletion.");
		final ArrayList<ItemModel> toBeRemoved = new ArrayList<>();
		for (final CronJobModel cronJobModel : elements)
		{
			LOG.info(String.format("Checking cron job %s:%s for logs elible for removal", cronJobModel.getJob().getCode(),
					cronJobModel.getCode()));
			toBeRemoved.clear();
			final List<JobLogModel> jobLogModels = this.<JobLogModel> getLogModels(cronJobModel, JOB_LOGS_QUERY);
			toBeRemoved.addAll(findLogModels(jobLogModels, cronJobModel.getLogsOperator(), cronJobModel.getLogsDaysOld(),
					cronJobModel.getLogsCount(), cronJobModel.getCode()));

			final List<LogFileModel> logFileModels = this.<LogFileModel> getLogModels(cronJobModel, LOG_FILES_QUERY);
			toBeRemoved.addAll(findLogModels(logFileModels, cronJobModel.getFilesOperator(), cronJobModel.getFilesDaysOld(),
					cronJobModel.getFilesCount(), cronJobModel.getCode()));

			if (!toBeRemoved.isEmpty())
			{
				if (toBeRemoved.size() > 100)
				{
					deleteInBatches(toBeRemoved, cronJobModel);
				}
				else
				{
					LOG.info(String.format("Removing %d log entries for cronjob %s:%s", toBeRemoved.size(),
							cronJobModel.getJob().getCode(), cronJobModel.getCode()));
					modelService.removeAll(toBeRemoved);
				}
			}
		}
	}

	private void deleteInBatches(final List<ItemModel> toBeRemoved, final CronJobModel cronJobModel)
	{
		final int batchSize = Config.getInt("cleanup.old.logs.strategy.delete.batch.size", 100);
		final int runTimeInSeconds = Config.getInt("cleanup.old.logs.strategy.max.runtime.seconds", 5 * 60);//default 5 minutes
		final long start = new Date().getTime();
		LOG.info(String.format("Removing %d log entries for cronjob %s:%s in batches of %d", toBeRemoved.size(),
				cronJobModel.getJob().getCode(), cronJobModel.getCode(), batchSize));
		for (final List<ItemModel> subList : Lists.partition(toBeRemoved, batchSize))
		{
			final long now = new Date().getTime();
			if (((now - start) / 1000) > runTimeInSeconds)
			{
				LOG.info(String.format("Batch delete max run time %d (seconds) has been exceeded, exiting batch delete for %s",
						runTimeInSeconds, cronJobModel.getCode()));
				return;
			}
			LOG.info(String.format("Removing %d log entries for cronjob %s:%s", subList.size(), cronJobModel.getJob().getCode(),
					cronJobModel.getCode()));
			modelService.removeAll(subList);
		}
	}

	private void checkJobParameters(final CronJobModel cjm)
	{
		if (cjm.getJob() instanceof MaintenanceCleanupJobModel)
		{
			final MaintenanceCleanupJobModel job = (MaintenanceCleanupJobModel) cjm.getJob();
			if (job.getThreshold() != null)
			{
				LOG.info(String.format("MaintenanceCleanupJobModel contains a threeshold value, using %d instead of %d",
						job.getThreshold(), logCount));
				setLogCount(job.getThreshold());
			}
		}
	}

	private void setLogCount(final Integer logCount)
	{
		if (logCount.intValue() < 0)
		{
			throw new IllegalArgumentException("Cannot set negative value for logCount");
		}
		this.logCount = logCount.intValue();
	}

	private <T> List<T> getLogModels(final CronJobModel cronJobModel, final String queryString)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(queryString);
		fsq.addQueryParameter("cronjob", cronJobModel.getPk().getLong());

		final int rowCount = Config.getInt("cleanup.old.logs.strategy.log.model.count", 100);
		fsq.setCount(rowCount);
		LOG.info("Result count of old log models fetch query is set to: " + rowCount);

		return flexibleSearchService.<T> search(fsq).getResult();
	}

	private List<ItemModel> findLogModels(final List<? extends ItemModel> logModels, final BooleanOperator operator,
			final Integer daysOld, final Integer maxCount, final String cronjobCode)
	{
		final List<ItemModel> toBeRemoved = new ArrayList<>();
		if (operator == null)
		{
			LOG.warn(String.format("Cannot delete logs for cronjob %s due to operator=null", cronjobCode));
			return toBeRemoved;
		}
		if (daysOld == null)
		{
			LOG.warn(String.format("Cannot delete logs for cronjob %s due to daysOld=null", cronjobCode));
			return toBeRemoved;
		}
		if (maxCount == null)
		{
			LOG.warn(String.format("Cannot delete logs for cronjob %s due to maxCount=null", cronjobCode));
			return toBeRemoved;
		}
		int logModelsCount = logModels.size();

		for (final ItemModel logModel : logModels)
		{
			switch (operator)
			{
				case AND:
					if (isOlderThan(logModel.getCreationtime(), daysOld) && logModelsCount > maxCount)
					{
						logModelsCount--;
						toBeRemoved.add(logModel);
					}
					break;
				case OR:
					if (isOlderThan(logModel.getCreationtime(), daysOld) || logModelsCount > maxCount)
					{
						logModelsCount--;
						toBeRemoved.add(logModel);
					}
					break;
				default:
					throw new IllegalStateException("Unsupported operator: " + operator.getCode() + ".");
			}
		}
		return toBeRemoved;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	private boolean isOlderThan(final Date date, final int daysOld)
	{
		final Date before = DateTime.now().minusDays(daysOld).toDate();
		return date.before(before);
	}

}
