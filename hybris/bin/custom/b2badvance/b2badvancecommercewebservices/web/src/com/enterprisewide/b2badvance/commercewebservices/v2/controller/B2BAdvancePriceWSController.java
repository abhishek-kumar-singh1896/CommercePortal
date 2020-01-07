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
import com.enterprisewide.b2badvance.commercewebservices.price.dto.ProductDiscountWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.price.dto.ProductPriceWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.price.request.ProductDiscountReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.price.request.ProductPriceReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.price.response.PDTResponseWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.ProductErrorWsDTO;
import com.enterprisewide.b2badvance.facades.price.data.B2BAdvanceDiscountData;
import com.enterprisewide.b2badvance.facades.price.data.B2BAdvancePriceData;
import com.enterprisewide.b2badvance.facades.product.B2BAdvanceProductFacade;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller for prices related requests such as creating updating products prices and discounts(for base/variants)
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/prices")
public class B2BAdvancePriceWSController extends B2BAdvanceBaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvancePriceWSController.class);
	private static final String ERROR_RESPONSE_HEADER_CODE = "500";
	private static final String SUCCESS_RESPONSE_HEADER_CODE = "200";
	private static final String REJECTED_RESPONSE_HEADER_CODE = "406";

	@Resource(name = "priceDTOValidator")
	private Validator priceDTOValidator;

	@Resource(name = "discountDTOValidator")
	private Validator discountDTOValidator;

	@Resource(name = "productVariantFacade")
	private B2BAdvanceProductFacade productFacade;

	/**
	 * Handles product price requests. Accepts a list of prices and validates each price entity. This API saves all the
	 * valid prices into the system and sends error response for all other invalid price entities
	 *
	 * @param priceRequest
	 *           contains the list of prices
	 * @return response with detailed error messages for price list
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/createUpdatePrices", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdatePrices", value = " Creates/Updates Prices")
	@ApiBaseSiteIdParam
	public PDTResponseWsDTO createUpdatePrices(@ApiParam(value = "Object containing price list.", required = true)
	@RequestBody
	final ProductPriceReqestWsDTO priceRequest)
	{
		final WSRequestHeaderDTO requestHeader = priceRequest.getHeader();
		/* Logging */
		LOG.info("Price update request has been generated on the system [{}] by the user [{}] at [{}]", requestHeader.getSystemID(),
				requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Price update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(priceRequest, infoLogString, "JSON processing error occurred for request");
		final PDTResponseWsDTO priceResponse = new PDTResponseWsDTO();
		priceResponse.setHeader(new WSResponseHeaderDTO());
		final List<ProductErrorWsDTO> errorEntries = new ArrayList<>();
		if (priceRequest == null || CollectionUtils.isEmpty(priceRequest.getPrices()))
		{
			priceResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addError(null, errorEntries, Collections.singletonList("No price available in request"));
		}
		else
		{
			priceResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvancePriceData> priceDataList = new ArrayList<>(priceRequest.getPrices().size());
			/* Validate and convert prices for facade layer */
			priceRequest.getPrices().forEach(price -> processPrices(price, priceDataList, errorEntries));
			/* Saves prices and updates error list with all the errors occurred during save */
			savePrices(errorEntries, priceDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			priceResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Price update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(priceResponse, infoLogString, "JSON processing error occurred for response");
		return priceResponse;
	}

	/**
	 * Validate and convert prices for facade layer
	 *
	 * @param price
	 *           to be validated
	 * @param priceDataList
	 *           to hold the priceData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processPrices(final ProductPriceWsDTO price, final List<B2BAdvancePriceData> priceDataList,
			final List<ProductErrorWsDTO> errorEntries)
	{
		try
		{

			validate(price, "price", priceDTOValidator);
			priceDataList.add(getDataMapper().map(price, B2BAdvancePriceData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addError(price.getArticleNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves prices
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param baseProducts
	 *           to be saved
	 */
	private void savePrices(final List<ProductErrorWsDTO> errorEntries, final List<B2BAdvancePriceData> prices)
	{
		if (CollectionUtils.isNotEmpty(prices))
		{
			final Map<String, String> errorMap = productFacade.savePrices(prices);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}

	/**
	 * Handles product discount requests. Accepts a list of discounts and validates each discount entity. This API saves
	 * all the valid discounts into the system and sends error response for all other invalid discount entities
	 *
	 * @param discountRequest
	 *           contains the list of discounts
	 * @return response with detailed error messages for discount list
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/createUpdateDiscounts", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdateDiscounts", value = " Creates/Updates Discounts")
	@ApiBaseSiteIdParam
	public PDTResponseWsDTO createUpdateDiscounts(@ApiParam(value = "Object containing discount list.", required = true)
	@RequestBody
	final ProductDiscountReqestWsDTO discountRequest)
	{
		final WSRequestHeaderDTO requestHeader = discountRequest.getHeader();
		/* Logging */
		LOG.info("Discount update request has been generated on the system [{}] by the user [{}] at [{}]",
				requestHeader.getSystemID(), requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Discount update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(discountRequest, infoLogString, "JSON processing error occurred for request");
		final PDTResponseWsDTO discountResponse = new PDTResponseWsDTO();
		discountResponse.setHeader(new WSResponseHeaderDTO());
		final List<ProductErrorWsDTO> errorEntries = new ArrayList<>();
		if (discountRequest == null || CollectionUtils.isEmpty(discountRequest.getDiscounts()))
		{
			discountResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addError(null, errorEntries, Collections.singletonList("No discount available in request"));
		}
		else
		{
			discountResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvanceDiscountData> discountDataList = new ArrayList<>(discountRequest.getDiscounts().size());
			/* Validate and convert discounts for facade layer */
			discountRequest.getDiscounts().forEach(discount -> processDiscounts(discount, discountDataList, errorEntries));
			/* Saves discounts and updates error list with all the errors occurred during save */
			saveDiscounts(errorEntries, discountDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			discountResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Discount update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(discountResponse, infoLogString, "JSON processing error occurred for response");
		return discountResponse;
	}

	/**
	 * Validate and convert discounts for facade layer
	 *
	 * @param discount
	 *           to be validated
	 * @param discountDataList
	 *           to hold the discountData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processDiscounts(final ProductDiscountWsDTO discount, final List<B2BAdvanceDiscountData> discountDataList,
			final List<ProductErrorWsDTO> errorEntries)
	{
		try
		{

			validate(discount, "price", discountDTOValidator);
			discountDataList.add(getDataMapper().map(discount, B2BAdvanceDiscountData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addError(discount.getArticleNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves discounts
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param baseProducts
	 *           to be saved
	 */
	private void saveDiscounts(final List<ProductErrorWsDTO> errorEntries, final List<B2BAdvanceDiscountData> discounts)
	{
		if (CollectionUtils.isNotEmpty(discounts))
		{
			final Map<String, String> errorMap = productFacade.saveDiscounts(discounts);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}
}
