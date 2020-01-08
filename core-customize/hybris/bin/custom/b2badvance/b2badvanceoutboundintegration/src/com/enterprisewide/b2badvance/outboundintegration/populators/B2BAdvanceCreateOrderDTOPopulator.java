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
package com.enterprisewide.b2badvance.outboundintegration.populators;

import org.springframework.util.Assert;

import com.enterprisewide.b2badvance.outboundintegration.dtos.B2BAdvanceOrderRequestDTO;
import com.enterprisewide.b2badvance.outboundintegration.request.B2BAdvanceCreateOrderRequest;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Populator to convert B2BAdvanceOrderRequestDTO to
 * B2BAdvanceCreateOrderRequest
 */
public class B2BAdvanceCreateOrderDTOPopulator
		implements Populator<B2BAdvanceOrderRequestDTO, B2BAdvanceCreateOrderRequest> {

	@Override
	public void populate(final B2BAdvanceOrderRequestDTO source, final B2BAdvanceCreateOrderRequest target)
			throws ConversionException {
		Assert.notNull(source, "B2BAdvanceOrderRequestDTO source cannot be null.");
		Assert.notNull(target, "B2BAdvanceCreateOrderRequest target cannot be null.");
		target.setOrderRequest(source);

	}

}
