package com.braintree.cscockpit.widgets.adapters;


import de.hybris.platform.cscockpit.widgets.adapters.CheckoutPaymentWidgetAdapter;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;

import com.braintree.cscockpit.widgets.controllers.BraintreeCheckoutController;


public class BraintreeCheckoutPaymentWidgetAdapter extends CheckoutPaymentWidgetAdapter
{
	protected boolean updateModel()
	{
		((DefaultMasterDetailListWidgetModel) this.getWidgetModel()).setItems(((BraintreeCheckoutController) this
				.getWidgetController()).getBraintreePaymentInfos());
		return true;
	}
}
