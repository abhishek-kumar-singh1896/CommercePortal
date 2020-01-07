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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;

import com.enterprisewide.b2badvance.outboundintegration.constants.B2badvanceoutboundintegrationConstants;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceLogEntriesDTO;
import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderRequestDTO;
import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceCreateOrderRequest;
import com.enterprisewide.b2badvance.outboundintegration.response.B2BAdvanceBaseResponse;
import com.enterprisewide.b2badvance.outboundintegration.response.B2BAdvanceCreateOrderResponse;
import com.enterprisewide.b2badvance.outboundintegration.services.B2BAdvanceOrderOutboundIntegrationService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 *
 */
public class B2BAdvanceOrderOutboundIntegrationServiceImpl extends B2BAdvanceBaseOutboundIntegrationServiceImpl
		implements B2BAdvanceOrderOutboundIntegrationService {

	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceOrderOutboundIntegrationServiceImpl.class);

	private static final String ORDER_CREATE_URL = "sap.order.create.url";

	private Converter<OrderModel, B2BAdvanceOrderRequestDTO> orderRequestDTOConverter;

	private Converter<B2BAdvanceOrderRequestDTO, B2BAdvanceCreateOrderRequest> createOrderConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createOrder(final OrderModel orderModel) throws B2BAdvanceWebServiceFailureException {
		boolean createStatus = false;
		final Configuration configuration = getConfiguration();
		final B2BAdvanceOrderRequestDTO orderDTO = getOrderRequestDTOConverter().convert(orderModel);
		final B2BAdvanceCreateOrderRequest fsOrderCreateRequest = getCreateOrderConverter().convert(orderDTO);
		B2BAdvanceCreateOrderResponse fsOrderCreateResponse = null;
		try {
			final String plainCredentials = new StringBuilder(30)
					.append(getConfiguration().getString(B2badvanceoutboundintegrationConstants.SAP_USERNAME))
					.append(':')
					.append(getConfiguration().getString(B2badvanceoutboundintegrationConstants.SAP_PASSWORD))
					.toString();
			fsOrderCreateResponse = this.<B2BAdvanceCreateOrderRequest, B2BAdvanceCreateOrderResponse>sendSAPRequest(
					configuration.getString(B2badvanceoutboundintegrationConstants.SAP_ROOT_URL)
							.concat(configuration.getString(ORDER_CREATE_URL)),
					HttpMethod.POST, fsOrderCreateRequest, B2BAdvanceCreateOrderResponse.class, plainCredentials);
			isRequestSuccessful(fsOrderCreateResponse);

			createStatus = true;
			// orderModel.setSapOrderNumber(fsOrderCreateResponse.getErpOrderID());
		} catch (final B2BAdvanceWebServiceFailureException fsWSFEx) {
			validateAndLogError(fsOrderCreateResponse);
			throw fsWSFEx;
		}

		return createStatus;
	}

	private void validateAndLogError(final B2BAdvanceBaseResponse fsOrderCreateResponse) {
		if (fsOrderCreateResponse != null && fsOrderCreateResponse.getHeader() != null
				&& CollectionUtils.isNotEmpty(fsOrderCreateResponse.getHeader().getLogEntries())) {
			LOG.error("######### ERROR in Order create/update opearion ###########");
			for (final B2BAdvanceLogEntriesDTO logEntry : fsOrderCreateResponse.getHeader().getLogEntries()) {
				if ("E".equals(logEntry.getLogType())) {
					LOG.error(logEntry.getMessage());
				}
			}
		}
	}

	protected Converter<OrderModel, B2BAdvanceOrderRequestDTO> getOrderRequestDTOConverter() {
		return orderRequestDTOConverter;
	}

	@Required
	public void setOrderRequestDTOConverter(
			final Converter<OrderModel, B2BAdvanceOrderRequestDTO> orderRequestDTOConverter) {
		this.orderRequestDTOConverter = orderRequestDTOConverter;
	}

	protected Converter<B2BAdvanceOrderRequestDTO, B2BAdvanceCreateOrderRequest> getCreateOrderConverter() {
		return createOrderConverter;
	}

	@Required
	public void setCreateOrderConverter(
			final Converter<B2BAdvanceOrderRequestDTO, B2BAdvanceCreateOrderRequest> createOrderConverter) {
		this.createOrderConverter = createOrderConverter;
	}
}
