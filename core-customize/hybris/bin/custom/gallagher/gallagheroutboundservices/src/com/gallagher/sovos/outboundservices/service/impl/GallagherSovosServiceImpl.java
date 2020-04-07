/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sovos.outboundservices.service.impl;

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
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gallagher.outboundservices.request.dto.GallagherSovosCalculateTaxRequest;
import com.gallagher.outboundservices.response.dto.GallagherSovosCalculatedTaxResponse;
import com.gallagher.sovos.outboundservices.service.GallagherSovosService;


/**
 * Implementation of GallagherSovosService
 *
 * @author shishirkant
 */
public class GallagherSovosServiceImpl implements GallagherSovosService
{
	private static final Logger LOGGER = Logger.getLogger(GallagherSovosServiceImpl.class);

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
			LOGGER.error("Exception occured while creating Sovos HMAC Signature.", exception);
		}

		final HttpEntity<GallagherSovosCalculateTaxRequest> entity = new HttpEntity<>(request, headers);

		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL + restURL);

		final ResponseEntity<GallagherSovosCalculatedTaxResponse> result = restTemplate.exchange(builder.build().encode().toUri(),
				HttpMethod.POST, entity, GallagherSovosCalculatedTaxResponse.class);

		return result.getBody();

	}

}
