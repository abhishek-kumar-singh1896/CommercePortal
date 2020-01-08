package com.braintree.cscockpit.widgets.renderers.impl.management;

import com.braintree.cscockpit.widgets.controllers.BraintreeVoidAuthorizationController;
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
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_AUTHORIZATION_AVAILABLE_TRANSACTIONS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_AUTHORIZATION_BUTTON;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_AUTHORIZATION_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_AUTHORIZATION_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_VOID_AUTHORIZATION_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

public class VoidAuthorizationWidgetRenderer extends
        AbstractCreateWidgetRenderer<InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeVoidAuthorizationController>> {

    protected static final Logger LOG = Logger.getLogger(VoidAuthorizationWidgetRenderer.class);


    private PopupWidgetHelper popupWidgetHelper;

    @Override
    protected HtmlBasedComponent createContentInternal(
            final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeVoidAuthorizationController> widget,
            HtmlBasedComponent htmlBasedComponent) {

        final BraintreeVoidAuthorizationController widgetController = widget.getWidgetController();

        final Div content = new Div();

        final Listbox authorizationsBox = new Listbox();
        createTransactionListDropDown((OrderModel) widgetController.getOrder().getObject(), authorizationsBox);

        wrapAuthorizationsBox(authorizationsBox, content);

        if (!authorizationsBox.getItems().isEmpty()) {
            authorizationsBox.setSelectedItem((Listitem) authorizationsBox.getItems().get(0));
        }

        final Div buttonBox = new Div();
        buttonBox.setClass("btTransactionActionButtonDiv");
        buttonBox.setParent(content);

        createButton(WIDGET_VOID_AUTHORIZATION_BUTTON, "btngreen", getButtonBox(content, "btTransactionActionButtonDiv"),
                new EventListener() {
                    @Override
                    public void onEvent(final Event event) throws Exception {
                        try {
                            PaymentTransactionEntryModel authorization = (PaymentTransactionEntryModel) authorizationsBox.getSelectedItem().getValue();
                            final BrainTreeResponseResultData response = widgetController.voidAuthorization(authorization);

                            if (response.isSuccess()) {
                                Messagebox.show(getLabel(WIDGET_VOID_AUTHORIZATION_SUCCESS), getLabel(WIDGET_VOID_AUTHORIZATION_TITLE), Messagebox.OK,
                                        Messagebox.INFORMATION);
                                getPopupWidgetHelper().dismissCurrentPopup();
                            } else {
                                Messagebox
                                        .show(getLabel(WIDGET_VOID_AUTHORIZATION_ERROR + " " + response.getErrorMessage()),
                                                getLabel(WIDGET_VOID_AUTHORIZATION_TITLE),
                                                Messagebox.OK,
                                                Messagebox.ERROR);
                            }
                            widget.getWidgetController().dispatchEvent(null, widget, null);
                        } catch (BraintreeErrorException btee) {
                            LOG.error("Errors occurred while trying to make void authorization!", btee);
                            Messagebox.show(btee.getMessage(), getLabel(WIDGET_VOID_AUTHORIZATION_TITLE), Messagebox.OK,
                                    Messagebox.ERROR);
                        } catch (InterruptedException e) {
                            LOG.debug("Errors occurred while showing message box!", e);
                        }
                    }
                });

        return content;
    }

    protected void wrapAuthorizationsBox(final Listbox authorizationsListBox, Div content) {
        final Label authorizationLabel = new Label(getLabel(WIDGET_VOID_AUTHORIZATION_AVAILABLE_TRANSACTIONS));
        Div sub = createDiv(content);
        authorizationLabel.setClass("btLabelField");
        authorizationsListBox.setMold("select");
        authorizationsListBox.setClass("btTextField");
        sub.appendChild(authorizationLabel);
        sub.appendChild(authorizationsListBox);
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

    protected Div createDiv(final Div content) {
        final Div cloneFieldsBox = new Div();
        cloneFieldsBox.setParent(content);
        cloneFieldsBox.setClass("btTransactionActionDiv");
        return cloneFieldsBox;
    }

    private Listbox createTransactionListDropDown(OrderModel order, final Listbox statuses) {
        statuses.setMold("select");
        for (PaymentTransactionModel transaction : order.getPaymentTransactions()) {
            for (PaymentTransactionEntryModel paymentEntry : transaction.getEntries()) {
                if (TransactionStatus.ACCEPTED.name().equals(paymentEntry.getTransactionStatus()) &&
                        PaymentTransactionType.AUTHORIZATION.equals(paymentEntry.getType())) {
                    Listitem li = new Listitem();
                    li.setValue(paymentEntry);
                    li.setLabel(paymentEntry.getRequestId());
                    statuses.appendChild(li);
                }
            }
        }
        return statuses;
    }

    public PopupWidgetHelper getPopupWidgetHelper() {
        return popupWidgetHelper;
    }

    public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper) {
        this.popupWidgetHelper = popupWidgetHelper;
    }
}
