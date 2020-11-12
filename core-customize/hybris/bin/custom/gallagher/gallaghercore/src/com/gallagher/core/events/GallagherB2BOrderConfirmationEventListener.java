/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class GallagherB2BOrderConfirmationEventListener
		extends AbstractAcceleratorSiteEventListener<GallagherB2BOrderConfirmationEvent>

{

	private static final Logger LOGGER = Logger.getLogger(GallagherB2BOrderConfirmationEvent.class);


	private ModelService modelService;
	private BusinessProcessService businessProcessService;



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



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener#getSiteChannelForEvent(de.hybris.
	 * platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected SiteChannel getSiteChannelForEvent(final GallagherB2BOrderConfirmationEvent event)
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
	protected void onSiteEvent(final GallagherB2BOrderConfirmationEvent event)
	{
		final OrderModel orderModel = event.getOrderEntryModel();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"orderConfirmationEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"orderConfirmationEmailProcess");
		orderProcessModel.setOrder(orderModel);
		orderProcessModel.setOrderEntry(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

	}


}



