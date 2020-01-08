package com.braintree.cscockpit.widgets.renderers.impl.management;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_TRANSACTION_CREATE_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import com.braintree.converters.BraintreeTransactionDetailConverter;
import com.braintree.cscockpit.constraint.RequiredAmountConstraint;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.widgets.controllers.BraintreeOrderAuthorizationController;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.hybris.data.BraintreeTransactionEntryData;
import com.braintree.model.BrainTreeTransactionDetailModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;


public class OrderAuthorizationWidgetRenderer extends
        AbstractCreateWidgetRenderer<InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController>>
{
    private static final Logger LOG = Logger.getLogger(OrderAuthorizationWidgetRenderer.class);

    private PopupWidgetHelper popupWidgetHelper;
    private CsBrainTreeFacade csBrainTreeFacade;
    private ItemAppender<TypedObject> detailItemAppender;
    private BraintreeTransactionDetailConverter transactionDetailPopulator;
    private OrderService orderService;

    @Override
    protected HtmlBasedComponent createContentInternal(
            final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget,
            final HtmlBasedComponent basedComponent)
    {
        final Div content = new Div();

        addPrettyTitle(widget, content, "paymentDetails");

        final Div paymentDetailsContent = new Div();
        paymentDetailsContent.setParent(content);

        final Textbox amount = createTextField(widget, paymentDetailsContent, "amount");
        amount.setValue(widget.getWidgetController().getTotalAmount().toString());

        createButton(widget, content, "createTransaction", new EventListener()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                OrderAuthorizationWidgetRenderer.this.handleSaveButtonClickEvent(widget, amount);
            }
        });

        return content;
    }

    private void handleSaveButtonClickEvent(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget,
                                            final org.zkoss.zul.Textbox amount)
    {
        BraintreeOrderAuthorizationController controller = widget.getWidgetController();
        final BrainTreeInfo brainTreeInfo = new BrainTreeInfo();
        brainTreeInfo.setAmount(amount.getValue());
        brainTreeInfo.setPaymentMethodToken(controller.getPaymentMethodToken());
        setCustomFields(brainTreeInfo, controller.getCustomFields());

        final PaymentTransactionEntryModel transaction;
        try {
            transaction = getCsBrainTreeFacade().authorizePayment((OrderModel)controller.getOrder().getObject(), controller.getCustomFields(), BigDecimal.valueOf(Double.valueOf(amount.getValue())));
            processResult(widget, transaction);
        } catch (final AdapterException e) {
            LOG.error("Error, message: " + e.getMessage(), e);
            showErrorMessage("Error message: " + e.getMessage());
        }
    }

    private void setCustomFields(BrainTreeInfo braintreeInfo, Map<String, String> customFields)
    {
        for (final String key : customFields.keySet())
        {
            braintreeInfo.setCustom(key, customFields.get(key));
        }
    }

    protected void processResult(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget,
                                 final PaymentTransactionEntryModel result)
    {
            if (!TransactionStatus.REJECTED.name().equalsIgnoreCase(result.getTransactionStatus()))
            {
                updateTransactionPage(widget);
                showSuccessMessage(widget, result);
                getPopupWidgetHelper().dismissCurrentPopup();
            }
            else
            {
                showErrorMessage("Error");
            }
    }

    private void updateTransactionPage(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget)
    {
        widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
    }

    private void showSuccessMessage(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget,
                                    final PaymentTransactionEntryModel result)
    {
        try {
            Messagebox.show(getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS) + " " + getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX) + ": " + result.getRequestId(),
                    getLabel(WIDGET_TRANSACTION_CREATE_TITLE), Messagebox.OK, Messagebox.INFORMATION);
        } catch (final InterruptedException e) {
            LOG.error("Errors occurred while showing message box!" + e.getMessage(), e);
        }
    }

    private void showErrorMessage(String errorMessageFromResult) {
        String errorMessage;
        if (StringUtils.isBlank(errorMessageFromResult)) {
            errorMessage = getLabel(WIDGET_MESSAGE_TRANSACTION_CREATE_ERROR);
        } else {
            errorMessage = errorMessageFromResult;
        }

        try {
            Messagebox.show(errorMessage, getLabel(WIDGET_TRANSACTION_CREATE_TITLE), Messagebox.OK, Messagebox.ERROR);
        } catch (final InterruptedException e) {
            LOG.error("Errors occurred while showing message box!" + e.getMessage(), e);
        }
    }

    private void handleTransactionSelectItem(final BraintreeTransactionEntryData transaction)
    {
        final BrainTreeTransactionDetailModel detailModel = getTransactionDetailPopulator().convert(transaction);
        final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(detailModel);
        getDetailItemAppender().add(itemTypedObject, 1L);
    }

    protected void addPrettyTitle(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget, final Div content,
                                  final String label)
    {
        final Div titleBox = new Div();
        titleBox.setClass("btAuthorizeOrderCustomPrettyTitleDiv");
        titleBox.setParent(content);

        final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
        titleOneLabel.setParent(titleBox);
        titleOneLabel.setClass("btAuthorizeOrderCustomPrettyTitleLabel");
    }

    protected org.zkoss.zul.Textbox createTextField(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget, final Div parent,
                                                    final String label)
    {
        return createTextField(widget, parent, label, null);
    }

    protected org.zkoss.zul.Textbox createTextField(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget, final Div parent,
                                                    final String label, final String exampleMessageLabel)
    {
        final Div divParent = createDiv(parent);
        final Textbox textField = UIElementUtils.createTextField(widget, parent, label, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(exampleMessageLabel))
        {
            final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, exampleMessageLabel, new Object[0]));
            titleOneLabel.setClass("btAuthorizeOrderTextFieldLabel");
            titleOneLabel.setParent(divParent);
        }
        return textField;
    }

    protected Div createDiv(final Div content)
    {
        final Div cloneFieldsBox = new Div();
        cloneFieldsBox.setParent(content);
        cloneFieldsBox.setClass("btAuthorizeOrderDiv");
        return cloneFieldsBox;
    }

    protected void createButton(final InputWidget<DefaultListWidgetModel<TypedObject>, BraintreeOrderAuthorizationController> widget,
                                final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
    {
        final Div saveButtonBox = new Div();
        saveButtonBox.setClass("btAuthorizeOrderCustomButton");
        saveButtonBox.setParent(component);

        final Button button = new Button();
        button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
        button.setParent(saveButtonBox);
        button.addEventListener(Events.ON_CLICK, eventListener);
    }

    public void setPopupWidgetHelper(PopupWidgetHelper popupWidgetHelper) {
        this.popupWidgetHelper = popupWidgetHelper;
    }

    public CsBrainTreeFacade getCsBrainTreeFacade() {
        return csBrainTreeFacade;
    }

    public void setCsBrainTreeFacade(CsBrainTreeFacade csBrainTreeFacade) {
        this.csBrainTreeFacade = csBrainTreeFacade;
    }

    public PopupWidgetHelper getPopupWidgetHelper() {
        return popupWidgetHelper;
    }

    public ItemAppender<TypedObject> getDetailItemAppender() {
        return detailItemAppender;
    }

    public void setDetailItemAppender(ItemAppender<TypedObject> detailItemAppender) {
        this.detailItemAppender = detailItemAppender;
    }

    public BraintreeTransactionDetailConverter getTransactionDetailPopulator() {
        return transactionDetailPopulator;
    }

    public void setTransactionDetailPopulator(BraintreeTransactionDetailConverter transactionDetailPopulator) {
        this.transactionDetailPopulator = transactionDetailPopulator;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
