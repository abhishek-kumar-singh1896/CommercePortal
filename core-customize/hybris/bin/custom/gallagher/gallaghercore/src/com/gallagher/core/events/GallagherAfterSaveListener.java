/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Gallagher implementation of {@link AfterSaveListener}
 */
public class GallagherAfterSaveListener implements AfterSaveListener
{
	private final ModelService modelService;
	private final EmailService emailService;
	private final ConfigurationService configurationService;


	public EmailService getEmailService()
	{
		return emailService;
	}


	public ModelService getModelService()
	{
		return modelService;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	@Autowired
	public GallagherAfterSaveListener(final ModelService modelService, final EmailService emailService,
			final ConfigurationService configurationService)
	{
		this.modelService = modelService;
		this.emailService = emailService;
		this.configurationService = configurationService;
	}

	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{
		for (final AfterSaveEvent event : events)
		{
			final int type = event.getType();
			if (AfterSaveEvent.CREATE == type)
			{
				final PK pk = event.getPk();

				if (pk.getTypeCode() == 4 && getModelService().get(pk) instanceof CustomerModel)
				{
					final CustomerModel customer = getModelService().get(pk);

					if (Boolean.TRUE.equals(customer.getDuplicate()))
					{
						final List<EmailAddressModel> toEmailAddresses = getToEmailAddresses();
						final EmailAddressModel fromEmailAddress = getFromEmailAddress();


						final String emailSubject = getConfigurationService().getConfiguration()
								.getString("gallagher.customer.registration.email.subject");
						final String emailBody = MessageFormat.format(
								getConfigurationService().getConfiguration().getString("gallagher.customer.registration.email.body"),
								customer.getContactEmail(), customer.getDisplayName());

						final EmailMessageModel emailMessage = getEmailService().createEmailMessage(toEmailAddresses,
								Collections.emptyList(), Collections.emptyList(), fromEmailAddress, fromEmailAddress.getEmailAddress(),
								emailSubject, emailBody, Collections.emptyList());

						getEmailService().send(emailMessage);
					}
				}
			}
		}
	}

	/**
	 * @return EmailAddressModel
	 */
	private EmailAddressModel getFromEmailAddress()
	{
		final String fromEmailAddress = getConfigurationService().getConfiguration().getString("gallagher.notification.email.from");
		final String displayName = getConfigurationService().getConfiguration().getString("gallagher.notification.email.name");

		return getEmailService().getOrCreateEmailAddressForEmail(fromEmailAddress, displayName);
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
