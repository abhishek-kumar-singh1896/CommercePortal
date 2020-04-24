/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gallagher.sapcustomerb2c.outbound;

import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.task.RetryLaterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sap.hybris.sapcustomerb2c.outbound.CustomerPublishAction;


/**
 * Action class to publish the registered customer to the SCPI
 *
 * @author Vikram Bishnoi
 */
public class GallagherCustomerPublishAction extends CustomerPublishAction
{


	/**
	 * {@inheritDoc}. Overridden to avoid sapContactID generation
	 */
	@Override
	public Transition executeAction(final BusinessProcessModel businessProcessModel) throws RetryLaterException
	{

		// set the time stamp in the sap replication info field
		final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		((StoreFrontCustomerProcessModel) businessProcessModel).getCustomer()
				.setSapReplicationInfo("Sent to SCPI " + dateFormat.format(Calendar.getInstance().getTime()));
		modelService.save(((StoreFrontCustomerProcessModel) businessProcessModel).getCustomer());

		final BaseStoreModel store = ((StoreFrontCustomerProcessModel) businessProcessModel).getStore();

		// prepare sending data to Data Hub
		final String baseStoreUid = store != null ? store.getUid() : null;
		getSendCustomerToDataHub().sendCustomerData(((StoreFrontCustomerProcessModel) businessProcessModel).getCustomer(),
				baseStoreUid, getStoreSessionFacade().getCurrentLanguage().getIsocode());
		return Transition.OK;
	}

}
