/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.gallagher.sap.sapcpiadapter.service;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.sapcpiadapter.service.SapCpiOutboundService;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import rx.Observable;


/**
 * SapCpiOutboundService
 */
public interface GallagherSCPIOutboundService extends SapCpiOutboundService
{

	Observable<ResponseEntity<Map>> sendCustomer(final CustomerModel customer);

}
