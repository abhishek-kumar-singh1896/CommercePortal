/**
 *
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
import com.enterprisewide.b2badvance.commercewebservices.order.dto.OrderErrorWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.order.dto.OrdersWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.order.request.OrderReqestWsDTO;
import com.enterprisewide.b2badvance.commercewebservices.order.response.OrderResponseWsDTO;
import com.enterprisewide.b2badvance.facades.order.B2BAdvanceOrderDetailsFacade;
import com.enterprisewide.b2badvance.facades.order.data.B2BAdvanceOrderData;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/orders")
public class B2BAdvanceOrderWSController extends B2BAdvanceBaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceOrderWSController.class);
	private static final String ERROR_RESPONSE_HEADER_CODE = "500";
	private static final String SUCCESS_RESPONSE_HEADER_CODE = "200";
	private static final String REJECTED_RESPONSE_HEADER_CODE = "406";

	@Resource(name = "orderDTOValidator")
	private Validator orderDTOValidator;

	@Resource(name = "b2badvanceOrderDetailsFacade")
	private B2BAdvanceOrderDetailsFacade b2badvanceOrderDetailsFacade;


	/**
	 * Handles order create/update requests. Accepts a list of orders and validates each order entity. This API saves all
	 * the valid order into the system and sends error response for all other invalid order entities
	 *
	 * @param orderRequest
	 *                        contains the list of orders
	 * @return response with detailed error messages for order list
	 */
	@Secured("IS_AUTHENTICATED_FULLY")
	@RequestMapping(value = "/update", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	@ApiOperation(nickname = "UpdateOrders", value = "Updates Orders")
	@ApiBaseSiteIdParam
	public OrderResponseWsDTO createUpdateOrders(
			@ApiParam(value = "Object containing order list.", required = true) @RequestBody final OrderReqestWsDTO orderRequest)
	{
		final WSRequestHeaderDTO requestHeader = orderRequest.getHeader();
		/* Logging */
		LOG.info("Order update request has been generated on the system [{}] by the user [{}] at [{}]", requestHeader.getSystemID(),
				requestHeader.getUserID(), requestHeader.getTimestamp());
		final String transactionId = RandomStringUtils.random(16, true, true);
		String infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" REQUEST ")
				.append("Order update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(orderRequest, infoLogString, "JSON processing error occurred for request");
		final OrderResponseWsDTO orderResponse = new OrderResponseWsDTO();
		orderResponse.setHeader(new WSResponseHeaderDTO());
		final List<OrderErrorWsDTO> errorEntries = new ArrayList<>();
		if (orderRequest == null || CollectionUtils.isEmpty(orderRequest.getOrders()))
		{
			orderResponse.getHeader().setResponseCode(REJECTED_RESPONSE_HEADER_CODE);
			addOrderError(null, errorEntries, Collections.singletonList("No orders available in request"));
		}
		else
		{
			orderResponse.getHeader().setResponseCode(SUCCESS_RESPONSE_HEADER_CODE);
			final List<B2BAdvanceOrderData> orderDataList = new ArrayList<>(orderRequest.getOrders().size());
			/* Validate and convert stocks for facade layer */
			orderRequest.getOrders().forEach(order -> processOrders(order, orderDataList, errorEntries));
			/* Saves stocks and updates error list with all the errors occurred during save */
			saveOrders(errorEntries, orderDataList);
		}
		if (CollectionUtils.isNotEmpty(errorEntries))
		{
			orderResponse.setErrorEntries(errorEntries);
		}
		/* Logging */
		infoLogString = new StringBuilder(150).append("\nTRANSACTION ID:-").append(transactionId).append(" RESPONSE ")
				.append("Order update WS ").append(' ').append(":-\n\n{}").toString();
		logJsonObject(orderResponse, infoLogString, "JSON processing error occurred for response");
		return orderResponse;
	}

	/**
	 * Validate and convert orders for facade layer
	 *
	 * @param order
	 *                         to be validated
	 * @param orderDataList
	 *                         to hold the orderData converted from WS DTO
	 * @param errorEntries
	 *                         to hold all the validation errors
	 */
	private void processOrders(final OrdersWsDTO order, final List<B2BAdvanceOrderData> orderDataList,
			final List<OrderErrorWsDTO> errorEntries)
	{
		try
		{

			validate(order, "order", orderDTOValidator);
			orderDataList.add(getDataMapper().map(order, B2BAdvanceOrderData.class));
		}
		catch (final WebserviceValidationException wsVEx)
		{
			addOrderError(order.getHybrisOrderNumber(), errorEntries, ((Errors) wsVEx.getValidationObject()));
		}
	}

	/**
	 * Saves orders
	 *
	 * @param errorEntries
	 *                        to hold all the processing errors
	 * @param Invoices
	 *                        to be saved
	 */
	private void saveOrders(final List<OrderErrorWsDTO> errorEntries, final List<B2BAdvanceOrderData> orders)
	{
		if (CollectionUtils.isNotEmpty(orders))
		{
			final Map<String, String> errorMap = b2badvanceOrderDetailsFacade.saveOrders(orders);
			if (MapUtils.isNotEmpty(errorMap))
			{
				errorMap.forEach((k, v) -> addOrderError(k, errorEntries, Collections.singletonList(v)));
			}
		}
	}

	/**
	 * @param k
	 * @param errorEntries
	 *                         to be updated
	 * @param singletonList
	 * @return
	 */
	private void addOrderError(final String hybrisOrderNumber, final List<OrderErrorWsDTO> errorEntries, final Errors errors)
	{
		final List<String> errorMsgList = new ArrayList<>();
		errors.getAllErrors().forEach(error -> errorMsgList
				.add(getMessageSource().getMessage(error.getCodes()[0], error.getArguments(), getI18Service().getCurrentLocale())));
		addOrderError(hybrisOrderNumber, errorEntries, errorMsgList);

	}

	/**
	 * Adds error message in response DTO
	 *
	 * @param invoiceNumber
	 *
	 * @param errorEntries
	 *                         to be update
	 * @param messages
	 *                         list of error messages
	 */
	private void addOrderError(final String hybrisOrderNumber, final List<OrderErrorWsDTO> errorEntries, final List<String> messages)
	{
		final OrderErrorWsDTO responseError = new OrderErrorWsDTO();
		responseError.setHybrisOrderNumber(hybrisOrderNumber);
		responseError.setLogType("E");
		responseError.setMessages(messages);
		errorEntries.add(responseError);
	}
}
