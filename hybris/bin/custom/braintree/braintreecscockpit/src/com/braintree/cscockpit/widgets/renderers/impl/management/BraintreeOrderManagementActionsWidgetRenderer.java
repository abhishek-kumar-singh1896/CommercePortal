package com.braintree.cscockpit.widgets.renderers.impl.management;

import com.braintree.constants.BraintreeConstants;
import com.braintree.cscockpit.widgets.controllers.BraintreeOrderManagementActionsWidgetController;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import com.braintree.cscockpit.widgets.controllers.impl.DefaultBraintreeOrderManagementActionsWidgetController;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.events.CsCockpitEvent;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderManagementActionsWidgetRenderer;


public class BraintreeOrderManagementActionsWidgetRenderer extends OrderManagementActionsWidgetRenderer {

    private final static Logger LOG = Logger.getLogger(BraintreeOrderManagementActionsWidgetRenderer.class);

    protected HtmlBasedComponent createContentInternal(
            Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, HtmlBasedComponent rootContainer) {
        Div component = new Div();
        if (widget.getWidgetController() instanceof BraintreeOrderManagementActionsWidgetController) {

            final String paymentProvider = ((BraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).getPaymentProvider();
            final String intent = ((BraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).getBraintreeIntent();
            component.setSclass("orderManagementActionsWidget");

            if (BraintreeConstants.BRAINTREE_PAYMENT.equals(paymentProvider) || BraintreeConstants.APPLE_PAY_PAYMENT.equals(paymentProvider)) {
                createButtonsForBraintreeCCPayment(widget, component);
            } else if (BraintreeConstants.PAYPAL_INTENT_SALE.equalsIgnoreCase(intent)) {
                createButtonsForSaleIntent(widget, component);
            } else if (BraintreeConstants.PAYPAL_INTENT_AUTHORIZE.equalsIgnoreCase(intent)) {
                createButtonsForAuthorizeIntent(widget, component);
            } else if (BraintreeConstants.PAYPAL_INTENT_ORDER.equalsIgnoreCase(intent)) {
                createButtonsForOrderIntent(widget, component);
            } else {
                LOG.error("Order was placed with incorrect braintree.paypal.intent = '" + intent + "'.");
                return super.createContentInternal(widget, rootContainer);
            }
            widget.getWidgetContainer().addCockpitEventAcceptor(new PartialRefundRefreshEventAcceptor(component.getLastChild(),
                    (DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()));
            return component;
        } else {
            return super.createContentInternal(widget, rootContainer);
        }
    }

    private void createButtonsForBraintreeCCPayment(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, Div component) {
        this.createButton(widget, component, "voidAuthorization", "csVoidAuthorizationWidgetConfig",
                "csVoidAuthorizationWidget-Popup", "csVoidAuthorizationWidget", "popup.voidAuthorization",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isVoidAuthorizationPossible());
        this.createButton(widget, component, "multipleCaptureOrder", "csMultipleCaptureWidgetConfig",
                "csMultipleCaptureWidget-Popup", "csMultipleCaptureWidget", "popup.multipleCapture",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isMultipleCapturePossible());
        this.createButton(widget, component, "partialRefundForOrder", "csPartialRefundWidgetConfig",
                "csPartialRefundWidget-Popup", "csPartialRefundWidget", "popup.partialRefund",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isPartialRefundPossible());
    }

    private void createButtonsForOrderIntent(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, Div component) {
        this.createButton(widget, component, "authorizeOrder", "csOrderAuthorizationWidgetConfig",
                "csOrderAuthorizationWidget-Popup", "csOrderAuthorizationWidgetConfig", "popup.authorizeOrder",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isAvailableOrderAuthorization());
        this.createButton(widget, component, "voidAuthorization", "csVoidAuthorizationWidgetConfig",
                "csVoidAuthorizationWidget-Popup", "csVoidAuthorizationWidget", "popup.voidAuthorization",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isVoidAuthorizationPossible());
        this.createButton(widget, component, "multipleCaptureOrder", "csMultipleCaptureWidgetConfig",
                "csMultipleCaptureWidget-Popup", "csMultipleCaptureWidget", "popup.multipleCapture",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isMultipleCapturePossible());
        this.createButton(widget, component, "partialRefundForOrder", "csPartialRefundWidgetConfig",
                "csPartialRefundWidget-Popup", "csPartialRefundWidget", "popup.partialRefund",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isPartialRefundPossible());
    }

    private void createButtonsForAuthorizeIntent(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, Div component) {
        this.createButton(widget, component, "voidAuthorization", "csVoidAuthorizationWidgetConfig",
                "csVoidAuthorizationWidget-Popup", "csVoidAuthorizationWidget", "popup.voidAuthorization",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isVoidAuthorizationPossible());
        this.createButton(widget, component, "multipleCaptureOrder", "csMultipleCaptureWidgetConfig",
                "csMultipleCaptureWidget-Popup", "csMultipleCaptureWidget", "popup.multipleCapture",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isMultipleCapturePossible());
        this.createButton(widget, component, "partialRefundForOrder", "csPartialRefundWidgetConfig",
                "csPartialRefundWidget-Popup", "csPartialRefundWidget", "popup.partialRefund",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isPartialRefundPossible());
    }

    private void createButtonsForSaleIntent(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget, Div component) {
        this.createButton(widget, component, "partialRefundForOrder", "csPartialRefundWidgetConfig",
                "csPartialRefundWidget-Popup", "csPartialRefundWidget", "popup.partialRefund",
                !((DefaultBraintreeOrderManagementActionsWidgetController) widget.getWidgetController()).isPartialRefundPossible());
    }

    protected void handleButtonClickEvent(Widget<DefaultItemWidgetModel, OrderManagementActionsWidgetController> widget,
                                          Event event, Div container, String springWidgetName, String popupCode, String cssClass,
                                          String popupTitleLabelName) {
        if ("csMultipleCaptureWidgetConfig".equals(springWidgetName) || "csPartialRefundWidgetConfig".equals(springWidgetName) || "csOrderAuthorizationWidgetConfig".equals(springWidgetName)) {
            this.getPopupWidgetHelper().createPopupWidget(container, springWidgetName, popupCode, cssClass, LabelUtils.getLabel(widget, popupTitleLabelName, new Object[0]), 350);
        } else {
            super.handleButtonClickEvent(widget, event, container, springWidgetName, popupCode, cssClass, popupTitleLabelName);
        }
    }

    protected class PartialRefundRefreshEventAcceptor implements CockpitEventAcceptor {

        private transient Component partialRefundButton;
        private transient DefaultBraintreeOrderManagementActionsWidgetController widgetController;

        public PartialRefundRefreshEventAcceptor(Component partialRefundButton,
                DefaultBraintreeOrderManagementActionsWidgetController widgetController) {
            this.partialRefundButton = partialRefundButton;
            this.widgetController = widgetController;
        }

        @Override
        public void onCockpitEvent(CockpitEvent cockpitEvent) {
            if (cockpitEvent instanceof CsCockpitEvent) {
                CsCockpitEvent event = (CsCockpitEvent) cockpitEvent;
                if (event.getData() != null && !event.getData().isEmpty()) {
                    if (event.getData().containsKey("refresh") && Boolean.TRUE.equals(event.getData().get("refresh"))) {
                        ((Button) partialRefundButton).setDisabled(!widgetController.isPartialRefundPossible());
                    }
                }
            }
        }
    }

}
