/**
 *
 */
package com.gallagher.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;


/**
 * @author shilpiverma
 *
 */
public class GallagherCustomerEmailResolutionServiceImpl extends DefaultCustomerEmailResolutionService
{
	private static final Logger LOG = Logger.getLogger(GallagherCustomerEmailResolutionServiceImpl.class);

	@Override
	public String getEmailForCustomer(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		if (customerModel instanceof B2BCustomerModel
				&& StringUtils.equals(customerModel.getEmailID(), ((B2BCustomerModel) customerModel).getEmail()))
		{
			return ((B2BCustomerModel) customerModel).getEmail();
		}
		else
		{
			final String emailAfterProcessing = validateAndProcessEmailForCustomer(customerModel);
			if (StringUtils.isNotEmpty(emailAfterProcessing))
			{
				return emailAfterProcessing;
			}

			return getConfigurationService().getConfiguration().getString(DEFAULT_CUSTOMER_KEY, DEFAULT_CUSTOMER_EMAIL);
		}

	}

	@Override
	protected String validateAndProcessEmailForCustomer(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		final String email = CustomerType.GUEST.equals(customerModel.getType())
				? StringUtils.substringAfter(customerModel.getUid(), "|")
				: customerModel.getEmailID();
		try
		{
			MailUtils.validateEmailAddress(email, "customer email");
			return email;
		}
		catch (final EmailException e) //NOSONAR
		{
			LOG.info("Given uid is not appropriate email. Customer PK: " + String.valueOf(customerModel.getPk()) + " Exception: "
					+ e.getClass().getName());
		}
		return null;
	}
}
