package com.braintree.cscockpit.widgets.renderers.impl.checkout;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.details.WidgetDetailRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CheckoutPaymentWidgetRenderer;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.widgets.controllers.BraintreeCheckoutController;
import com.braintree.cscockpit.widgets.controllers.impl.DefaultBraintreeCheckoutController;
import com.braintree.cscockpit.widgets.renderers.impl.checkout.utils.PaymentMethodListUtils;


public class BraintreeCheckoutPaymentWidgetRenderer extends CheckoutPaymentWidgetRenderer
{
	private static final Logger LOGGER = Logger.getLogger(BraintreeCheckoutPaymentWidgetRenderer.class);

	@Override
	protected void populateMasterRow(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget, Listitem row,
			Object context, TypedObject item)
	{
		if (item != null && item.getObject() != null)
		{
			PaymentMethodListUtils.populateBasePaymentMethodRow(row, item);

			boolean paymentMethodSelected = ((BraintreeCheckoutController) widget.getWidgetController())
					.isPaymentMethodSelected(item);
			if (paymentMethodSelected)
			{
				Listcell selectedCell = new Listcell(LabelUtils.getLabel(widget, "selected"));
				row.appendChild(selectedCell);
			}
			else
			{
				Listcell actionCell = new Listcell();
				row.appendChild(actionCell);
				Div actionContainer = new Div();
				actionContainer.setParent(actionCell);
				Button payBtn = new Button(LabelUtils.getLabel(widget, "payButton"));
				payBtn.setParent(actionContainer);
				BigDecimal amount = BigDecimal.valueOf(widget.getWidgetController().getSuggestedAmountForPaymentOption());
				payBtn.addEventListener("onClick", new PayEventListener(widget, item, amount));
			}
		}
	}

	@Override
	protected Object populateHeaderRow(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget, Listhead row)
	{
		Listheader typeHeader = new Listheader(LabelUtils.getLabel(widget, "cardTypeHeader"));
		row.appendChild(typeHeader);
		Listheader paymentInfo = new Listheader(LabelUtils.getLabel(widget, "paymentInfoHeader"));
		row.appendChild(paymentInfo);
		Listheader cardHolder = new Listheader(LabelUtils.getLabel(widget, "cardHolderHeader"));
		row.appendChild(cardHolder);
		Listheader billingAddress = new Listheader(LabelUtils.getLabel(widget, "billingAddressHeader"));
		row.appendChild(billingAddress);
		Listheader actionHeader = new Listheader(LabelUtils.getLabel(widget, "actionHeader"));
		actionHeader.setWidth("100px");
		row.appendChild(actionHeader);
		return null;
	}


	@Override
	protected void renderDetailRow(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget, Listitem row,
			Listcell cell, Object rowContext, Object detailContext, TypedObject item, TypedObject detailItem)
	{
		Div detailContainer = new Div();
		detailContainer.setSclass("csListItemDetailContainer");
		WidgetDetailRenderer detailRenderer = this.getDetailRenderer(detailItem);
		if (detailRenderer != null)
		{
			HtmlBasedComponent detailContent = detailRenderer.createContent(detailContext, detailItem, widget);
			if (detailContent != null)
			{
				detailContainer.appendChild(detailContent);
			}
		}

		cell.appendChild(detailContainer);
	}

	private class PayEventListener implements EventListener
	{
		private final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget;
		private final TypedObject item;
		private final BigDecimal amount;

		PayEventListener(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget, TypedObject item,
				BigDecimal amountInput)
		{
			this.widget = widget;
			this.amount = amountInput;
			this.item = item;
		}

		@Override
		public void onEvent(Event event) throws Exception
		{
			handlePayUsingStoredCardEvent(this.widget, this.item, this.amount);
		}

		private void handlePayUsingStoredCardEvent(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
				TypedObject item, BigDecimal amount) throws Exception
		{
			if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && item.getObject() != null)
			{
				try
				{
					if (((DefaultBraintreeCheckoutController) widget.getWidgetController()).processPayment(item, amount))
					{
						widget.getWidgetModel().notifyListeners();
						widget.getWidgetController().dispatchEvent(null, widget, null);
					}
				}
				catch (PaymentException ex)
				{
					Messagebox.show(ex.getMessage(), LabelUtils.getLabel(widget, "failedToAuthorise"), 1, "z-msgbox z-msgbox-error");
					widget.getWidgetController().dispatchEvent(null, widget, null);
					LOGGER.error(ex.getMessage(), ex);
				}
				catch (ValidationException ex)
				{
					Messagebox.show(ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage()),
							LabelUtils.getLabel(widget, "failedToValidate"), 1, "z-msgbox z-msgbox-error");
					LOGGER.error(ex.getMessage(), ex);
				}
				catch (Exception ex)
				{
					LOGGER.error("Failed to use existing stored card", ex);
					throw ex;
				}
			}
		}
	}
}
