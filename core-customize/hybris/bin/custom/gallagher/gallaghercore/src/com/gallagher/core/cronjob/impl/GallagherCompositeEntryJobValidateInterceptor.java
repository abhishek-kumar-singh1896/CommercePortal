/**
 *
 */
package com.gallagher.core.cronjob.impl;

import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.TriggerableJob;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;


/**
 * @author shishirkant
 */
public class GallagherCompositeEntryJobValidateInterceptor implements ValidateInterceptor<CompositeEntryModel>
{
	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public void onValidate(final CompositeEntryModel model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model.getTriggerableJob() != null)
		{
			final Job job = modelService.getSource(model.getTriggerableJob());
			if ((!(job instanceof TriggerableJob)) && (!(model.getTriggerableJob() instanceof ServicelayerJobModel)))
			{
				throw new InterceptorException(
						"Assigned Job either does not implements de.hybris.platform.cronjob.jalo.TriggerableJob or is not an instance of "
								+ ServicelayerJobModel.class.getName() + "!");
			}
		}
	}
}
