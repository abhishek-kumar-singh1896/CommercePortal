/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvance.commercewebservices.v2.controller;

import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enterprisewide.b2badvance.commercewebservices.dto.WSRequestHeaderDTO;
import com.enterprisewide.b2badvance.commercewebservices.dto.WSResponseHeaderDTO;
import com.enterprisewide.b2badvance.commercewebservices.invoice.dto.InvoiceErrorWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.invoice.dto.InvoiceWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.invoice.request.InvoiceReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.invoice.response.InvoiceResponseWsDTO;
import com.enterprisewide.b2badvance.facades.invoice.B2BAdvanceInvoiceFacade;
import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller for invoices related requests such as creating updating invoices
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/invoices")
public class B2BAdvanceInvoiceWSController extends B2BAdvanceBaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceInvoiceWSController.class);
	private static final String ERROR_RESPONSE_HEADER_CODE = "500";
	private static final String SUCCESS_RESPONSE_HEADER_CODE = "200";
	private static final String REJECTED_RESPONSE_HEADER_CODE = "406";

	@Resource(name = "invoiceDTOValidator")
	private Validator invoiceDTOValidator;

	@Resource(name = "b2bAdvanceInvoiceFacade")
	private B2BAdvanceInvoiceFacade invoiceFacade;

	/**
	 * Handles invoice create/update requests. Accepts a list of invoices and validates each invoice. This API saves all
	 * the valid invoices into the system and sends error response for all other invalid invoices
	 *
	 * @param invoiceRequest
	 *           contains the list of invoices
	 * @return response with detailed error messages for invalid invoices
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/create-update", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdateInvoices", value = " Creates/Updates Invoices")
	@ApiBaseSiteIdParam
	public InvoiceResponseWsDTO createInvoices(@ApiParam(value = "Object containing Invoices list.", required = true)
	@RequestBody
	final InvoiceReqestWsDTO invoiceRequest)
	{
		final WSRequestHeaderDTO requestHeader = invoiceRequest.getHeader();
		/* Logging */
		LOG.info("Invoice update request has been generated on the system [{}] by the user [{}] at [{}]",
				requestHeader.getSystemID(), requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Invoice update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(invoiceRequest, infoLogString, "JSON processing error occurred for request");
		final InvoiceResponseWsDTO invoiceResponse = new InvoiceResponseWsDTO();
		invoiceResponse.setHeader(new WSResponseHeaderDTO());
		final List<InvoiceErrorWsDTO> errorEntries = new ArrayList<>();
		if (invoiceRequest == null || CollectionUtils.isEmpty(invoiceRequest.getInvoices()))
		{
			invoiceResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addInvoiceError(null, errorEntries, Collections.singletonList("No invoice available in request"));
		}
		else
		{
			invoiceResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<InvoiceData> invoiceDataList = new ArrayList<InvoiceData>(invoiceRequest.getInvoices().size());
			/* Validate and convert invoices for facade layer */
			invoiceRequest.getInvoices().forEach(invoice -> processInvoice(invoice, invoiceDataList, errorEntries));
			/* Saves invoices and updates error list with all the errors occurred during save */
			saveInvoices(errorEntries, invoiceDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			invoiceResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Invoice update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(invoiceResponse, infoLogString, "JSON processing error occurred for response");
		return invoiceResponse;
	}

	/**
	 * Validate and convert invoices for facade layer
	 *
	 * @param invoice
	 *           to be validated
	 * @param invoiceDataList
	 *           to hold the invoicesData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processInvoice(final InvoiceWsDTO invoice, final List<InvoiceData> invoiceDataList,
			final List<InvoiceErrorWsDTO> errorEntries)
	{
		try
		{

			validate(invoice, "invoice", invoiceDTOValidator);
			invoiceDataList.add(getDataMapper().map(invoice, InvoiceData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addInvoiceError(invoice.getInvoiceNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves invoices
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param invoiceDataList
	 *           to be saved
	 */
	private void saveInvoices(final List<InvoiceErrorWsDTO> errorEntries, final List<InvoiceData> invoiceDataList)
	{
		if (CollectionUtils.isNotEmpty(invoiceDataList))
		{
			final Map<String, String> errorMap = invoiceFacade.saveInvoices(invoiceDataList);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addInvoiceError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}

	/**
	 * Adds error message from the validation error object
	 *
	 * @param invoiceNumber
	 *
	 * @param errorEntries
	 *           to be updated
	 * @param errors
	 *           error object
	 */
	private void addInvoiceError(final String invoiceNumber, final List<InvoiceErrorWsDTO> errorEntries, final Errors errors)
	{
		final List<String> errorMsgList = new ArrayList<>();
		errors.getAllErrors().forEach(error -> errorMsgList
				.add(getMessageSource().getMessage(error.getCodes()[0], error.getArguments(), getI18Service().getCurrentLocale())));
		addInvoiceError(invoiceNumber, errorEntries, errorMsgList);
	}

	/**
	 * Adds error message in response DTO
	 *
	 * @param invoiceNumber
	 *
	 * @param errorEntries
	 *           to be update
	 * @param messages
	 *           list of error messages
	 */
	private void addInvoiceError(final String invoiceNumber, final List<InvoiceErrorWsDTO> errorEntries,
			final List<String> messages)
	{
		final InvoiceErrorWsDTO responseError = new InvoiceErrorWsDTO();
		responseError.setInvoiceNumber(invoiceNumber);
		responseError.setLogType("E");
		responseError.setMessages(messages);
		errorEntries.add(responseError);
	}
}
