/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.outboundservices.decorator;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;

import org.springframework.http.HttpHeaders;


/**
 * Decorates the outbound client request. By modifying headers along the destinationModel.
 *
 * @author shishirkant
 */
public interface GallagherCsrfOutboundRequestDecorator
{
	/**
	 * Decorates an Outbound request
	 *
	 * @param httpHeaders
	 *           The headers to be used for the outgoing request.
	 * @param destinationModel
	 *           The ConsumedDestinationModel.
	 * @return An {@link HttpHeaders} containing the result of the decoration.
	 */
	HttpHeaders decorate(final HttpHeaders httpHeaders, final ConsumedDestinationModel destinationModel);
}
