package com.braintree.cscockpit.widgets.renderers.impl.management;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_AVAILABLE_AUTHORIZATION_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_CAPTURE_AMOUNT_NOT_MATH_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_CAPTURE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_CAPTURE_INCORRECT_AMOUNT_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_CAPTURE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_CAPTURE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REFUND_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import com.braintree.cscockpit.constraint.*;
import com.braintree.cscockpit.widgets.controllers.BraintreeMultipleCaptureController;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.exceptions.BraintreeErrorException;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.*;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.payment.dto.*;
import de.hybris.platform.payment.enums.*;
import de.hybris.platform.payment.model.*;

import org.apache.log4j.Logger;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.api.Textbox;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;


public class MultipleCaptureWidgetRenderer extends
		AbstractCreateWidgetRenderer<InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController>>
{
	private static final Logger LOG = Logger.getLogger(MultipleCaptureWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController> widget,
			final HtmlBasedComponent basedComponent)
	{
		final Div content = new Div();
		final BraintreeMultipleCaptureController widgetController = widget.getWidgetController();

        final Listbox transactionsList = new Listbox();
        createTransactionListDropDown((OrderModel) widgetController.getOrder().getObject(), transactionsList);
        wrapTransactionBox(transactionsList, content);

        BigDecimal amount = BigDecimal.ZERO;

		if (transactionsList.getItemCount() > 0){
			transactionsList.getItemAtIndex(0).setSelected(true);
			amount = ((PaymentTransactionEntryModel)transactionsList.getSelectedItem().getValue()).getAmount();
		}

		Textbox amountField = createAmountField(widget, createDiv(content), amount);

        transactionsList.addEventListener(Events.ON_SELECT, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (event instanceof SelectEvent) {
                    Set<Listitem> items = ((SelectEvent) event).getSelectedItems();
                    if (items != null && !items.isEmpty()) {
                        Listitem selectedItem = items.iterator().next();
                        PaymentTransactionEntryModel payment = (PaymentTransactionEntryModel) selectedItem.getValue();
                        if (payment != null) {
                            setValueToAmountTextBox(amountField, payment);
                        }
                    }
                }
            }
        });

		final Div buttonBox = new Div();
		buttonBox.setClass("btTransactionActionButtonDiv");
		buttonBox.setParent(content);


		createButton(widget, "btngreen", getButtonBox(content, "btTransactionActionButtonDiv"), "partialCaptureButton",
				createPartialCaptureEventListener(widget, amountField, transactionsList));

		return content;
	}


	protected void createButton(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController> widget,
			final String style, final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName));
		button.setParent(component);
		button.setStyle(style);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	private EventListener createPartialCaptureEventListener(
			final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController> widget,
			final Textbox amountField, final Listbox transactionsList)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handlePartialCaptureButtonClickEvent(widget, amountField, transactionsList);
			}
		};
	}

	private void handlePartialCaptureButtonClickEvent(
			final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController> widget,
			final Textbox amountField, final Listbox transactionsList) throws InterruptedException
	{
		try
		{
			final DecimalFormat format = new DecimalFormat();
			format.setParseBigDecimal(true);

			final BigDecimal amount = (BigDecimal) format.parse(amountField.getValue());
			final String authorizeTransactionID = transactionsList.getSelectedItem().getLabel();
			Boolean isComplete = widget.getWidgetController().partialCapture(amount, authorizeTransactionID);
			LOG.info("isComplete: " + isComplete);
//			if (isComplete)
//			{
//				Messagebox.show(getLabel(WIDGET_PARTIAL_CAPTURE_SUCCESS), getLabel(WIDGET_PARTIAL_CAPTURE_TITLE), Messagebox.OK,
//						Messagebox.INFORMATION);
//				getPopupWidgetHelper().dismissCurrentPopup();
//			}
//			else
//			{
//				Messagebox.show(getLabel(WIDGET_PARTIAL_CAPTURE_AMOUNT_NOT_MATH_ERROR)
//						+ UIElementUtils.formatAmount(widget.getWidgetController().getAmountAvailableForMultiCapture()),
//                                                getLabel(WIDGET_PARTIAL_CAPTURE_TITLE), Messagebox.OK, Messagebox.ERROR);
//			}
		}
		catch (BraintreeErrorException e)
		{
			Messagebox.show(getLabel(WIDGET_PARTIAL_CAPTURE_ERROR) + e.getMessage(), getLabel(WIDGET_PARTIAL_CAPTURE_TITLE),
					Messagebox.OK, Messagebox.ERROR);

			LOG.error("Errors occurred: " + e.getMessage() + " for transaction :" + e.getBraintreeId(), e);
		}
		catch (ParseException e)
		{
			Messagebox.show(getLabel(WIDGET_PARTIAL_CAPTURE_INCORRECT_AMOUNT_ERROR), getLabel(WIDGET_REFUND_TITLE), Messagebox.OK,
					Messagebox.ERROR);
			LOG.error("Errors occurred!", e);
		}
//		catch (InterruptedException e)
//		{
//			LOG.error("Errors occurred while showing message box!", e);
//		}
		widget.getWidgetController().dispatchEvent(null, widget, null);
	}

	protected Textbox createAmountField(
			final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeMultipleCaptureController> widget, final Div content,
			final BigDecimal amount)
	{
		final Textbox transactionAmount = UIElementUtils.createTextField(widget, content, "captureAmount", formatAmount(amount));
		return transactionAmount;
	}

	private String formatAmount(final BigDecimal amount)
	{
		final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locales.getCurrent());
		decimalFormat.applyPattern("#0.00");
		return decimalFormat.format(amount);
	}


	protected Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btTransactionActionDiv");
		return cloneFieldsBox;
	}

	protected Div getButtonBox(final Div content, final String style)
	{
		final Div buttonBox = new Div();
		buttonBox.setClass(style);
		buttonBox.setParent(content);
		return buttonBox;
	}

	private Listbox createmultipleCaptureTable(final HtmlBasedComponent container) {
		final Listbox multipleCaptureTable = new Listbox();
		multipleCaptureTable.setParent(container);
		multipleCaptureTable.setVflex(false);
		multipleCaptureTable.setFixedLayout(true);

		return multipleCaptureTable;
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

    private Listbox createTransactionListDropDown(OrderModel order, final Listbox statuses) {
        statuses.setMold("select");
        for (PaymentTransactionModel transaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel paymentEntry : transaction.getEntries()) {
                if (TransactionStatus.ACCEPTED.name().equals(paymentEntry.getTransactionStatus()) &&
                        (PaymentTransactionType.AUTHORIZATION.equals(paymentEntry.getType()))) {
                    Listitem li = new Listitem();
                    li.setValue(paymentEntry);
                    li.setLabel(paymentEntry.getRequestId());
                    statuses.appendChild(li);
                }
            }
        }
        return statuses;
    }
    
    protected void wrapTransactionBox(final Listbox transactionsList, Div content) {
        final Label paymentLabel = new Label(getLabel(WIDGET_PARTIAL_AVAILABLE_AUTHORIZATION_TITLE));
        Div sub = createDiv(content);
        paymentLabel.setClass("btLabelField");
        transactionsList.setMold("select");
        transactionsList.setClass("btTextField");
        sub.appendChild(paymentLabel);
        sub.appendChild(transactionsList);
    }

    private void setValueToAmountTextBox(Textbox amountTextBox, PaymentTransactionEntryModel payment) {
        amountTextBox.setValue(UIElementUtils.formatAmount(payment.getAmount()));
    }

}
