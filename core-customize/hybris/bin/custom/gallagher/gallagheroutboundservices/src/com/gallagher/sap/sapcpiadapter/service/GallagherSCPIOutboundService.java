package com.gallagher.sap.sapcpiadapter.service;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.sapcpiadapter.service.SapCpiOutboundService;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import rx.Observable;


/**
 *
 * Service to provide SCPI operations for customer
 *
 * @author Vikram Bishnoi
 */
public interface GallagherSCPIOutboundService extends SapCpiOutboundService
{
	/**
	 * Sends customer to SCPI
	 *
	 * @param customer
	 *           to be sent
	 * @return response map
	 */
	Observable<ResponseEntity<Map>> sendCustomer(final CustomerModel customer);
}
