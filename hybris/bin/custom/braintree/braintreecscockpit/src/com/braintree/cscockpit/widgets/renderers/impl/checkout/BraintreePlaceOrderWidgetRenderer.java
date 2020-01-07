package com.braintree.cscockpit.widgets.renderers.impl.checkout;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_ERROR;

import de.hybris.platform.cockpit.widgets.impl.DefaultWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.PlaceOrderWidgetRenderer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.widgets.controllers.BraintreeCheckoutController;


public class BraintreePlaceOrderWidgetRenderer extends PlaceOrderWidgetRenderer
{

	@Override
	protected Component createSummary(DefaultWidget<DefaultItemWidgetModel, CheckoutController> widget)
	{
		Div summary = new Div();
		summary.setSclass("csPlaceOrderSummary");
		boolean paymentMethodInCard = ((BraintreeCheckoutController) widget.getWidgetController()).isPaymentMethodAvailableInCard();
		String labelText;
		if (paymentMethodInCard)
		{
			labelText = LabelUtils.getLabel(widget, "paymentsAdded", 1);
		}
		else
		{
			labelText = LabelUtils.getLabel(widget, "noPaymentsAdded");
		}
		Label pmSummary = new Label(labelText);
		pmSummary.setSclass("csPaymentMethodSummaryValue");
		pmSummary.setParent(summary);
		return summary;
	}

	@Override
	protected void handlePlaceOrderEvent(DefaultWidget<DefaultItemWidgetModel, CheckoutController> widget, Event event)
			throws Exception
	{
		boolean paymentMethodInCard = ((BraintreeCheckoutController) widget.getWidgetController()).isPaymentMethodAvailableInCard();
		if (paymentMethodInCard)
		{
			super.handlePlaceOrderEvent(widget, event);
		}
		else
		{
			Messagebox.show(LabelUtils.getLabel(widget, "noPaymentMethodSelected"),
					LabelUtils.getLabel(widget, "failedToPlaceOrder"), 1, WIDGET_ICON_ERROR);
		}
	}
}
