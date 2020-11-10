/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.core.email.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;


/**
 * Service to render email message.
 *
 * @author Vikram Bishnoi
 */
public class GallagherEmailGenerationServiceImpl extends DefaultEmailGenerationService
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	private static final String REPLY_TO = "replyTo";

	private static final String ORDER_CONTEXT_CLASS_NAME = "GallagherOrderNotificationEmailContext";
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
				emailContext.getToDisplayName());
		toEmails.add(toAddress);
		if (emailContext.getClass().getSimpleName().equals(ORDER_CONTEXT_CLASS_NAME))
		{
		final String uid = emailContext.getBaseSite().getUid();
			final String email = getConfigurationService().getConfiguration().getString(uid);
		final EmailAddressModel somAddress = getEmailService().getOrCreateEmailAddressForEmail(email,
				emailContext.getToDisplayName());
		toEmails.add(somAddress);
		}
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());
		String replyTo = (String) emailContext.get(REPLY_TO);
		if (StringUtils.isEmpty(replyTo))
		{
			replyTo = emailContext.getFromEmail();
		}
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
				new ArrayList<EmailAddressModel>(), fromAddress, replyTo, emailSubject, emailBody, null);
	}

}
