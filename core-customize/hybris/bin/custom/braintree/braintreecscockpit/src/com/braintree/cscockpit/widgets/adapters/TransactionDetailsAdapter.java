package com.braintree.cscockpit.widgets.adapters;

import de.hybris.platform.cscockpit.widgets.adapters.AbstractInitialisingWidgetAdapter;

import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;


public class TransactionDetailsAdapter extends
		AbstractInitialisingWidgetAdapter<TransactionItemWidgetModel, TransactionController>
{

	@Override
	protected boolean updateModel()
	{
		return this.getWidgetModel().setTransaction(this.getWidgetModel().getTransaction());
	}
}
