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
package com.enterprisewide.b2badvance.outboundintegration.services.impl;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import com.enterprisewide.b2badvance.outboundintegration.client.B2BAdvanceRestClient;
import com.enterprisewide.b2badvance.outboundintegration.constants.B2badvanceoutboundintegrationConstants;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceHeaderRequestDTO;
import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceBaseRequest;
import com.enterprisewide.b2badvance.outboundintegration.response.B2BAdvanceBaseResponse;
import com.enterprisewide.b2badvance.outboundintegration.services.B2BAdvanceBaseOutboundIntegrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * The Class FSBaseOutboundIntegrationServiceImpl is base class for all the
 * services. It is responsible for creating header which is sent in all the
 * request. The class is also responsible for creating HTTP header template for
 * authentication of web service. The class forwards all the request to rest
 * client after encapsulating header and HTTP header in request.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceBaseOutboundIntegrationServiceImpl implements B2BAdvanceBaseOutboundIntegrationService {
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceBaseOutboundIntegrationServiceImpl.class);

	protected static final String ROOT_URL = "portal.root.url";

	protected static final String OAUTH_ROOT_URL = "portal.root.oauth.root.url";

	private static final String ENABLE_REQUEST_LOGGING_KEY = "enable.web.service.request.logging";

	private static final String ENABLE_RESPONSE_LOGGING_KEY = "enable.web.service.response.logging";

	private static final String ENABLE_RESPONSE_HEADER_LOGGING_KEY = "enable.web.service.response.header.logging";

	private static final String HEADER_SYSTEM_ID = "header.system.id";

	private static final String HEADER_USER_ID = "header.user.id";

	private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

	private ModelService modelService;

	private ConfigurationService configurationService;

	private B2BAdvanceRestClient b2BAdvanceRestClient;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject)
			throws B2BAdvanceWebServiceFailureException {
		return sendRequest(url, method, fsRequest, fsResponseObject, StringUtils.EMPTY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendSAPRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject, String plainCredentials)
			throws B2BAdvanceWebServiceFailureException {
		appendHeaderToRequest(fsRequest);
		final MultiValueMap<String, String> headers = getHeadersWithBasicAuth(plainCredentials);
		return createAndSendRequest(url, method, fsRequest, fsResponseObject, headers);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws FSWebServiceFailureException
	 */
	@Override
	public <T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V sendRequest(final String url,
			final HttpMethod method, final T fsRequest, final Class fsResponseObject, final String accessToken)
			throws B2BAdvanceWebServiceFailureException {
		final MultiValueMap<String, String> headers = StringUtils.isNotBlank(accessToken)
				? getAuthenticationHeaders(accessToken)
				: getHeaders();
		return createAndSendRequest(url, method, fsRequest, fsResponseObject, headers);
	}

	/**
	 * Creates and sends request.
	 *
	 * @param                  <T> the generic type
	 * @param                  <V> the value type
	 * @param url              the URL
	 * @param method           the method
	 * @param fsRequest        the request
	 * @param fsResponseObject the response class
	 * @return the v
	 * @throws FSWebServiceFailureException
	 */
	protected <T extends B2BAdvanceBaseRequest, V extends B2BAdvanceBaseResponse> V createAndSendRequest(
			final String url, final HttpMethod method, final T fsRequest, final Class fsResponseObject,
			final MultiValueMap<String, String> headers) throws B2BAdvanceWebServiceFailureException {
		final HttpEntity<T> requestEntity = new HttpEntity<T>(fsRequest, headers);

		final String transactionId = RandomStringUtils.random(16, true, true);
		logRequest(fsRequest, url, method, transactionId);

		final V response = getB2BAdvanceRestClient().<T, V>sendRequest(url, method, requestEntity, fsResponseObject);

		logResponse(response, transactionId);

		return response;
	}

	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	protected MultiValueMap<String, String> getHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	/**
	 * Gets the headers.
	 * 
	 * @param plainCredentials for basic authentication header
	 *
	 * @return the headers
	 */
	protected MultiValueMap<String, String> getHeadersWithBasicAuth(String plainCredentials) {
		final String encodedCredentials = getEncodedCredentials(plainCredentials);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);
		return headers;
	}

	protected String getEncodedCredentials(String plainCredentials) {
		final byte[] plainCredentialsBytes = plainCredentials.getBytes(StandardCharsets.UTF_8);
		final byte[] base64EncodedCredentials = Base64.getEncoder().encode(plainCredentialsBytes);
		return new String(base64EncodedCredentials, StandardCharsets.UTF_8);
	}

	/**
	 * Implementation classes will provide the credentials
	 */
	protected String getEncodedCredentials() {
		return null;
	}

	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	private MultiValueMap<String, String> getAuthenticationHeaders(final String accessToken) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + accessToken);
		return headers;
	}

	/**
	 * Append header to request.
	 *
	 * @param           <T> the generic type
	 * @param fsRequest the request
	 * @return the t
	 */
	protected <T extends B2BAdvanceBaseRequest> T appendHeaderToRequest(final T fsRequest) {
		final B2BAdvanceHeaderRequestDTO header = createHeader();
		fsRequest.setHeader(header);
		return fsRequest;
	}

	/**
	 * Generate common header sent in all request.
	 *
	 * @return object of type FSHeaderRequestDTO
	 */
	protected B2BAdvanceHeaderRequestDTO createHeader() {
		final B2BAdvanceHeaderRequestDTO header = new B2BAdvanceHeaderRequestDTO();
		final Configuration configuration = getConfiguration();
		header.setUserID(configuration.getString(HEADER_USER_ID));
		header.setSystemID(configuration.getString(HEADER_SYSTEM_ID));
		header.setTimestamp(DATE_FORMAT.format(new Date()));
		return header;
	}

	/**
	 * Log request.
	 *
	 * @param               <T> the generic type
	 * @param fsRequest     the request object
	 * @param url           the URL
	 * @param method        the method
	 * @param transactionId the transaction id
	 */
	private <T extends B2BAdvanceBaseRequest> void logRequest(final T fsRequest, final String url,
			final HttpMethod method, final String transactionId) {
		if (isRequestLoggingEnabled()) {
			final String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId)
					.append(" REQUEST ").append(method).append(' ').append(url).append(":-\n\n{}").toString();
			logJsonObject(fsRequest, infoLogString, "JSON processing error occurred for request");
		}
	}

	/**
	 * Log response.
	 *
	 * @param               <V> the value type
	 * @param response      the response
	 * @param transactionId the transaction id
	 */
	private <V extends B2BAdvanceBaseResponse> void logResponse(final V response, final String transactionId) {
		if (isResponseLoggingEnabled()) {
			logJsonObject(response, "\nTRANSACTION ID:-" + transactionId + " RESPONSE:-\n\n{}",
					"JSON processing error occurred for response");
		} else if ((response != null) && (response.getHeader() != null) && (isResponseHeaderLoggingEnabled())) {
			logJsonObject(response.getHeader(), "\nRequest was successful.\n\n{}",
					"JSON processing error occurred for response header");
		}
	}

	/**
	 * This method decides if the request should be logged.
	 *
	 * @return true, if request is to be logged.
	 */
	private boolean isRequestLoggingEnabled() {
		return getConfigurationService().getConfiguration().getBoolean(ENABLE_REQUEST_LOGGING_KEY, false);
	}

	/**
	 * This method decides if the response should be logged.
	 *
	 * @return true, if response is to be logged.
	 */
	private boolean isResponseLoggingEnabled() {
		return getConfigurationService().getConfiguration().getBoolean(ENABLE_RESPONSE_LOGGING_KEY, false);
	}

	/**
	 * This method decides if the response header should be logged or not.
	 *
	 * @return true, if response headers are to be logged.
	 */
	private boolean isResponseHeaderLoggingEnabled() {
		return getConfigurationService().getConfiguration().getBoolean(ENABLE_RESPONSE_HEADER_LOGGING_KEY, false);
	}

	/**
	 * Logs the JSON object.
	 *
	 * @param object         JSON object to be logged.
	 * @param infoLogString  the info log string
	 * @param errorLogString the error log string
	 */
	private void logJsonObject(final Object object, final String infoLogString, final String errorLogString) {
		try {
			final String objectJson = OBJECT_WRITER.writeValueAsString(object);
			LOG.info(infoLogString, objectJson);
		} catch (final JsonProcessingException jPE) {
			LOG.error(errorLogString, jPE);
		}
	}

	/**
	 * Checks if the request whose response is given was successful or not.
	 *
	 * @param response response to be checked for success response code
	 * @return true, if response header has success response code.
	 * @throws FSWebServiceFailureException
	 */
	protected boolean isRequestSuccessful(final B2BAdvanceBaseResponse response)
			throws B2BAdvanceWebServiceFailureException {
		boolean requestStatus = false;
		if ((response != null) && (response.getHeader() != null)) {
			if ((B2badvanceoutboundintegrationConstants.SUCCESS_RESPONSE_PATTERN
					.matcher(response.getHeader().getResponseCode()).matches())) {
				requestStatus = true;
			} else if (response.getHeader().getResponseCode() != null) {
				throw new B2BAdvanceWebServiceFailureException(response.getHeader().getResponseCode());
			}
		}
		return requestStatus;
	}

	/**
	 * Gets the configuration container for retrieving further properties.
	 *
	 * @return the configuration
	 */
	protected Configuration getConfiguration() {
		return getConfigurationService().getConfiguration();
	}

	public ModelService getModelService() {
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public B2BAdvanceRestClient getB2BAdvanceRestClient() {
		return b2BAdvanceRestClient;
	}

	@Required
	public void setB2BAdvanceRestClient(final B2BAdvanceRestClient b2BAdvanceRestClient) {
		this.b2BAdvanceRestClient = b2BAdvanceRestClient;
	}

}
