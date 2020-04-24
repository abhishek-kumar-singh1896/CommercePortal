/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.core.daos.GallagherCustomerDao;
import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.services.GallagherCustomerService;
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
			if (customer.getName() == null || !customer.getName().equals(customerName))
			{
				customer.setName(customerName);
				modelService.save(customer);
			}
		}


		return true;
	}
}
