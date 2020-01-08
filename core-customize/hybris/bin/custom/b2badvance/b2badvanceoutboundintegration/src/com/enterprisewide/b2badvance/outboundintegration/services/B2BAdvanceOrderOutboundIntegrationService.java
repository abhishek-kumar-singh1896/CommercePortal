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
package com.enterprisewide.b2badvance.outboundintegration.services;

import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;

import de.hybris.platform.core.model.order.OrderModel;

/**
 *
 */
public interface B2BAdvanceOrderOutboundIntegrationService {

	/**
	 * Sends Create Order Request to ERP.
	 *
	 * @param order Order Model
	 * @return true if create order request is sent successfully
	 */
	boolean createOrder(final OrderModel order) throws B2BAdvanceWebServiceFailureException;
}
