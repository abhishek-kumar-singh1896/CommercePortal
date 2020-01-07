package com.braintree.cscockpit.widgets.adapters;

import de.hybris.platform.cscockpit.widgets.adapters.AbstractInitialisingWidgetAdapter;

import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;


public class CustomerDetailsAdapter extends AbstractInitialisingWidgetAdapter<CustomerItemWidgetModel, CustomerController>
{

	@Override
	protected boolean updateModel()
	{
		return this.getWidgetModel().setCustomer(this.getWidgetModel().getCustomer());
	}
}
