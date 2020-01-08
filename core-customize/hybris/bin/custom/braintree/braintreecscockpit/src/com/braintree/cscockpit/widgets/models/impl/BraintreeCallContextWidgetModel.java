package com.braintree.cscockpit.widgets.models.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.models.impl.CallContextWidgetModel;


public class BraintreeCallContextWidgetModel extends CallContextWidgetModel
{
	private TypedObject transaction;
	private TypedObject btCustomer;

	public TypedObject getTransaction()
	{
		return this.transaction;
	}

	public boolean setTransaction(final TypedObject transaction)
	{
		final boolean changed = this.transaction == null || transaction == null || !this.transaction.equals(transaction);
		this.transaction = transaction;
		return changed;
	}

	public TypedObject getBtCustomer()
	{
		return this.btCustomer;
	}

	public boolean setBtCustomer(final TypedObject btCustomer)
	{
		final boolean changed = this.btCustomer == null || btCustomer == null || !this.btCustomer.equals(btCustomer);
		this.btCustomer = btCustomer;
		return changed;
	}
}
