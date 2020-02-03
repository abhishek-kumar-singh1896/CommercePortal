/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.facade.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.gallagher.outboundservices.dto.inbound.customer.response.GallagherInboundCustomerInfo;
import com.gallagher.outboundservices.facade.GallagherOutboundServiceFacade;


/**
 *
 */
public class GallagherOutboundServiceFacadeImpl implements GallagherOutboundServiceFacade
{
	private static final Logger LOGGER = Logger.getLogger(GallagherOutboundServiceFacadeImpl.class.getName());
	private static final String OUTBOUND_CONTACT_COLLECTION_DESTINATION = "scpiContactCollectionDestination";

	private final FlexibleSearchService flexibleSearchService;
	private final IntegrationRestTemplateFactory integrationRestTemplateFactory;

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public IntegrationRestTemplateFactory getIntegrationRestTemplateFactory()
	{
		return integrationRestTemplateFactory;
	}

	@Autowired
	public GallagherOutboundServiceFacadeImpl(final FlexibleSearchService flexibleSearchService,
			final IntegrationRestTemplateFactory integrationRestTemplateFactory)
	{
		this.flexibleSearchService = flexibleSearchService;
		this.integrationRestTemplateFactory = integrationRestTemplateFactory;
	}

	@Override
	public GallagherInboundCustomerInfo getCustomerInfoFromC4C(final String email)
	{
		final ConsumedDestinationModel destinationModel = getConsumedDestinationModelById(OUTBOUND_CONTACT_COLLECTION_DESTINATION);
		final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);

		final String baseURL = destinationModel.getUrl();

		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL).queryParam("$filter",
				"Email eq '" + email + "'");

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		final HttpEntity entity = new HttpEntity(headers);

		final HttpEntity<GallagherInboundCustomerInfo> response = restOperations.exchange(builder.build().encode().toUri(),
				HttpMethod.GET, entity, GallagherInboundCustomerInfo.class);

		return response.getBody();
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
