package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;


public interface CustomerController extends WidgetController
{
	TypedObject getCurrentCustomer();

	void setCurrentCustomer(TypedObject newCustomer);

	TypedObject getCurrentPaymentMethod();

	void setCurrentPaymentMethod(TypedObject newCustomer);

	TypedObject getCurrentAddress();

	void setCurrentAddress(TypedObject newCustomer);
}
