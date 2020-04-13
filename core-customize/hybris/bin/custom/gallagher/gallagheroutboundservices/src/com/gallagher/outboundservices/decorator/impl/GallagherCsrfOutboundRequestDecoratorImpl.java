/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.outboundservices.decorator.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.integrationservices.cache.IntegrationCache;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;
import de.hybris.platform.outboundservices.decorator.CSRFTokenFetchingException;
import de.hybris.platform.outboundservices.decorator.impl.csrf.CsrfParametersCacheKey;
import de.hybris.platform.outboundservices.decorator.impl.csrf.DefaultCsrfOutboundRequestDecorator;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.gallagher.outboundservices.decorator.GallagherCsrfOutboundRequestDecorator;


/**
 * Implementation of GallagherCsrfOutboundRequestDecorator.
 */
public class GallagherCsrfOutboundRequestDecoratorImpl implements GallagherCsrfOutboundRequestDecorator
{
	private static final String X_CSRF_TOKEN = "X-CSRF-Token";
	private static final String X_CSRF_TOKEN_FETCH = "Fetch";
	private static final String X_CSRF_COOKIE = "Cookie";
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCsrfOutboundRequestDecorator.class);
	private static final String CSRF_URL = "csrfURL";

	private IntegrationRestTemplateFactory integrationRestTemplateFactory;
	private IntegrationCache<CsrfParametersCacheKey, CsrfParameters> cache;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpHeaders decorate(final HttpHeaders httpHeaders, final ConsumedDestinationModel destinationModel)
	{
		if (destinationModel.getAdditionalProperties().containsKey(CSRF_URL))
		{
			final CsrfParametersCacheKey key = CsrfParametersCacheKey.from(destinationModel);
			final CsrfParameters csrfParameters = retrieveCsrfParameters(destinationModel, key);

			httpHeaders.set(X_CSRF_TOKEN, csrfParameters.getCsrfToken());
			httpHeaders.set(X_CSRF_COOKIE, csrfParameters.getCsrfCookie());
		}
		return httpHeaders;
	}

	private synchronized CsrfParameters retrieveCsrfParameters(final ConsumedDestinationModel destinationModel,
			final CsrfParametersCacheKey key)
	{
		CsrfParameters parameters = getCache().get(key);
		if (parameters == null)
		{
			parameters = fetchCsrfTokenFromSCPI(destinationModel);
			getCache().put(key, parameters);
		}
		return parameters;
	}

	protected CsrfParameters fetchCsrfTokenFromSCPI(final ConsumedDestinationModel destinationModel)
	{
		final HttpHeaders headers = getHeadersForTokenFetching();

		final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);
		final String tokenUrl = destinationModel.getAdditionalProperties().get(CSRF_URL);
		final ResponseEntity<Map> csrfTokenExchange = restOperations.exchange(tokenUrl, HttpMethod.GET, new HttpEntity(headers),
				Map.class);

		try
		{
			final CsrfParameters csrfParameters = CsrfParameters.create(csrfTokenExchange.getHeaders());
			LOG.debug("The CSRF token and cookies have been fetched from {}", destinationModel.getId());
			return csrfParameters;
		}
		catch (final IllegalArgumentException e)
		{
			LOG.debug("Invalid CSRF related header parameter", e);
			throw new CSRFTokenFetchingException("Failed to fetch the CSRF token or CSRF cookies from " + destinationModel.getId());
		}
	}

	protected static HttpHeaders getHeadersForTokenFetching()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.set(X_CSRF_TOKEN, X_CSRF_TOKEN_FETCH);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	protected IntegrationRestTemplateFactory getIntegrationRestTemplateFactory()
	{
		return integrationRestTemplateFactory;
	}

	@Required
	public void setIntegrationRestTemplateFactory(final IntegrationRestTemplateFactory integrationRestTemplateFactory)
	{
		this.integrationRestTemplateFactory = integrationRestTemplateFactory;
	}

	@Required
	public void setCache(final IntegrationCache<CsrfParametersCacheKey, CsrfParameters> cache)
	{
		this.cache = cache;
	}

	protected IntegrationCache<CsrfParametersCacheKey, CsrfParameters> getCache()
	{
		return cache;
	}
}