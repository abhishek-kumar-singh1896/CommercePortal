/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Listener for duplicate customer registration events.
 */
public class GallagherDuplicateCustomerRegisterationEventListener extends AbstractEventListener<GallagherDuplicateRegistrationEvent>
{
	private final EmailService emailService;
	private final ConfigurationService configurationService;


	public EmailService getEmailService()
	{
		return emailService;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	@Autowired
	public GallagherDuplicateCustomerRegisterationEventListener(final ConfigurationService configurationService,
			final EmailService emailService)
	{
		this.configurationService = configurationService;
		this.emailService = emailService;
	}


	@Override
	protected void onEvent(final GallagherDuplicateRegistrationEvent event)
	{
		final CustomerModel customer = event.getCustomer();

		if (Boolean.TRUE.equals(customer.getDuplicate()) || customer.getContactEmail().contains("vikram"))
		{
			final List<EmailAddressModel> toEmailAddresses = getToEmailAddresses();
			final EmailAddressModel fromEmailAddress = getFromEmailAddress();


			final String emailSubject = getConfigurationService().getConfiguration()
					.getString("gallagher.customer.registration.email.subject");
			final String emailBody = MessageFormat.format(
					getConfigurationService().getConfiguration().getString("gallagher.customer.registration.email.body"),
					customer.getContactEmail(), customer.getDisplayName());

			final EmailMessageModel emailMessage = getEmailService().createEmailMessage(toEmailAddresses, Collections.emptyList(),
					Collections.emptyList(), fromEmailAddress, fromEmailAddress.getEmailAddress(), emailSubject, emailBody,
					Collections.emptyList());

			getEmailService().send(emailMessage);
		}

	}

	/**
	 * @return EmailAddressModel
	 */
	private EmailAddressModel getFromEmailAddress()
	{
		final String fromEmailAddress = getConfigurationService().getConfiguration().getString("gallagher.notification.email.from");

		return getEmailService().getOrCreateEmailAddressForEmail(fromEmailAddress, fromEmailAddress);
	}

	/**
	 * @return List<EmailAddressModel>
	 */
	private List<EmailAddressModel> getToEmailAddresses()
	{
		final List<EmailAddressModel> emailAddresses = new ArrayList<>();

		final String toEmailAddressesString = getConfigurationService().getConfiguration().getString("gallagher.admin.emails");
		final List<String> toEmailAddresses = new ArrayList<>(Arrays.asList(toEmailAddressesString.split(",")));

		for (final String toEmailAddress : toEmailAddresses)
		{
			final EmailAddressModel toEmailAddressModel = getEmailService().getOrCreateEmailAddressForEmail(toEmailAddress,
					toEmailAddress);

			emailAddresses.add(toEmailAddressModel);
		}

		return emailAddresses;
	}

}
