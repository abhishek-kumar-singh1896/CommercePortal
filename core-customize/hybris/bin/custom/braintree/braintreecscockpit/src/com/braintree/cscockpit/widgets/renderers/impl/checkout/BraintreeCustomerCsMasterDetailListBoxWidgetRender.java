package com.braintree.cscockpit.widgets.renderers.impl.checkout;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ConfigurableCsMasterDetailListboxWidgetRenderer;

import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.braintree.cscockpit.widgets.renderers.impl.checkout.utils.PaymentMethodListUtils;


public class BraintreeCustomerCsMasterDetailListBoxWidgetRender extends ConfigurableCsMasterDetailListboxWidgetRenderer
{
	@Override
	protected void populateMasterRow(ListboxWidget<DefaultMasterDetailListWidgetModel<TypedObject>, WidgetController> widget,
			Listitem row, Object context, TypedObject item)
	{
		if (item != null && item.getObject() != null)
		{
			PaymentMethodListUtils.populateBasePaymentMethodRow(row, item);
		}
	}

	@Override
	protected Object populateHeaderRow(ListboxWidget<DefaultMasterDetailListWidgetModel<TypedObject>, WidgetController> widget,
			Listhead row)
	{
		Listheader typeHeader = new Listheader(LabelUtils.getLabel(widget, "cardTypeHeader"));
		row.appendChild(typeHeader);
		Listheader paymentInfo = new Listheader(LabelUtils.getLabel(widget, "paymentInfoHeader"));
		row.appendChild(paymentInfo);
		Listheader cardHolder = new Listheader(LabelUtils.getLabel(widget, "cardHolderHeader"));
		row.appendChild(cardHolder);
		Listheader billingAddress = new Listheader(LabelUtils.getLabel(widget, "billingAddressHeader"));
		row.appendChild(billingAddress);
		return null;
	}
}
