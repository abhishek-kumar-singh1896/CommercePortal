package com.braintree.cscockpit.widgets.renderers.impl.management;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CancellationController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.FullOrderCancellationWidgetRenderer;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.DefaultOrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDeniedException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;


public class BraintreeFullOrderCancellationWidgetRenderer extends FullOrderCancellationWidgetRenderer {

    protected void handleAttemptCancellationEvent(
            InputWidget<DefaultListWidgetModel<OrderCancelRequest>, CancellationController> widget, Event event,
            ObjectValueContainer orderCancelRequestObjectValueContainer) throws Exception {
        if ("onOK".equals(event.getName())) {
            try {
                TypedObject orderCancellationRequest = widget.getWidgetController().createOrderCancellationRequest(
                        orderCancelRequestObjectValueContainer);
                if (orderCancellationRequest != null) {
                    this.getPopupWidgetHelper().dismissCurrentPopup();
                    OrderCancelRecordEntryModel orderCancelRecordEntryModel = (OrderCancelRecordEntryModel) orderCancellationRequest
                            .getObject();
                    Messagebox.show(LabelUtils.getLabel(widget, "cancellationNumber", new Object[]
                                    {orderCancelRecordEntryModel.getCode()}), LabelUtils.getLabel(widget, "cancellationNumberTitle"), 1,
                            "z-msgbox z-msgbox-information");
                    widget.getWidgetController().dispatchEvent(null, widget, null);
                } else {
                    Messagebox.show(LabelUtils.getLabel(widget, "errorCreatingRequest", new Object[0]),
                            LabelUtils.getLabel(widget, "failed"), 1, "z-msgbox z-msgbox-error");
                }
            } catch (OrderCancelDeniedException ex) {
                String errorMessage = getOrderCancelDeniedMessage(ex);
                Messagebox.show(errorMessage, LabelUtils.getLabel(widget, "failedToValidate"), 1, "z-msgbox z-msgbox-error");
            } catch (OrderCancelException ex) {
                Messagebox.show(ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage()),
                        LabelUtils.getLabel(widget, "failedToValidate"), 1, "z-msgbox z-msgbox-error");
            } catch (ValidationException ex) {
                Messagebox.show(ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage()),
                        LabelUtils.getLabel(widget, "failedToValidate"), 1, "z-msgbox z-msgbox-error");
            }
        }

    }

    private String getOrderCancelDeniedMessage(OrderCancelDeniedException ex) {
        CancelDecision cancelDecision = ex.getCancelDecision();
        List<OrderCancelDenialReason> denialReasons = cancelDecision.getDenialReasons();
        if (CollectionUtils.isNotEmpty(denialReasons)) {
            OrderCancelDenialReason orderCancelDenialReason = denialReasons.iterator().next();
            if (orderCancelDenialReason instanceof DefaultOrderCancelDenialReason) {
                DefaultOrderCancelDenialReason reason = (DefaultOrderCancelDenialReason) orderCancelDenialReason;
                return reason.getDescription();
            }
        }
        return ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage());
    }
}
