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
package com.enterprisewide.b2badvance.outboundintegration.services;

import org.springframework.http.HttpMethod;

import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceBaseRequest;
import com.enterprisewide.b2badvance.outboundintegration.response.B2BAdvanceBaseResponse;

/**
 * The Interface B2BAdvanceBaseOutboundIntegrationService. It is used to hold
 * common operations required all other outbound services.
 *
 * @author Enterprisewide
 */
public interface B2BAdvanceBaseOutboundIntegrationService {

	/**
	 * Forwards request to rest client.
	 *
	 * @param                  <T> the generic type
	 * @param                  <V> the value type
	 * @param url              the URL
	 * @param method           the method
	 * @param fsRequest        the request
	 * @param fsResponseObject the response class
	 * @return class V that extends FSBaseResponse
	 */
	<T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject)
			throws B2BAdvanceWebServiceFailureException;

	/**
	 * Forwards request by appending an auth access token to the header.
	 *
	 * @param                  <T> the generic type
	 * @param                  <V> the value type
	 * @param url              the URL
	 * @param method           the method
	 * @param fsRequest        the request
	 * @param fsResponseObject the response class
	 * @param accessToken      the access token
	 * @return the v
	 */
	<T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject, final String accessToken)
			throws B2BAdvanceWebServiceFailureException;

	/**
	 * Forwards request TO SAP systems appending SAP specific header.
	 *
	 * @param                  <T> the generic type
	 * @param                  <V> the value type
	 * @param url              the URL
	 * @param method           the method
	 * @param fsRequest        the request
	 * @param fsResponseObject the response class
	 *
	 * @param plainCredentials for basic authentication
	 *
	 * @return the v
	 */
	<T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendSAPRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject, String plainCredentials)
			throws B2BAdvanceWebServiceFailureException;
}
