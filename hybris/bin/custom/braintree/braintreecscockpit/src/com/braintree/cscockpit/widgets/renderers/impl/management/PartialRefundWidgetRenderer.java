package com.braintree.cscockpit.widgets.renderers.impl.management;


import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_AVAILABLE_TRANSACTIONS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_BUTTON;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_INCORRECT_AMOUNT_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PARTIAL_REFUND_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REFUND_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.api.Textbox;
import com.braintree.cscockpit.widgets.controllers.BraintreePartialRefundController;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.exceptions.BraintreeErrorException;
import com.braintree.hybris.data.BrainTreeResponseResultData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

public class PartialRefundWidgetRenderer extends
                                         AbstractCreateWidgetRenderer<InputWidget<DefaultListWidgetModel<TypedObject>, BraintreePartialRefundController>> {

    protected static final Logger LOG = Logger.getLogger(PartialRefundWidgetRenderer.class);
    private static final String EMPTY_STRING = "";

    private PopupWidgetHelper popupWidgetHelper;

    @Override
    protected HtmlBasedComponent createContentInternal(
            final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreePartialRefundController> widget,
            HtmlBasedComponent htmlBasedComponent) {

        final BraintreePartialRefundController widgetController = widget.getWidgetController();

        final Div content = new Div();

        final Listbox paymentsBox = new Listbox();
        createTransactionListDropDown((OrderModel) widgetController.getOrder().getObject(), paymentsBox);


        wrapPaymentsBox(paymentsBox, content);

        final Textbox amountTextBox = createAmountField(widget, createDiv(content), BigDecimal.ZERO);

        paymentsBox.addEventListener(Events.ON_SELECT, event -> {
            if (event instanceof SelectEvent) {
                Set<Listitem> items = ((SelectEvent) event).getSelectedItems();
                if (items != null && !items.isEmpty()) {
                    Listitem selectedItem = items.iterator().next();
                    PaymentTransactionEntryModel payment = (PaymentTransactionEntryModel) selectedItem.getValue();
                    if (payment != null) {
                        // setValueToAmountTextBox(amountTextBox, payment);
                    }
                }
            }
        });
        if (!paymentsBox.getItems().isEmpty()) {
            paymentsBox.setSelectedItem((Listitem) paymentsBox.getItems().get(0));
            PaymentTransactionEntryModel payment = (PaymentTransactionEntryModel) paymentsBox.getSelectedItem().getValue();
            if (payment != null) {
                // setValueToAmountTextBox(amountTextBox, payment);
            }
        }

        final Div buttonBox = new Div();
        buttonBox.setClass("btTransactionActionButtonDiv");
        buttonBox.setParent(content);

        createButton(WIDGET_PARTIAL_REFUND_BUTTON, "btngreen", getButtonBox(content, "btTransactionActionButtonDiv"),
                event -> {
                    try {
                        Listitem li = paymentsBox.getSelectedItem();
                        PaymentTransactionEntryModel selected = null;
                        if (li != null && li.getValue() != null && li.getValue() instanceof PaymentTransactionEntryModel) {
                            selected = (PaymentTransactionEntryModel) li.getValue();
                        } else {
                            return;
                        }

                        final DecimalFormat format = new DecimalFormat();
                        format.setParseBigDecimal(true);
                        final BigDecimal amount = (BigDecimal) format.parse(amountTextBox.getValue());

                        BrainTreeResponseResultData response =
                                widgetController.partialRefund(selected, amount);

                        if (response.isSuccess()) {
                            Messagebox.show(getLabel(WIDGET_PARTIAL_REFUND_SUCCESS), getLabel(WIDGET_PARTIAL_REFUND_TITLE), Messagebox.OK,
                                            Messagebox.INFORMATION);
                            getPopupWidgetHelper().dismissCurrentPopup();
                        } else {
                            Messagebox
                                    .show(getLabel(WIDGET_PARTIAL_REFUND_ERROR + " " + response.getErrorMessage()),
                                          getLabel(WIDGET_PARTIAL_REFUND_TITLE),
                                          Messagebox.OK,
                                          Messagebox.ERROR);
                        }
                        widget.getWidgetController().dispatchEvent(null, widget, null);
                    } catch (BraintreeErrorException btee) {
                        LOG.error("Errors occurred while trying to make partial refund!", btee);
                        Messagebox.show(btee.getMessage(), getLabel(WIDGET_PARTIAL_REFUND_TITLE), Messagebox.OK,
                                        Messagebox.ERROR);
                    } catch (ParseException pe) {
                        Messagebox.show(getLabel(WIDGET_PARTIAL_REFUND_INCORRECT_AMOUNT_ERROR), getLabel(WIDGET_REFUND_TITLE), Messagebox.OK,
                                        Messagebox.ERROR);
                        LOG.debug("Error while trying to parse amount value!", pe);

                    }
                    catch (InterruptedException e) {
                        LOG.debug("Errors occurred while showing message box!", e);
                    }
                });

        return content;
    }

    private void setValueToAmountTextBox(Textbox amountTextBox, PaymentTransactionEntryModel payment) {
        amountTextBox.setValue(UIElementUtils.formatAmount(payment.getAmount()));
    }

    protected void wrapPaymentsBox(final Listbox paymentsListBox, Div content) {
        final Label paymentLabel = new Label(getLabel(WIDGET_PARTIAL_REFUND_AVAILABLE_TRANSACTIONS));
        Div sub = createDiv(content);
        paymentLabel.setClass("btLabelField");
        paymentsListBox.setMold("select");
        paymentsListBox.setClass("btTextField");
        sub.appendChild(paymentLabel);
        sub.appendChild(paymentsListBox);
    }

    protected void createButton(final String labelName,
                                final String style, final HtmlBasedComponent component, final EventListener eventListener) {
        final Button button = new Button();
        button.setLabel(getLabel(labelName));
        button.setParent(component);
        button.setStyle(style);
        if (eventListener != null) {
            button.addEventListener(Events.ON_CLICK, eventListener);
        }
    }

    protected Div getButtonBox(final Div content, final String style) {
        final Div buttonBox = new Div();
        buttonBox.setClass(style);
        buttonBox.setParent(content);
        return buttonBox;
    }

    protected Div createDiv(final Div content)
    {
        final Div cloneFieldsBox = new Div();
        cloneFieldsBox.setParent(content);
        cloneFieldsBox.setClass("btTransactionActionDiv");
        return cloneFieldsBox;
    }

    protected org.zkoss.zul.api.Textbox createAmountField(
            final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreePartialRefundController> widget, final Div content,
            final BigDecimal amount)
    {
        final org.zkoss.zul.api.Textbox
                transactionAmount = UIElementUtils.createTextField(widget, content, "amount", EMPTY_STRING);
        return transactionAmount;
    }


    private Listbox createTransactionListDropDown(OrderModel order, final Listbox statuses) {
        statuses.setMold("select");
        for (PaymentTransactionModel transaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel paymentEntry : transaction.getEntries()) {
                if (TransactionStatus.ACCEPTED.name().equals(paymentEntry.getTransactionStatus()) &&
                    (PaymentTransactionType.CAPTURE.equals(paymentEntry.getType()) || PaymentTransactionType.PARTIAL_CAPTURE.equals(paymentEntry.getType()))) {
                    Listitem li = new Listitem();
                    li.setValue(paymentEntry);
                    li.setLabel(paymentEntry.getRequestId() + " " + UIElementUtils.formatAmount(paymentEntry.getRefundedAmount() == null ?
                                                                                                paymentEntry.getAmount() :
                                                                                                paymentEntry.getAmount()
                                                                                                        .subtract(paymentEntry.getRefundedAmount())));
                    statuses.appendChild(li);
                }
            }
        }
        return statuses;
    }

    private BigDecimal getMaxAmountForPayment(final PaymentTransactionEntryModel payment) {
        BigDecimal max = BigDecimal.ZERO;
        if (TransactionStatus.ACCEPTED.name().equals(payment.getTransactionStatus()) &&
            (PaymentTransactionType.CAPTURE.equals(payment.getType())) || PaymentTransactionType.PARTIAL_CAPTURE.equals(payment.getType())) {
            BigDecimal amountForTransaction = payment.getRefundedAmount() == null ? payment.getAmount() : payment.getAmount().subtract(payment.getRefundedAmount());
            if(payment.getRefundedAmount()!= null){
                max = amountForTransaction.compareTo(BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getSubtotal())) > 0 ?
                        BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getSubtotal()).add(BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getDeliveryCost())).subtract(payment.getRefundedAmount()) : amountForTransaction;
            }else{
                max = amountForTransaction.compareTo(BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getSubtotal())) > 0 ?
                BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getSubtotal()).add(BigDecimal.valueOf(payment.getPaymentTransaction().getOrder().getDeliveryCost())) : amountForTransaction;
            }
        }
        return max;
    }

//    private PaymentTransactionModel getTransaction(OrderModel order, String transactionId) {
//        for (PaymentTransactionModel paymentTrx : order.getPaymentTransactions()) {
//            if (paymentTrx.getRequestId().equals(transactionId)) {
//                return paymentTrx;
//            }
//        }
//        return null;
//    }

    public PopupWidgetHelper getPopupWidgetHelper() {
        return popupWidgetHelper;
    }

    public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper) {
        this.popupWidgetHelper = popupWidgetHelper;
    }

}
