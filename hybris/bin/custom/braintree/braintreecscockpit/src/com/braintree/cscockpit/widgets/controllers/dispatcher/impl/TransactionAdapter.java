package com.braintree.cscockpit.widgets.controllers.dispatcher.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.impl.AbstractItemAppenderEventDispatcher;

import com.braintree.cscockpit.widgets.controllers.BraintreeCallContextController;


public class TransactionAdapter extends AbstractItemAppenderEventDispatcher<BraintreeCallContextController, TypedObject>
{
	public TransactionAdapter()
	{
	}

	@Override
	protected boolean addItem(final TypedObject item, final long l)
	{
		return this.getWidgetController().setCurrentTransaction(item);
	}
}
