package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;

import java.util.List;


public interface BraintreeCallContextController extends CallContextController
{

	TypedObject getCurrentTransaction();

	boolean setCurrentTransaction(TypedObject var1);

	TypedObject getCurrentBTCustomer();

	boolean setCurrentBTCustomer(TypedObject var1);

	TypedObject getCurrentPaymentMethod();

	boolean setCurrentPaymentMethod(TypedObject var1);

	TypedObject getCurrentAddress();

	boolean setCurrentAddress(TypedObject var1);

	List<TypedObject> getBrainTreePaymentInfos();

}
