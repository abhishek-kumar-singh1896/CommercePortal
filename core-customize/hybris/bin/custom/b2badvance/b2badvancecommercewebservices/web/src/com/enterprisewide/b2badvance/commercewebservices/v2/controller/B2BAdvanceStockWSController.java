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
import com.enterprisewide.b2badvance.commercewebservices.product.request.ProductStockReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.ProductErrorWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.response.ProductResponseWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.product.dto.ProductStockWsDTO;
import com.enterprisewide.b2badvance.facades.product.B2BAdvanceProductFacade;
import com.enterprisewide.b2badvance.facades.stock.data.B2BAdvanceStockData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller for stocks related requests such as creating updating products stocks and discounts(for base/variants)
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/stocks")
public class B2BAdvanceStockWSController extends B2BAdvanceBaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceStockWSController.class);
	private static final String ERROR_RESPONSE_HEADER_CODE = "500";
	private static final String SUCCESS_RESPONSE_HEADER_CODE = "200";
	private static final String REJECTED_RESPONSE_HEADER_CODE = "406";

	@Resource(name = "stockDTOValidator")
	private Validator stockDTOValidator;

	@Resource(name = "productVariantFacade")
	private B2BAdvanceProductFacade productFacade;

	/**
	 * Handles product stock requests. Accepts a list of stocks and validates each stock entity. This API saves all the
	 * valid stocks into the system and sends error response for all other invalid stock entities
	 *
	 * @param stockRequest
	 *           contains the list of stocks
	 * @return response with detailed error messages for stock list
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/createUpdateStocks", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "createUpdateStocks", value = " Creates/Updates Stocks")
	@ApiBaseSiteIdParam
	public ProductResponseWsDTO createUpdateStocks(@ApiParam(value = "Object containing stock list.", required = true)
	@RequestBody
	final ProductStockReqestWsDTO stockRequest)
	{
		final WSRequestHeaderDTO requestHeader = stockRequest.getHeader();
		/* Logging */
		LOG.info("Stock update request has been generated on the system [{}] by the user [{}] at [{}]", requestHeader.getSystemID(),
				requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Stock update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(stockRequest, infoLogString, "JSON processing error occurred for request");
		final ProductResponseWsDTO stockResponse = new ProductResponseWsDTO();
		stockResponse.setHeader(new WSResponseHeaderDTO());
		final List<ProductErrorWsDTO> errorEntries = new ArrayList<>();
		if (stockRequest == null || CollectionUtils.isEmpty(stockRequest.getStocks()))
		{
			stockResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addError(null, errorEntries, Collections.singletonList("No stock available in request"));
		}
		else
		{
			stockResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvanceStockData> stockDataList = new ArrayList<>(stockRequest.getStocks().size());
			/* Validate and convert stocks for facade layer */
			stockRequest.getStocks().forEach(stock -> processStocks(stock, stockDataList, errorEntries));
			/* Saves stocks and updates error list with all the errors occurred during save */
			saveStocks(errorEntries, stockDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			stockResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Stock update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(stockResponse, infoLogString, "JSON processing error occurred for response");
		return stockResponse;
	}

	/**
	 * Validate and convert stocks for facade layer
	 *
	 * @param stock
	 *           to be validated
	 * @param stockDataList
	 *           to hold the stockData converted from WS DTO
	 * @param errorEntries
	 *           to hold all the validation errors
	 */
	private void processStocks(final ProductStockWsDTO stock, final List<B2BAdvanceStockData> stockDataList,
			final List<ProductErrorWsDTO> errorEntries)
	{
		try
		{

			validate(stock, "stock", stockDTOValidator);
			stockDataList.add(getDataMapper().map(stock, B2BAdvanceStockData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addError(stock.getArticleNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves stocks
	 *
	 * @param errorEntries
	 *           to hold all the processing errors
	 * @param baseProducts
	 *           to be saved
	 */
	private void saveStocks(final List<ProductErrorWsDTO> errorEntries, final List<B2BAdvanceStockData> stocks)
	{
		if (CollectionUtils.isNotEmpty(stocks))
		{
			final Map<String, String> errorMap = productFacade.saveStocks(stocks);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}
}
