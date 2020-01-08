package com.braintree.cscockpit.widgets.controllers.impl;


import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCallContextDependantController;
import de.hybris.platform.cscockpit.widgets.events.CsCockpitEvent;

import java.util.Map;

import com.braintree.cscockpit.widgets.controllers.CustomerController;


public class DefaultCustomerController extends AbstractCallContextDependantController implements CustomerController
{
	private BraintreeDefaultCallContextController callContextController;

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		getCallContextController().setImpersonationContextChanged();
		this.dispatchEvent("csCtx", new CsCockpitEvent(source, context, data));
	}

	@Override
	public TypedObject getCurrentCustomer()
	{
		return getCallContextController().getCurrentBTCustomer();
	}

	@Override
	public void setCurrentCustomer(final TypedObject newCustomer)
	{

		getCallContextController().setCurrentBTCustomer(newCustomer);
	}

	@Override
	public TypedObject getCurrentPaymentMethod()
	{
		return getCallContextController().getCurrentPaymentMethod();
	}

	@Override
	public void setCurrentPaymentMethod(final TypedObject newCustomer)
	{
		getCallContextController().setCurrentPaymentMethod(newCustomer);
	}

	@Override
	public BraintreeDefaultCallContextController getCallContextController()
	{
		return callContextController;
	}

	public void setCallContextController(final BraintreeDefaultCallContextController callContextController)
	{
		this.callContextController = callContextController;
	}

	@Override
	public TypedObject getCurrentAddress()
	{
		return getCallContextController().getCurrentAddress();
	}

	@Override
	public void setCurrentAddress(final TypedObject address)
	{
		getCallContextController().setCurrentAddress(address);
	}
}
