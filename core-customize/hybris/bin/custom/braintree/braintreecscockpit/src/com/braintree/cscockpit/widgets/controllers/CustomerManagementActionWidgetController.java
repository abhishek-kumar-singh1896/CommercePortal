package com.braintree.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;


public interface CustomerManagementActionWidgetController extends WidgetController
{
	boolean isEditPossible();

	boolean isAddPaymentTokenPossible();

	boolean isAddAddressPossible();

	boolean isDeletePossible();

	boolean isRefreshPossible();

	TypedObject getBtCustomer();

	void setBtCustomer(TypedObject customer);

	void removeBTCustomer();
}
