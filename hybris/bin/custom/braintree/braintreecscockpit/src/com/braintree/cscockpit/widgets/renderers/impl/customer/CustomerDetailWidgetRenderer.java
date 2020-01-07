package com.braintree.cscockpit.widgets.renderers.impl.customer;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.details.WidgetDetailRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.model.BraintreeCustomerDetailsModel;


public class CustomerDetailWidgetRenderer extends AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerController>>
{
	private WidgetDetailRenderer<TypedObject, Widget> detailRenderer;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();

		final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
		if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			final HtmlBasedComponent dummyLabel1 = this.getDetailRenderer().createContent((Object) null, currentCustomer, widget);
			if (dummyLabel1 != null)
			{
				dummyLabel1.setParent(content);
			}
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noCustomerSelected", new Object[0]));
			dummyLabel.setParent(content);
		}

		return content;
	}

	public WidgetDetailRenderer<TypedObject, Widget> getDetailRenderer()
	{
		return detailRenderer;
	}

	public void setDetailRenderer(final WidgetDetailRenderer<TypedObject, Widget> detailRenderer)
	{
		this.detailRenderer = detailRenderer;
	}
}
