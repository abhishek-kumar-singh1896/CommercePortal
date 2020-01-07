package com.braintree.cscockpit.widgets.renderers.impl;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.session.impl.OrderedConfigurableBrowserArea;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.CustomerAddressesListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CustomerAddressesWidgetRenderer;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;


public class BraintreeCustomerAddressesWidgetRenderer extends CustomerAddressesWidgetRenderer
{

	@Override
	protected HtmlBasedComponent createContentInternal(
			final DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget, HtmlBasedComponent rootContainer)
	{
		final HtmlBasedComponent container = createBaseContentInternal(widget, rootContainer);
		CustomerModel customerModel = this.getCustomer(widget);
		Div content;
		if (customerModel != null)
		{
			CockpitEventAcceptor addNewAddressContainer = this.createAddressNotificationEventAcceptor(widget);
			OrderedConfigurableBrowserArea addNewAddressButton = (OrderedConfigurableBrowserArea) UISessionUtils.getCurrentSession()
					.getCurrentPerspective().getBrowserArea();
			addNewAddressButton.addNotificationListener(widget.getWidgetCode(), addNewAddressContainer);
			AddressModel defaultShipmentAddress = customerModel.getDefaultShipmentAddress();
			Div selectDefaultShipmentAddressContent = new Div();
			selectDefaultShipmentAddressContent.setSclass("defaultShipmentAddressBox");
			selectDefaultShipmentAddressContent.setParent(container);
			Label defaultShipmentAddressLabel = new Label(LabelUtils.getLabel(widget, "defaultShipmentAddress"));
			defaultShipmentAddressLabel.setParent(selectDefaultShipmentAddressContent);
			Listbox defaultShipmentAddressDropdown = new Listbox();
			defaultShipmentAddressDropdown.setParent(selectDefaultShipmentAddressContent);
			defaultShipmentAddressDropdown.setMold("select");
			defaultShipmentAddressDropdown.setRows(1);
			if (defaultShipmentAddress == null)
			{
				Listitem i = new Listitem("", null);
				i.setParent(defaultShipmentAddressDropdown);
				i.setSelected(true);
			}

			createAddressContent(widget, defaultShipmentAddress, defaultShipmentAddressDropdown);

			defaultShipmentAddressDropdown.addEventListener("onSelect",
					this.createDefaultShipmentAddressChangedEventListener(widget));
		}
		else
		{
			content = new Div();
			content.setParent(container);
			Label var17 = new Label(LabelUtils.getLabel(widget, "noResults"));
			var17.setParent(content);
		}

		content = new Div();
		content.setParent(container);
		createAddButtonContent(widget, container, content);
		return container;
	}

	private void createAddressContent(DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			AddressModel defaultShipmentAddress, Listbox defaultShipmentAddressDropdown)
	{
		for (int i = 0; i < widget.getWidgetModel().getItems().size(); ++i)
		{
			TypedObject address = widget.getWidgetModel().getItems().get(i);
			AddressModel addressModel = (AddressModel) address.getObject();
			if (addressModel != null)
			{
				String label = this.getAddressModelLabelProvider().getLabel(address);
				Listitem listItem = new Listitem(label, address);
				listItem.setParent(defaultShipmentAddressDropdown);
				listItem.setSelected(defaultShipmentAddress != null && defaultShipmentAddress.getPk().equals(addressModel.getPk()));
			}
		}
	}

	private void createAddButtonContent(final DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget,
			final HtmlBasedComponent container, Div content)
	{
		Button add = new Button(LabelUtils.getLabel(widget, "addBtn"));
		content.setClass("createNewAddress");
		add.setParent(content);
		add.addEventListener("onClick", new EventListener()
		{
			public void onEvent(Event event) throws Exception
			{
				handleOpenAddressCreateEvent(widget, event, container);
			}
		});
	}


	protected HtmlBasedComponent createBaseContentInternal(
			DefaultListboxWidget<CustomerAddressesListWidgetModel, CustomerController> widget, HtmlBasedComponent rootContainer)
	{
		Div container = new Div();
		container.setSclass("csListboxContainer");
		Listbox listBox = new Listbox();
		listBox.setParent(container);
		widget.setListBox(listBox);
		listBox.setVflex(false);
		listBox.setFixedLayout(true);
		listBox.setSclass("csWidgetListbox");
		this.renderListbox(listBox, widget, rootContainer);
		if (this.isLazyLoadingEnabled())
		{
			UITools.applyLazyload(listBox);
		}

		if (listBox.getItemCount() > 0 && listBox.getSelectedIndex() <= 0)
		{
			listBox.setSelectedIndex(0);
		}

		return container;
	}

	@Override
	protected void renderEditor(org.zkoss.zk.ui.HtmlBasedComponent parent, EditorRowConfiguration rowConfig, TypedObject item,
			PropertyDescriptor propertyDescriptor, Widget widget)
	{
		if (item != null && item.getObject() != null)
		{
			super.renderEditor(parent, rowConfig, item, propertyDescriptor, widget);
		}
	}
}
