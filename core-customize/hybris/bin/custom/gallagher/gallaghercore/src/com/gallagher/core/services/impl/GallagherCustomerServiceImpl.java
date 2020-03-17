/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
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
 * Gallagher customer service implementation for create
 *
 * and update customer in commerce and SCPI/C4C
 *
 * @author abhishek
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

	//update commerce user if exist
	@Override
	public boolean updateCommerceCustomer(final GallagherAccessToken token)
	{
		boolean success = false;

		final List<CustomerModel> retrieveUserBySubjectId = gallagherCustomerDao
				.retrieveUserByKeycloakGUID(token.getSubjectId());

		if (CollectionUtils.isEmpty(retrieveUserBySubjectId))
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
			final CustomerModel retrieveUser = retrieveUserBySubjectId.get(0);
			retrieveUser.setName(token.getName());
			retrieveUser.setUid(token.getEmail());
			retrieveUser.setIsUserExist(true);
			modelService.save(retrieveUser);
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
				.getCustomerInfoFromC4C(token.getEmail());


		//if customer exist and count > 1 then
		//set commerceCustomer flag duplicate = true
		//else if customer count = 1 then update C4C
		if (CollectionUtils.isNotEmpty(existingCustomers))
		{
			if (existingCustomers.size() > 1)
			{
				newCustomer.setDuplicate(true);
			}
			else
			{
				newCustomer.setObjectID(existingCustomers.get(0).getObjectID());
				newCustomer.setSapContactID(existingCustomers.get(0).getContactID());
			}
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
}