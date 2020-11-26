package com.gallagher.facades.company.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.gallagher.core.enums.BU;
import com.gallagher.core.events.GallagherB2BRegistrationEvent;
import com.gallagher.core.services.GallagherMindTouchService;


/**
 * Custom Facade implementation to handle business cases specific to Gallagher
 *
 * @author Vikram Bishnoi
 */
public class GallagherB2BUserFacadeImpl extends DefaultB2BUserFacade
{
	private static final Logger LOG = Logger.getLogger(GallagherB2BUserFacadeImpl.class);

	private EventService eventService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CommonI18NService commonI18NService;
	private GallagherMindTouchService gallagherMindTouchService;

	/**
	 * {@inheritDoc} Adds the registration listener to send emails
	 */
	@Override
	public void updateCustomer(final CustomerData customerData)
	{
		updateCustomerData(customerData);
		publishCustomerRegistrationEvent(customerData);
	}

	/**
	 * @param customerData
	 */
	private void updateCustomerData(final CustomerData customerData)
	{
		validateParameterNotNullStandardMessage("customerData", customerData);
		Assert.hasText(customerData.getFirstName(), "The field [FirstName] cannot be empty");
		Assert.hasText(customerData.getLastName(), "The field [LastName] cannot be empty");
		B2BCustomerModel customerModel;
		if (StringUtils.isEmpty(customerData.getUid()))
		{
			customerModel = this.getModelService().create(B2BCustomerModel.class);
		}
		else
		{
			customerModel = getUserService().getUserForUID(customerData.getUid(), B2BCustomerModel.class);
		}
		
		if (customerModel != null)
		{
			pushToMindTouch(customerModel);
		}
		getModelService().save(getB2BCustomerReverseConverter().convert(customerData, customerModel));
		
	}

	/**
	 * This method calls the mindtouch service to push cusstomer to mindtouch
	 *
	 * @param savedCustomer
	 */
	private void pushToMindTouch(final B2BCustomerModel savedCustomer)
	{
		try
		{
			getGallagherMindTouchService().pushCustomerToMindTouch(savedCustomer);
		}
		catch (final IOException e)
		{
			LOG.error("IOException while creating pushing user in mindTouch :: " + e.getMessage());
		}
		catch (final DocumentException e)
		{
			LOG.error("Document Exception while creating pushing user in mindTouch :: " + e.getMessage());
		}
	}

	private void publishCustomerRegistrationEvent(final CustomerData customerData)
	{
		if (StringUtils.isEmpty(customerData.getUid()))
		{
			String updateUid = null;
			if (StringUtils.isNotBlank(customerData.getDisplayUid()))
			{
				updateUid = customerData.getDisplayUid();
			}
			else if (customerData.getEmail() != null)
			{
				updateUid = customerData.getEmail();
			}
			final B2BCustomerModel customerModel = getUserService().getUserForUID(BU.SEC.getCode().toLowerCase() + "|" + updateUid,
					B2BCustomerModel.class);
			eventService.publishEvent(initializeEvent(new GallagherB2BRegistrationEvent(), customerModel));
		}
	}



	/**
	 * initializes an {@link GallagherB2BRegistrationEvent}
	 *
	 * @param event
	 * @param customerModel
	 * @return the event
	 */
	protected GallagherB2BRegistrationEvent initializeEvent(final GallagherB2BRegistrationEvent event,
			final B2BCustomerModel b2bCustomerModel)
	{
		event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		event.setSite(getBaseSiteService().getCurrentBaseSite());
		event.setCustomer(b2bCustomerModel);
		event.setLanguage(getCommonI18NService().getCurrentLanguage());
		event.setCurrency(getCommonI18NService().getCurrentCurrency());
		return event;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the gallagherMindTouchService
	 */
	public GallagherMindTouchService getGallagherMindTouchService()
	{
		return gallagherMindTouchService;
	}

	/**
	 * @param gallagherMindTouchService
	 *           the gallagherMindTouchService to set
	 */
	public void setGallagherMindTouchService(final GallagherMindTouchService gallagherMindTouchService)
	{
		this.gallagherMindTouchService = gallagherMindTouchService;
	}

}
