/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.core.daos.GallagherCustomerDao;
import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.services.GallagherCustomerService;
import com.gallagher.keycloak.outboundservices.service.GallagherKeycloakService;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;


/**
 *
 * Implementation of {@link GallagherCustomerService}
 *
 * @author Abhishek
 */
public class GallagherCustomerServiceImpl implements GallagherCustomerService
{
	private static final Logger LOGGER = Logger.getLogger(GallagherCustomerServiceImpl.class);
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "gallagherCustomerDao")
	private GallagherCustomerDao gallagherCustomerDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "gallagherC4COutboundServiceFacade")
	private GallagherC4COutboundServiceFacade gallagherC4COutboundServiceFacade;

	@Resource(name = "customerAccountService")
	private CustomerAccountService customerAccountService;

	@Resource(name = "customerNameStrategy")
	private CustomerNameStrategy customerNameStrategy;

	@Resource(name = "gallagherKeycloakService")
	private GallagherKeycloakService keycloakService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateCommerceCustomer(final GallagherAccessToken token, final SiteChannel channel)
	{
		boolean success = false;

		final List<CustomerModel> retrieveUserBySubjectId = gallagherCustomerDao.retrieveUserByKeycloakGUID(token.getSubjectId());

		if (CollectionUtils.isEmpty(retrieveUserBySubjectId))
		{
			if (SiteChannel.B2C.equals(channel))
			{
				try
				{
					success = createCommerceCustomer(token);
				}
				catch (final DuplicateUidException dupUidEx)
				{
					LOGGER.error("Exception while creating new customer", dupUidEx);
				}
			}
		}
		else
		{

			final CustomerModel retrieveUser = retrieveUserBySubjectId.get(0);

			if (SiteChannel.B2B.equals(channel))
			{
				updateCustomerFromC4C(retrieveUser);
			}
			else
			{
				/* Update name and email only if updated to avoid sending data to SCPI */
				if (!retrieveUser.getName().equals(token.getName()) || !retrieveUser.getUid().equals(token.getEmail()))
				{
					retrieveUser.setName(token.getName());
					retrieveUser.setUid(token.getEmail());
				}
				retrieveUser.setIsUserExist(true);
				modelService.save(retrieveUser);
			}
			success = true;
		}

		return success;
	}

	private boolean createCommerceCustomer(final GallagherAccessToken token) throws DuplicateUidException
	{

		final CustomerModel newCustomer = modelService.create(CustomerModel.class);

		newCustomer.setName(token.getName());
		newCustomer.setUid(token.getEmail());
		newCustomer.setKeycloakGUID(token.getSubjectId());
		newCustomer.setIsUserExist(false);

		//check if customer exist in the C4C
		final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
				.getCustomerInfoFromC4C(token.getEmail(), token.getSubjectId());


		//if customer exist and count > 1 then
		//set commerceCustomer flag duplicate = true
		//else if customer count = 1 then update C4C
		if (CollectionUtils.isNotEmpty(existingCustomers))
		{
			newCustomer.setSapContactID(existingCustomers.get(0).getContactID());
			newCustomer.setObjectID(existingCustomers.get(0).getObjectID());
			if (existingCustomers.size() > 1)
			{
				newCustomer.setDuplicate(true);
			}
			newCustomer.setSapIsReplicated(true);
		}

		//create customer in commerce and update C4C
		try
		{
			customerAccountService.register(newCustomer, null);
		}
		catch (final DuplicateUidException dupUidEx)
		{
			LOGGER.error("Exception while creating new customer", dupUidEx);
			throw new DuplicateUidException(dupUidEx.getMessage(), dupUidEx);

		}

		return true;
	}

	private boolean updateCustomerFromC4C(final CustomerModel customer)
	{

		//check if customer exist in the C4C
		final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
				.getCustomerInfoFromC4C(customer.getUid(), customer.getKeycloakGUID());

		if (CollectionUtils.isNotEmpty(existingCustomers))
		{
			final GallagherInboundCustomerEntry c4cCustomer = existingCustomers.get(0);
			final String customerName = customerNameStrategy.getName(c4cCustomer.getFirstName(), c4cCustomer.getLastName());
			/* Update name and email only if updated to avoid sending data to SCPI */
			boolean updated = false;
			if (customer.getName() == null || !customer.getName().equals(customerName))
			{
				customer.setName(customerName);
				updated = true;
			}
			if (customer.getUid() == null || !customer.getUid().equals(c4cCustomer.getEmail()))
			{
				customer.setUid(c4cCustomer.getEmail());
				updated = true;
			}
			if (updated)
			{
				modelService.save(customer);
				final CustomerData customerData = new CustomerData();
				customerData.setUid(customer.getUid());
				customerData.setFirstName(c4cCustomer.getFirstName());
				customerData.setLastName(c4cCustomer.getLastName());
				keycloakService.updateKeyCloakUserProfile(customerData);
			}
		}


		return true;
	}

	public void changeUid(final String newUid) throws DuplicateUidException
	{
		Assert.hasText(newUid, "The field [newEmail] cannot be empty");
		//Assert.hasText(currentPassword, "The field [currentPassword] cannot be empty");
		final String newUidLower = newUid.toLowerCase();
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		final ChangeUIDEvent event = new ChangeUIDEvent(currentUser.getOriginalUid(), newUid);
		currentUser.setOriginalUid(newUid);

		// check uniqueness only if the uids are case insensitive different
		if (!currentUser.getUid().equalsIgnoreCase(newUid))
		{
			checkUidUniqueness(newUidLower);
			currentUser.setUid(newUidLower);
		}
		//adjustPassword(currentUser, newUidLower, currentPassword);
		modelService.save(currentUser);
		getEventService().publishEvent(initializeEvent(event, currentUser));
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected void checkUidUniqueness(final String newUid) throws DuplicateUidException
	{
		// Check if the newUid is available
		try
		{
			if (getUserService().getUserForUID(newUid) != null)
			{
				throw new DuplicateUidException("User with email " + newUid + " already exists.");
			}
		}
		catch (final UnknownIdentifierException unknownIdentifierException)
		{
			// That's ok - user for new uid was not found
		}
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

	protected EventService getEventService()
	{
		return eventService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}


	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		event.setSite(getBaseSiteService().getCurrentBaseSite());
		event.setCustomer(customerModel);
		event.setLanguage(getCommonI18NService().getCurrentLanguage());
		event.setCurrency(getCommonI18NService().getCurrentCurrency());
		return event;
	}

}
