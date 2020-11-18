/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.util.Assert;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.core.daos.GallagherCustomerDao;
import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.enums.BU;
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
			if (retrieveUserBySubjectId.size() == 1)
			{
				final CustomerModel retrieveUser = retrieveUserBySubjectId.get(0);
				Boolean loginValue = null;
				loginValue = retrieveUser.isLoginDisabled();
				if (SiteChannel.B2C.equals(channel))
				{
					if (retrieveUser.getClass() == B2BCustomerModel.class)
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
					else
					{
						/* Update name and email only if updated to avoid sending data to SCPI */
						if (!retrieveUser.getName().equals(token.getName()) || !retrieveUser.getEmailID().equals(token.getEmail()))
						{
							retrieveUser.setName(token.getName());
							retrieveUser.setUid(token.getEmail());
						}
						retrieveUser.setIsUserExist(true);
						modelService.save(retrieveUser);
					}
				}
				else if (SiteChannel.B2B.equals(channel))
				{
					if (retrieveUser.getClass() == CustomerModel.class)
					{
						LOGGER.error("B2C Customer is not allowed to login in B2B website");
						throw new BadCredentialsException("B2C Customer is not allowed to login in B2B website");
					}
					else if (loginValue != null && loginValue == true)
					{
						LOGGER.error("B2B Customer is Disabled");
						throw new DisabledException("B2B Customer is Disabled");
					}
					else
					{
						updateCustomerFromC4C(retrieveUser);
					}
				}
			}
			else
			{
				for (final CustomerModel retrieveUser : retrieveUserBySubjectId)
				{
					if (SiteChannel.B2C.equals(channel) && retrieveUser.getClass() == CustomerModel.class)
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
					else if (SiteChannel.B2B.equals(channel) && retrieveUser.getClass() == B2BCustomerModel.class)
					{
						Boolean loginValue = null;
						loginValue = retrieveUser.isLoginDisabled();
						if (loginValue != null && loginValue == true)
						{
							LOGGER.error("B2B Customer is Disabled");
							throw new DisabledException("B2B Customer is Disabled");
						}
						else
						{
							updateCustomerFromC4C(retrieveUser);
						}
					}
				}
			}
			success = true;
		}
		return success;
	}

	private boolean createCommerceCustomer(final GallagherAccessToken token) throws DuplicateUidException
	{

		final CustomerModel newCustomer = modelService.create(CustomerModel.class);

		newCustomer.setName(token.getName());
		newCustomer.setUid(BU.AM.getCode().toLowerCase() + "|" + token.getEmail());
		newCustomer.setKeycloakGUID(token.getSubjectId());
		newCustomer.setIsUserExist(false);
		newCustomer.setBusinessUnit(BU.AM);
		newCustomer.setEmailID(token.getEmail());

		//check if customer exist in the C4C
		/*
		 * final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
		 * .getCustomerInfoFromC4C(token.getEmail(), token.getSubjectId());
		 */


		final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
				.getCustomerInfoFromC4C(token.getEmail(), token.getSubjectId(), BU.AM.getCode());


		//if customer exist and count > 1 then
		//set commerceCustomer flag duplicate = true
		//else if customer count = 1 then update C4C
		if (CollectionUtils.isNotEmpty(existingCustomers))
		{
			newCustomer.setSapAccountID(existingCustomers.get(0).getAccountID());
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
		/*
		 * final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
		 * .getCustomerInfoFromC4C(customer.getUid(), customer.getKeycloakGUID());
		 */


		final List<GallagherInboundCustomerEntry> existingCustomers = gallagherC4COutboundServiceFacade
				.getCustomerInfoFromC4C(customer.getEmailID(), customer.getKeycloakGUID(), BU.SEC.getCode());

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
			if (StringUtils.isNotEmpty(c4cCustomer.getEmail())
					&& (customer.getUid() == null || !customer.getEmailID().equals(c4cCustomer.getEmail())))
			{
				customer.setUid("sec|" + c4cCustomer.getEmail());
				updated = true;
			}
			if (StringUtils.isNotEmpty(c4cCustomer.getStatusCode()) && c4cCustomer.getStatusCode().equalsIgnoreCase("4")
					&& !customer.isLoginDisabled())
			{
				customer.setLoginDisabled(true);
				updated = true;
			}
			if (updated)
			{
				modelService.save(customer);
				final CustomerData customerData = new CustomerData();
				customerData.setUid(customer.getUid());
				customerData.setKeycloakGUID(customer.getKeycloakGUID());
				customerData.setFirstName(c4cCustomer.getFirstName());
				customerData.setLastName(c4cCustomer.getLastName());
				customerData.setLoginDisabled(customer.isLoginDisabled());
				/*
				 * if(customer.getBackOfficeLoginDisabled() == true) { customerData.setBackofficeLoginValue(c4c.get) }
				 */
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
			if (newUidLower.startsWith("am|"))
			{
				currentUser.setEmailID(newUidLower.substring(3, newUidLower.length()));
			}
			else
			{
				currentUser.setEmailID(newUidLower);
			}
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
