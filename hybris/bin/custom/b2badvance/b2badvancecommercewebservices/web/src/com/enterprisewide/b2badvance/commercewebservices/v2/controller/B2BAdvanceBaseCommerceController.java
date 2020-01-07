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
package com.enterprisewide.b2badvance.commercewebservices.v2.controller;


import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import com.enterprisewide.b2badvance.commercewebservices.product.dto.ProductErrorWsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


/**
 * Base controller
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceBaseCommerceController extends BaseController
{

	private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private static final Logger LOG = LoggerFactory.getLogger(BaseCommerceController.class);

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "i18nService")
	private I18NService i18Service;

	/**
	 * Logs the JSON object.
	 *
	 * @param object
	 *           JSON object to be logged.
	 * @param infoLogString
	 *           the info log string
	 *
	 * @param errorLogString
	 *           the error log string
	 */
	protected void logJsonObject(final Object object, final String infoLogString, final String errorLogString)
	{
		try
		{
			final String objectJson = OBJECT_WRITER.writeValueAsString(object);
			LOG.info(infoLogString, objectJson);
		}
		catch (final JsonProcessingException jPE)
		{
			LOG.error(errorLogString, jPE);
		}
	}

	/**
	 * Adds error message from the validation error object
	 *
	 * @param articleNumber
	 *
	 * @param errorEntries
	 *           to be updated
	 * @param errors
	 *           error object
	 */
	protected void addError(final String articleNumber, final List<ProductErrorWsDTO> errorEntries, final Errors errors)
	{
		final List<String> errorMsgList = new ArrayList<>();
		errors.getAllErrors().forEach(error -> errorMsgList
				.add(messageSource.getMessage(error.getCodes()[0], error.getArguments(), i18Service.getCurrentLocale())));
		addError(articleNumber, errorEntries, errorMsgList);
	}

	/**
	 * Adds error message in response DTO
	 *
	 * @param articleNumber
	 *
	 * @param errorEntries
	 *           to be update
	 * @param messages
	 *           list of error messages
	 */
	protected void addError(final String articleNumber, final List<ProductErrorWsDTO> errorEntries, final List<String> messages)
	{
		final ProductErrorWsDTO responseError = new ProductErrorWsDTO();
		responseError.setArticleNumber(articleNumber);
		responseError.setLogType("E");
		responseError.setMessages(messages);
		errorEntries.add(responseError);
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	protected I18NService getI18Service()
	{
		return i18Service;
	}
}
