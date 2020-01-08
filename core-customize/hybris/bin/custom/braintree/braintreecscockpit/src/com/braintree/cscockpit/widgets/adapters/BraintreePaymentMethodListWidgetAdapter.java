package com.braintree.cscockpit.widgets.adapters;


import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cscockpit.widgets.adapters.AbstractInitialisingWidgetAdapter;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;

import com.braintree.cscockpit.widgets.controllers.BraintreeCallContextController;


public class BraintreePaymentMethodListWidgetAdapter
		extends
			AbstractInitialisingWidgetAdapter<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController>
{
	private BraintreePaymentMethodListWidgetAdapter()
	{
	}

	@Override
	protected boolean updateModel()
	{
		boolean changed = false;
		changed |= ((DefaultMasterDetailListWidgetModel) this.getWidgetModel()).setItems(((BraintreeCallContextController) this
				.getWidgetController()).getBrainTreePaymentInfos());
		return changed;
	}
}
