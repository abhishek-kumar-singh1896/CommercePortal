/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.c4c.outboundservices.facade.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.outboundservices.facade.impl.DefaultOutboundServiceFacade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.gallagher.c4c.outboundservices.facade.GallagherC4COutboundServiceFacade;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerEntry;
import com.gallagher.outboundservices.response.dto.GallagherInboundCustomerInfo;
import com.gallagher.outboundservices.response.dto.GallagherRegisteredProduct;


/**
 * Implementation of GallagherC4COutboundServiceFacade
 *
 * @author shishirkant
 */
public class GallagherC4COutboundServiceFacadeImpl extends DefaultOutboundServiceFacade
		implements GallagherC4COutboundServiceFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherC4COutboundServiceFacadeImpl.class);
	private static final String OUTBOUND_CONTACT_COLLECTION_DESTINATION = "scpiContactCollectionDestination";
	private static final String REGISTER_PRODUCT_DESTINATION = "scpiRegisterProductDestination";
	private static final String REGISTERED_PRODUCT_COLLECTION_DESTINATION = "scpiRegisteredProductCollectionDestination";

	/**
	 * {@inheritDoc}
	 */
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
				Collections.sort(existingCustomers, Collections.reverseOrder());
			}
		}
		return existingCustomers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProduct(final RegisterProductRequest request)
	{
		final ConsumedDestinationModel destinationModel = getConsumedDestinationModelById(REGISTER_PRODUCT_DESTINATION);
		final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);

		final String baseURL = destinationModel.getUrl();

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		final HttpEntity entity = new HttpEntity(request, headers);

		restOperations.exchange(baseURL, HttpMethod.POST, entity, String.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GallagherRegisteredProduct> getRegisteredProductFromC4C(final String email)
	{
		final ConsumedDestinationModel destinationModel = getConsumedDestinationModelById(
				REGISTERED_PRODUCT_COLLECTION_DESTINATION);
		final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);

		final String baseURL = destinationModel.getUrl();

		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL).queryParam("$filter",
				"Email eq '" + email + "'");

		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		final HttpEntity entity = new HttpEntity(headers);

		//final HttpEntity<GallagherRegisteredProduct[]> response = restOperations.exchange(builder.build().encode().toUri(),
		//		HttpMethod.GET, entity, GallagherRegisteredProduct[].class);

		//return Arrays.asList(response.getBody());

		final List<GallagherRegisteredProduct> registeredProductsList = new ArrayList<GallagherRegisteredProduct>();
		for (int i = 0; i <= 5; i++)
		{
			final GallagherRegisteredProduct registeredProduct = new GallagherRegisteredProduct();
			registeredProduct.setAttachment("this is attachment");
			registeredProduct.setAttachmentUrl("https://images.google.com");
			registeredProduct.setCode("solar-fence-energizer-s10");
			registeredProduct.setName("Solar Fence Energiser");
			registeredProduct.setImage(null);

			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			try
			{
				registeredProduct.setPurchaseDate(simpleDateFormat.parse("12/01/2020"));
				registeredProduct.setRegistrationDate(simpleDateFormat.parse("15/03/2020"));
			}
			catch (final ParseException e)
			{
				e.printStackTrace();
			}
			registeredProductsList.add(registeredProduct);
		}
		return registeredProductsList;
	}

}
