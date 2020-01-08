/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.enterprisewide.b2badvance.outboundintegration.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceHeaderResponseDTO;
import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceBaseRequest;
import com.enterprisewide.b2badvance.outboundintegration.response.B2BAdvanceBaseResponse;

/**
 * The Rest Client for B2B Advance outbound integrations.
 *
 * @author Enterprise Wide
 */
public final class B2BAdvanceRestClient {

	private RestTemplate restTemplate;

	/**
	 * Initial method for the client that sets up the rest template .
	 */
	public void setUpRestTemplate() {
		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	}

	/**
	 * Common Method to send a request.
	 *
	 * @param                 <T> the generic request type
	 * @param                 <V> the generic response type
	 * @param url             the URL
	 * @param method          the HTTP method
	 * @param fsRequestEntity the request entity
	 * @param responseClass   the response class
	 * @return class V that extends B2BAdvanceBaseResponse
	 */
	public <T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendRequest(final String url,
			final HttpMethod method, final HttpEntity<T> fsRequestEntity, final Class responseClass)
			throws B2BAdvanceWebServiceFailureException {
		ResponseEntity<V> result = null;
		try {
			result = restTemplate.exchange(url, method, fsRequestEntity, responseClass);
			if (result.getBody() == null) {
				final V body = (V) new B2BAdvanceBaseResponse();
				final B2BAdvanceHeaderResponseDTO header = new B2BAdvanceHeaderResponseDTO();
				header.setHttpStatus(result.getStatusCode());
				header.setResponseCode(result.getStatusCode().toString());
				body.setHeader(header);
				return body;
			} else {
				if (result.getBody().getHeader() == null) {
					result.getBody().setHeader(new B2BAdvanceHeaderResponseDTO());
					result.getBody().getHeader().setResponseCode(result.getStatusCode().toString());
				}
				result.getBody().getHeader().setHttpStatus(result.getStatusCode());
			}
		} catch (final RestClientException restClientException) {
			throw new B2BAdvanceWebServiceFailureException("Rest Client reported error", restClientException);
		}
		return result.getBody();
	}

}
