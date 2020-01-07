package com.braintree.cscockpit.widgets.controllers.impl;


import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCallContextDependantController;
import de.hybris.platform.cscockpit.widgets.events.CsCockpitEvent;

import java.util.Map;

import com.braintree.cscockpit.widgets.controllers.TransactionController;


public class DefaultTransactionController extends AbstractCallContextDependantController implements TransactionController
{
	private BraintreeDefaultCallContextController callContextController;

	@Override
	public void dispatchEvent(final String context, final Object source, final Map<String, Object> data)
	{
		getCallContextController().setImpersonationContextChanged();
		this.dispatchEvent("csCtx", new CsCockpitEvent(source, context, data));
	}

	@Override
	public TypedObject getCurrentTransaction()
	{
		return getCallContextController().getCurrentTransaction();
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
}
