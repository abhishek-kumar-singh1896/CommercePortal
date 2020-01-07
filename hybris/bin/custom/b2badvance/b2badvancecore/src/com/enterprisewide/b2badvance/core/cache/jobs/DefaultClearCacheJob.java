/**
 *
 */
package com.enterprisewide.b2badvance.core.cache.jobs;

import com.enterprisewide.b2badvance.core.cache.B2badvanceManualCacheRegion;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;


/**
 * @author Enterprise Wide
 *
 */
public class DefaultClearCacheJob extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOG = Logger.getLogger(DefaultClearCacheJob.class);

	private B2badvanceManualCacheRegion cacheRegion;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		PerformResult result = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		try
		{
			getCacheRegion().clear();
		}
		catch (final Exception e)
		{
			LOG.error("CACHE CLEAR FAILED : ", e);
			result = new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
		return result;
	}

	/**
	 * @return the cacheRegion
	 */
	public B2badvanceManualCacheRegion getCacheRegion()
	{
		return cacheRegion;
	}

	/**
	 * @param cacheRegion
	 *           the cacheRegion to set
	 */
	public void setCacheRegion(final B2badvanceManualCacheRegion cacheRegion)
	{
		this.cacheRegion = cacheRegion;
	}
}
