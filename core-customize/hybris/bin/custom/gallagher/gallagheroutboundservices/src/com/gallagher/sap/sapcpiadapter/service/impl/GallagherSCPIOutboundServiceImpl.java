package com.gallagher.sap.sapcpiadapter.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.sapcpiadapter.service.impl.SapCpiOutboundServiceImpl;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.gallagher.sap.sapcpiadapter.service.GallagherSCPIOutboundService;

import rx.Observable;


/**
 * Implementation of {@link GallagherSCPIOutboundService}
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOutboundServiceImpl extends SapCpiOutboundServiceImpl implements GallagherSCPIOutboundService
{

	// Customer Outbound
	private static final String OUTBOUND_CUSTOMER_OBJECT = "OutboundC4cCustomer";
	private static final String OUTBOUND_CUSTOMER_DESTINATION = "scpiCustomerDestination";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Observable<ResponseEntity<Map>> sendCustomer(final CustomerModel sapCpiOutboundCustomerModel)
	{
		return getOutboundServiceFacade().send(sapCpiOutboundCustomerModel, OUTBOUND_CUSTOMER_OBJECT,
				OUTBOUND_CUSTOMER_DESTINATION);
	}
}