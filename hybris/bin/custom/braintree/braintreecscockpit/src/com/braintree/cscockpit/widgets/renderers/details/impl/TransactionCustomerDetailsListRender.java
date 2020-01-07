package com.braintree.cscockpit.widgets.renderers.details.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;

import org.zkoss.zul.Div;

import com.braintree.model.BrainTreeTransactionDetailModel;
import com.braintree.model.TransactionCustomerModel;


public class TransactionCustomerDetailsListRender extends AbstractTransactionDetailsListRender
{
	@Override
	protected void populateKnownDetails(final Object context, final TypedObject item, final Widget widget,
			final Div detailContainer)
	{
		if (item != null && item.getObject() instanceof BrainTreeTransactionDetailModel)
		{
			final BrainTreeTransactionDetailModel transactionDetail = (BrainTreeTransactionDetailModel) item.getObject();

			final TransactionCustomerModel customer = transactionDetail.getCustomer();
			if (customer != null)
			{
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "customerId", new Object[0])),
						this.createValueLabel(customer.getCustomerID()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "customerName", new Object[0])),
						this.createValueLabel(customer.getName()), detailContainer);
				this.renderRow(this.createTitleLabel(LabelUtils.getLabel(widget, "customerEmail", new Object[0])),
						this.createValueLabel(customer.getEmail()), detailContainer);
			}
		}

	}
}
