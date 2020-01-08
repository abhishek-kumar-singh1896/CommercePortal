package com.braintree.cscockpit.widgets.renderers.impl.checkout;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ICON_ERROR;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AddressCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PostPopupAction;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.validators.BraintreeAddressValidator;


public class BraintreeAddressCreateWidgetRenderer extends AddressCreateWidgetRenderer
{
	private static final Logger LOGGER = Logger.getLogger(BraintreeAddressCreateWidgetRenderer.class);
	private BraintreeAddressValidator braintreeAddressValidator;

	@Override
	protected void handleCreateClickEvent(InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			Event event, ObjectValueContainer container, String configurationTypeCode) throws Exception
	{
		try
		{
			getBraintreeAddressValidator().validate(container);
			TypedObject ex = widget.getWidgetController().createNewCustomerAddress(container, configurationTypeCode);
			PostPopupAction postPopupAction = this.getPopupWidgetHelper().getPostPopupAction();
			if (postPopupAction != null)
			{
				postPopupAction.execute(ex);
			}

			widget.getWidgetController().dispatchEvent(null, this, Collections.<String, Object> emptyMap());
			((DefaultMasterDetailListWidgetModel) widget.getWidgetModel()).notifyListeners();
			this.getPopupWidgetHelper().dismissCurrentPopup();
		}
		catch (ValueHandlerException ex)
		{
			Messagebox.show(ex.getMessage(), LabelUtils.getLabel(widget, "unableToCreateAddress"), 1, WIDGET_ICON_ERROR);
			LOGGER.debug("unable to create item", ex);
		}
		catch (ValidationException ex)
		{
			Messagebox.show(ex.getMessage() + (ex.getCause() == null ? "" : " - " + ex.getCause().getMessage()),
					LabelUtils.getLabel(widget, "failedToValidate"), 1, WIDGET_ICON_ERROR);
			LOGGER.error(ex.getMessage(), ex);
		}
	}


	public BraintreeAddressValidator getBraintreeAddressValidator()
	{
		return braintreeAddressValidator;
	}

	public void setBraintreeAddressValidator(BraintreeAddressValidator braintreeAddressValidator)
	{
		this.braintreeAddressValidator = braintreeAddressValidator;
	}
}
