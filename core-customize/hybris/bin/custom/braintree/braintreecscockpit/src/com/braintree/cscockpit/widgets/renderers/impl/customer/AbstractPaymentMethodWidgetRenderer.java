package com.braintree.cscockpit.widgets.renderers.impl.customer;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;


public abstract class AbstractPaymentMethodWidgetRenderer extends
		AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerController>>
{
	private PopupWidgetHelper popupWidgetHelper;

	protected Textbox createTextField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div parent,
			final String label, final String value)
	{
		return UIElementUtils.createTextField(widget, createDiv(parent), label, value);
	}

	protected Textbox createViewTextField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div parent,
			final String label, final String value)
	{
		final Textbox textField = UIElementUtils.createTextField(widget, createDiv(parent), label, value);
		textField.setReadonly(Boolean.TRUE.booleanValue());
		return textField;
	}


	protected Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btCustomerPaymentMethodDiv");
		return cloneFieldsBox;
	}

	protected void addPrettyTitle(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final String label)
	{
		final Div titleBox = new Div();
		titleBox.setSclass("btPrettyTitleDiv");
		titleBox.setParent(content);

		final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
		titleOneLabel.setParent(titleBox);
		titleOneLabel.setClass("btPrettyTitleLabel");
	}

	protected void addHelpTitle(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final String label)
	{
		final Div titleBox = new Div();
		titleBox.setSclass("btPrettyTitleDiv");
		titleBox.setParent(content);

		final Label titleOneLabel = new Label(LabelUtils.getLabel(widget, label, new Object[0]));
		titleOneLabel.setParent(titleBox);
		titleOneLabel.setClass("btCustomPaymentMethodHelpLabel");
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

}
