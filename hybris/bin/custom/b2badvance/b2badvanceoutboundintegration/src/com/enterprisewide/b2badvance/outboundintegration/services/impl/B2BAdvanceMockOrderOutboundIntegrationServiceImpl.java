/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.enterprisewide.b2badvance.outboundintegration.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderRequestDTO;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceCreateOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * Mock implementation for order create WS
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceMockOrderOutboundIntegrationServiceImpl extends B2BAdvanceOrderOutboundIntegrationServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceMockOrderOutboundIntegrationServiceImpl.class);

	private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createOrder(final OrderModel orderModel) {
		final B2BAdvanceOrderRequestDTO orderDTO = getOrderRequestDTOConverter().convert(orderModel);
		final B2BAdvanceCreateOrderRequest fsOrderCreateRequest = getCreateOrderConverter().convert(orderDTO);
		appendHeaderToRequest(fsOrderCreateRequest);
		logJsonObject(fsOrderCreateRequest);
		// orderModel.setSapOrderNumber(String.valueOf(System.currentTimeMillis()));
		return true;
	}

	private void logJsonObject(final Object object) {
		try {
			final String objectJson = OBJECT_WRITER.writeValueAsString(object);
			LOG.info(objectJson);
		} catch (final JsonProcessingException jPE) {
			LOG.error("Error in parsing the JSON object", jPE);
		}
	}
}
