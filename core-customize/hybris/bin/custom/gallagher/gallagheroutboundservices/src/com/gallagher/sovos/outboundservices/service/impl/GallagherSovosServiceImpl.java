/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sovos.outboundservices.service.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;
import com.gallagher.outboundservices.request.dto.GallagherSovosGeoCodeRequest;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxResponse;
import com.gallagher.outboundservices.response.dto.GallagherSovosGeoCodeResponse;
import com.gallagher.outboundservices.util.GallagherSovosGeoCodeUtil;
import com.gallagher.sovos.outboundservices.service.GallagherSovosService;


/**
 * Implementation of GallagherSovosService
 *
 * @author shishirkant
 */
public class GallagherSovosServiceImpl implements GallagherSovosService
{
	private static final Logger LOG = LoggerFactory.getLogger(GallagherSovosServiceImpl.class);

	private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

	private static final String ENABLE_SOVOS_LOGGING_KEY = "sovos.service.request.logging";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GallagherSovosCalculatedTaxResponse calculateExternalTax(final GallagherSovosCalculateTaxRequest request)
	{
		final RestTemplate restTemplate = new RestTemplate();

		final String baseURL = configurationService.getConfiguration().getString("sovos.baseURL");
		final String restURL = configurationService.getConfiguration().getString("sovos.restURL");
		final String username = configurationService.getConfiguration().getString("sovos.username");
		final String password = configurationService.getConfiguration().getString("sovos.password");
		final String secretKey = configurationService.getConfiguration().getString("sovos.secretKey");

		request.setUsrname(username);
		request.setPswrd(password);

		final HttpHeaders headers = getHeaders(restURL, username, password, secretKey);

		final HttpEntity<GallagherSovosCalculateTaxRequest> entity = new HttpEntity<>(request, headers);

		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL + restURL);

		final String transactionId = RandomStringUtils.random(16, true, true);
		logRequest(request, transactionId);

		final ResponseEntity<GallagherSovosCalculatedTaxResponse> result = restTemplate.exchange(builder.build().encode().toUri(),
				HttpMethod.POST, entity, GallagherSovosCalculatedTaxResponse.class);
		logResponse(result.getBody(), transactionId);
		return result.getBody();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGeoCode(final AddressModel address)
	{
		final GallagherSovosGeoCodeRequest request = new GallagherSovosGeoCodeRequest();
		GallagherSovosGeoCodeUtil.convert(address, request);

		final RestTemplate restTemplate = new RestTemplate();
		final String baseURL = configurationService.getConfiguration().getString("sovos.baseURL");
		final String restURL = configurationService.getConfiguration().getString("sovos.geoCode.restURL");
		final String username = configurationService.getConfiguration().getString("sovos.username");
		final String password = configurationService.getConfiguration().getString("sovos.password");
		final String secretKey = configurationService.getConfiguration().getString("sovos.secretKey");
		final boolean oOCLmtInd = Boolean.FALSE;

		request.setUsrname(username);
		request.setPswrd(password);
		request.setoOCLmtInd(oOCLmtInd);

		final HttpHeaders headers = getHeaders(restURL, username, password, secretKey);

		final HttpEntity<GallagherSovosGeoCodeRequest> entity = new HttpEntity<>(request, headers);

		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL + restURL);

		final ResponseEntity<GallagherSovosGeoCodeResponse> result = restTemplate.exchange(builder.build().encode().toUri(),
				HttpMethod.POST, entity, GallagherSovosGeoCodeResponse.class);
		return result.getBody().getGeoCd();
	}

	/**
	 * Log request.
	 *
	 * @param request
	 *           the request object
	 * @param transactionId
	 *           the transaction id
	 */
	private void logRequest(final Object request, final String transactionId)
	{
		if (isLoggingEnabled())
		{
			logJsonObject(request, new StringBuilder(150).append("Request : SOVOS transaction ID ").append(transactionId).toString(),
					"JSON processing error occurred for request");
		}
	}

	/**
	 * Log response.
	 *
	 * @param response
	 *           the response object
	 * @param transactionId
	 *           the transaction id
	 */
	private void logResponse(final Object response, final String transactionId)
	{
		if (isLoggingEnabled())
		{
			logJsonObject(response,
					new StringBuilder(150).append("Response : SOVOS transaction ID ").append(transactionId).toString(),
					"JSON processing error occurred for request");
		}
	}

	/**
	 * Logs the JSON object.
	 *
	 * @param object
	 *           JSON object to be logged.
	 * @param infoLogString
	 *           the info log string
	 * @param errorLogString
	 *           the error log string
	 */
	private void logJsonObject(final Object object, final String infoLogString, final String errorLogString)
	{
		if (object != null)
		{
			try
			{
				final String objectJson = OBJECT_WRITER.writeValueAsString(object);
				LOG.info(infoLogString + "\n" + objectJson);
			}
			catch (final JsonProcessingException jPE)
			{
				LOG.error(errorLogString, jPE);
			}
		}
	}


	/**
	 * This method decides if the request should be logged.
	 *
	 * @return true, if request is to be logged.
	 */
	private boolean isLoggingEnabled()
	{
		return configurationService.getConfiguration().getBoolean(ENABLE_SOVOS_LOGGING_KEY, false);
	}

	/**
	 * create headers
	 */
	private HttpHeaders getHeaders(final String restURL, final String username, final String password, final String secretKey)
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		final TimeZone timeZone = TimeZone.getTimeZone("UTC");
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		dateFormat.setTimeZone(timeZone);
		final String dateISOString = dateFormat.format(new Date());

		headers.set("Date", dateISOString);

		try
		{
			final String input = HttpMethod.POST.toString() + MediaType.APPLICATION_JSON.toString() + dateISOString + restURL
					+ username + password;
			final SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
			final Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			final byte[] rawHmac = mac.doFinal(input.getBytes());
			final String hmacDigest = new String(Base64.encodeBase64(rawHmac));

			headers.set("Authorization", "TAX" + " " + username + ':' + hmacDigest);
		}
		catch (final NoSuchAlgorithmException | InvalidKeyException exception)
		{
			LOG.error("Exception occured while creating Sovos HMAC Signature.", exception);
		}
		return headers;
	}
}