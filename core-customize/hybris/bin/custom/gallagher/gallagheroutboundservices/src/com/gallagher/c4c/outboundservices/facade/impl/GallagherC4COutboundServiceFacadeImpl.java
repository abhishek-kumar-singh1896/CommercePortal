/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.outboundservices.dto.inbound.customer.response.GallagherInboundCustomerEntry;
import com.gallagher.outboundservices.dto.inbound.customer.response.GallagherInboundCustomerInfo;


/**
 * Implementation of GallagherC4COutboundServiceFacade
 */
public class GallagherC4COutboundServiceFacadeImpl implements GallagherC4COutboundServiceFacade
{
	private static final Logger LOGGER = Logger.getLogger(GallagherC4COutboundServiceFacadeImpl.class.getName());
	private static final String OUTBOUND_CONTACT_COLLECTION_DESTINATION = "scpiContactCollectionDestination";

	private final FlexibleSearchService flexibleSearchService;
	private final IntegrationRestTemplateFactory integrationRestTemplateFactory;

	@Resource
	private DefaultUserService userService;

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public IntegrationRestTemplateFactory getIntegrationRestTemplateFactory()
	{
		return integrationRestTemplateFactory;
	}

	@Autowired
	public GallagherC4COutboundServiceFacadeImpl(final FlexibleSearchService flexibleSearchService,
			final IntegrationRestTemplateFactory integrationRestTemplateFactory)
	{
		this.flexibleSearchService = flexibleSearchService;
		this.integrationRestTemplateFactory = integrationRestTemplateFactory;
	}

	@Override
	public List<GallagherInboundCustomerEntry> getCustomerInfoFromC4C(final String email)
	{
		final GallagherInboundCustomerEntry existingCustomer = new GallagherInboundCustomerEntry();
		List<GallagherInboundCustomerEntry> existingCustomers = new ArrayList<>();
		final EmailValidator eValidator = EmailValidator.getInstance();

		if (!eValidator.isValid(email))
		{
			existingCustomer.setEmailError("invalid");
			existingCustomers.add(existingCustomer);
		}
		else if (userService.isUserExisting(email))
		{
			existingCustomer.setEmailError("duplicate");
			existingCustomers.add(existingCustomer);
		}
		else
		{

			final ConsumedDestinationModel destinationModel = getConsumedDestinationModelById(
					OUTBOUND_CONTACT_COLLECTION_DESTINATION);
			final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);

			final String baseURL = destinationModel.getUrl();

			final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL).queryParam("$filter",
					"Email eq '" + email + "'");

			final HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			final HttpEntity entity = new HttpEntity(headers);

			final HttpEntity<GallagherInboundCustomerInfo> response = restOperations.exchange(builder.build().encode().toUri(),
					HttpMethod.GET, entity, GallagherInboundCustomerInfo.class);

			final GallagherInboundCustomerInfo existingCustomerInfo = response.getBody();

			if (null != existingCustomerInfo && null != existingCustomerInfo.getCustomerInfo()
					&& null != existingCustomerInfo.getCustomerInfo().getCustomerEntries())
			{
				existingCustomers = existingCustomerInfo.getCustomerInfo().getCustomerEntries();
			}
		}
		return existingCustomers;
	}

	protected ConsumedDestinationModel getConsumedDestinationModelById(final String destinationId)
	{
		ConsumedDestinationModel destination = null;
		try
		{
			final ConsumedDestinationModel example = new ConsumedDestinationModel();
			example.setId(destinationId);
			destination = getFlexibleSearchService().getModelByExample(example);
		}
		catch (final RuntimeException exception)
		{
			LOGGER.warn("Failed to find ConsumedDestination with id: " + destinationId, exception);
		}
		return destination;
	}

}
