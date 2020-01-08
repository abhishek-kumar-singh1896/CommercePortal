package com.braintree.cscockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;

import java.util.Map;

import com.braintree.cscockpit.widgets.controllers.CustomerManagementActionWidgetController;


public class DefaultCustomerManagementActionWidgetControllerImpl extends AbstractCsWidgetController implements
		CustomerManagementActionWidgetController
{
	private BraintreeDefaultCallContextController callContextController;

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		callContextController.setImpersonationContextChanged();
		this.getCallContextController().dispatchEvent((String) null, source, data);
	}

	@Override
	public boolean isEditPossible()
	{
		return true;
	}

	@Override
	public boolean isAddPaymentTokenPossible()
	{
		return true;
	}

	@Override
	public boolean isDeletePossible()
	{
		return true;
	}

	@Override
	public boolean isRefreshPossible()
	{
		return getBtCustomer() != null;
	}

	@Override
	public boolean isAddAddressPossible()
	{
		return true;
	}

	@Override
	public TypedObject getBtCustomer()
	{
		return this.getCallContextController().getCurrentBTCustomer();
	}

	@Override
	public void setBtCustomer(final TypedObject customer)
	{
		this.getCallContextController().setCurrentBTCustomer(customer);
	}

	@Override
	public void removeBTCustomer()
	{
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(null);
		this.getCallContextController().setCurrentBTCustomer(itemTypedObject);
	}

	public BraintreeDefaultCallContextController getCallContextController()
	{
		return callContextController;
	}

	public void setCallContextController(final BraintreeDefaultCallContextController callContextController)
	{
		this.callContextController = callContextController;
	}
}
