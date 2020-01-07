package com.braintree.cscockpit.widgets.renderers.impl.management;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_ERROR;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ReturnRequestCreateWidgetRenderer;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.braintree.constants.BraintreecscockpitConstants;


public class BraintreeReturnRequestCreateWidgetRenderer extends ReturnRequestCreateWidgetRenderer
{

	private static final String REFUND_ENTRY = "RefundEntry";
	private static final String REPLACEMENT_ENTRY = "ReplacementEntry";
	private static final String FAILED_TO_VALIDATE = "failedToValidate";

	@Override
	protected void handleReturnRequestCreateEvent(InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			Event event, List<ObjectValueContainer> returnObjectValueContainers) throws Exception
	{
		try
		{
			if (REFUND_ENTRY.equalsIgnoreCase(this.getListConfigurationType()))
			{
				createRefundOrderPreview(widget, returnObjectValueContainers);
			}
			else
			{
				if (!REPLACEMENT_ENTRY.equalsIgnoreCase(this.getListConfigurationType()))
				{
					throw new IllegalStateException("Unsupported return entry type [" + this.getListConfigurationType() + "]");
				}
				createReplacementRequest(widget, returnObjectValueContainers);
			}
		}
		catch (ValidationException ex)
		{
			Messagebox.show(ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage()),
					LabelUtils.getLabel(widget, FAILED_TO_VALIDATE), 1, WIDGET_ICON_ERROR);
		}

	}

	private void createReplacementRequest(InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			List<ObjectValueContainer> returnObjectValueContainers) throws ValidationException, InterruptedException
	{
		TypedObject typedObject;
		if (widget.getWidgetController().validateCreateReplacementRequest(returnObjectValueContainers))
		{
			typedObject = widget.getWidgetController().createReplacementRequest(returnObjectValueContainers);
			if (typedObject == null)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "error", new Object[0]), LabelUtils.getLabel(widget, "failed"), 1,
						WIDGET_ICON_ERROR);
			}
			else
			{
				this.getPopupWidgetHelper().dismissCurrentPopup();
				ReturnRequestModel returnRequestModel = (ReturnRequestModel) typedObject.getObject();
				Messagebox.show(LabelUtils.getLabel(widget, "rmaNumber", new Object[]
				{ returnRequestModel.getRMA() }), LabelUtils.getLabel(widget, "rmaNumberTitle"), 1,
						BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_SUCCESS);
				widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
			}
		}
		else
		{
			Messagebox.show(LabelUtils.getLabel(widget, FAILED_TO_VALIDATE, new Object[0]),
					LabelUtils.getLabel(widget, FAILED_TO_VALIDATE), 1, WIDGET_ICON_ERROR);
		}
	}

	private void createRefundOrderPreview(InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			List<ObjectValueContainer> returnObjectValueContainers) throws ValidationException, InterruptedException
	{
		TypedObject typedObject;
		if (widget.getWidgetController().validateCreateRefundRequest(returnObjectValueContainers))
		{
			typedObject = widget.getWidgetController().createRefundOrderPreview(returnObjectValueContainers);
			if (typedObject == null)
			{
				this.getPopupWidgetHelper().dismissCurrentPopup();
			}
			else
			{
				this.createRefundConfirmationPopupWindow(widget, this.getPopupWidgetHelper().getCurrentPopup().getParent());
			}
		}
		else
		{
			Messagebox.show(LabelUtils.getLabel(widget, FAILED_TO_VALIDATE, new Object[0]),
					LabelUtils.getLabel(widget, FAILED_TO_VALIDATE), 1, WIDGET_ICON_ERROR);
		}
	}
}
