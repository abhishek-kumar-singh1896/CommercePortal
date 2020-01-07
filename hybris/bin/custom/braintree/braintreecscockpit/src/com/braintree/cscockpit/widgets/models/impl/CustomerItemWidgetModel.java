package com.braintree.cscockpit.widgets.models.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;

import org.springframework.util.ObjectUtils;

import com.braintree.model.BraintreeCustomerDetailsModel;


public class CustomerItemWidgetModel extends DefaultMasterDetailListWidgetModel<TypedObject>
{
	private TypedObject customer;

	public CustomerItemWidgetModel()
	{
	}

	public TypedObject getCustomer()
	{
		return customer;
	}

	private BraintreeCustomerDetailsModel getCustomer(final TypedObject customer)
	{
		return customer.getObject() instanceof BraintreeCustomerDetailsModel ? (BraintreeCustomerDetailsModel) customer.getObject()
				: null;
	}

	public boolean setCustomer(final TypedObject customer)
	{
		boolean changed = false;
		if (!ObjectUtils.nullSafeEquals(this.customer, customer))
		{
			changed = true;
			this.customer = customer;
		}

		return changed;
	}

}
