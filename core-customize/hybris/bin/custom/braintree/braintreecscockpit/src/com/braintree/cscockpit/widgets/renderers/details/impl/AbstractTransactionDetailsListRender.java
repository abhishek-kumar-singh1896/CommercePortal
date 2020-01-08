package com.braintree.cscockpit.widgets.renderers.details.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.services.label.impl.CustomerModelLabelProvider;
import de.hybris.platform.cscockpit.widgets.renderers.details.impl.AbstractConfigurableItemWidgetDetailRenderer;
import de.hybris.platform.enumeration.EnumerationService;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;


//TODO:  move css style from class
public abstract class AbstractTransactionDetailsListRender extends
		AbstractConfigurableItemWidgetDetailRenderer<TypedObject, Widget>
{
	private EnumerationService enumerationService;
	private CustomerModelLabelProvider customerModelLabelProvider;

	@Override
	public Object createContext()
	{
		return null;
	}

	@Override
	public HtmlBasedComponent createContent(final Object context, final TypedObject item, final Widget widget)
	{
		final Div detailContainer = new Div();
		if (item != null)
		{
			detailContainer.setClass("btTransactionDiv");
			populateKnownDetails(context, item, widget, detailContainer);
		}
		return detailContainer;
	}

	abstract void populateKnownDetails(Object context, TypedObject item, Widget widget, Div detailContainer);

	protected void renderRow(final Label label, final Label value, final HtmlBasedComponent parent)
	{
		final Div content = new Div();
		content.setClass("btTransactionRenderRowDiv");
		if (label != null)
		{
			content.appendChild(label);
		}

		if (value != null)
		{
			content.appendChild(value);
		}

		content.setParent(parent);
	}

	protected Label createLabel(final String value, final String cssClass)
	{
		final Label label = new Label(value);
		label.setSclass(cssClass);
		return label;
	}

	protected Label createTitleLabel(final String value)
	{
		final Label csTransactionLabelTitle = this.createLabel(value, "csTransactionLabelTitle");
		csTransactionLabelTitle.setClass("btTransactionTitleLabel");
		return csTransactionLabelTitle;
	}

	protected Label createValueLabel(final String value)
	{
		final Label label = this.createLabel(value, "csTransactionLabelValue");
		label.setClass("btTransactionValueLabel");
		return label;
	}

	protected EnumerationService getEnumerationService()
	{
		return this.enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public CustomerModelLabelProvider getCustomerModelLabelProvider()
	{
		return this.customerModelLabelProvider;
	}

	@Required
	public void setCustomerModelLabelProvider(final CustomerModelLabelProvider customerModelLabelProvider)
	{
		this.customerModelLabelProvider = customerModelLabelProvider;
	}
}
