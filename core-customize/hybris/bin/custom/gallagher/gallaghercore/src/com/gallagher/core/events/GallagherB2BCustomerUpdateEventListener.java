/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerUpdateProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;




/**
 * @author ankituniyal
 *
 *         Customer Update event listener to trigger welcome mail
 */
public class GallagherB2BCustomerUpdateEventListener
		extends AbstractAcceleratorSiteEventListener<GallagherB2BCustomerUpdateEvent>
{

	private static final Logger LOGGER = Logger.getLogger(GallagherB2BCustomerUpdateEventListener.class);


	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener#getSiteChannelForEvent(de.hybris.
	 * platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected SiteChannel getSiteChannelForEvent(final GallagherB2BCustomerUpdateEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return site.getChannel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer.
	 * event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final GallagherB2BCustomerUpdateEvent updateEvent)
	{
		LOGGER.info("B2B Customer update logger start...");
		final StoreFrontCustomerUpdateProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerUpdateProcessModel) getBusinessProcessService()
				.createProcess(
						"customerUpdateEmailProcess-" + updateEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
						"customerUpdateEmailProcess");
		storeFrontCustomerProcessModel.setSite(updateEvent.getSite());
		storeFrontCustomerProcessModel.setCustomer(updateEvent.getCustomer());
		storeFrontCustomerProcessModel.setLanguage(updateEvent.getLanguage());
		storeFrontCustomerProcessModel.setCurrency(updateEvent.getCurrency());
		storeFrontCustomerProcessModel.setStore(updateEvent.getBaseStore());
		storeFrontCustomerProcessModel.setModifiedAttributesMap(updateEvent.getModifiedAttributesMap());
		getModelService().save(storeFrontCustomerProcessModel);
		getBusinessProcessService().startProcess(storeFrontCustomerProcessModel);
		LOGGER.info("B2B Customer update logger end...");

	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}



