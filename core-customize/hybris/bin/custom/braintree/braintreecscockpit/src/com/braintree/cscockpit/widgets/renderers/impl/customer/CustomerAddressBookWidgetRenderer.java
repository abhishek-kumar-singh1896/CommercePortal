/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.cscockpit.widgets.renderers.impl.customer;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_ADDRESS_ASK_MESSAGE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_ADDRESS_ASK_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.strategy.UpdateCustomerStrategy;
import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.DefaultLocaleUtils;
import com.braintree.cscockpit.widgets.renderers.utils.MessageShowUtils;
import com.braintree.model.BraintreeCustomerDetailsModel;


public class CustomerAddressBookWidgetRenderer
		extends
			AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerController>>
{
	private static final Logger LOG = Logger.getLogger(CustomerAddressBookWidgetRenderer.class);

	private static final String HEADER_FIELD_ID = "id";
	private static final String HEADER_FIELD_ADDRESS = "address";
	private static final String HEADER_FIELD_ACTION = "action";

	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
	private UpdateCustomerStrategy defaultUpdateCustomerStrategy;
	private ItemAppender<TypedObject> itemAppender;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent arg1)
	{
		final Div content = new Div();
		final Listbox listBox = new Listbox();

		final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
		if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			final List items = ((BraintreeCustomerDetailsModel) currentCustomer.getObject()).getAddresses();
			if (items != null && !items.isEmpty())
			{
				final Listhead headRow = new Listhead();
				headRow.setParent(listBox);

				populateHeaderRow(widget, headRow);

				final Iterator itemsIterator = items.iterator();

				while (itemsIterator.hasNext())
				{
					final AddressModel item = (AddressModel) itemsIterator.next();
					final Listitem row = new Listitem();
					row.setParent(listBox);
					row.setSclass("csSearchResultRow");
					populateDataRow(widget, row, item, content, (BraintreeCustomerDetailsModel) currentCustomer.getObject());
				}
			}
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noCustomerSelected", new Object[0]));
			dummyLabel.setParent(content);
		}

		content.appendChild(listBox);
		return content;
	}

	protected void populateHeaderRow(final Widget widget, final Listhead row)
	{
		final String[] columns =
		{HEADER_FIELD_ID, HEADER_FIELD_ADDRESS, HEADER_FIELD_ACTION};
		for (final String column : columns)
		{
			final Listheader header = new Listheader(LabelUtils.getLabel(widget, column, new Object[0]));
			header.setTooltiptext(column);
			if (HEADER_FIELD_ID.equals(column))
			{
				header.setWidth("355px");
			}
			else if (HEADER_FIELD_ACTION.equals(column))
			{
				header.setWidth("200px");
			}
			row.appendChild(header);
		}
	}

	protected void populateDataRow(final Widget widget, final Listitem row, final AddressModel item, final Div content,
			final BraintreeCustomerDetailsModel customer)
	{
		if (item != null)
		{
			final Listcell idCell = new Listcell(item.getBrainTreeAddressId());
			final Listcell addressCell = new Listcell();
			fillInAddressCell(item, addressCell);
			final Listcell actionCell = generateActionCell(widget, item, content, customer);

			row.appendChild(idCell);
			row.appendChild(addressCell);
			row.appendChild(actionCell);
		}
	}

	private Listcell generateActionCell(final Widget widget, final AddressModel item, final Div content,
			final BraintreeCustomerDetailsModel customer)
	{
		final Listcell cell = new Listcell();
		final Button editButton = createEditButton(widget, "editAddress", "defaultCsEditBtAddressWidgetConfig",
				"csDetailsBtPaymentMethod-Popup", "csDetailsPaymentMethod", "popup.address", false, item, content);
		final Button deleteButton = createDeleteButton(widget, "deleteAddress", false, item, customer);
		cell.appendChild(editButton);
		cell.appendChild(deleteButton);

		return cell;
	}

	private Button createEditButton(final Widget widget, final String buttonLabelName, final String springWidgetName,
			final String popupCode, final String cssClass, final String popupTitleLabelName, final boolean disabled,
			final AddressModel item, final Div content)
	{
		final EventListener eventListener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleEditButtonClickEvent(widget, springWidgetName, popupCode, cssClass, popupTitleLabelName, item, content);
			}
		};
		return createButton(widget, buttonLabelName, eventListener, disabled);
	}

	private Button createDeleteButton(final Widget widget, final String buttonLabelName, final boolean disabled,
			final AddressModel item, final BraintreeCustomerDetailsModel customer)
	{
		final EventListener eventListener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleDeleteButtonClickEvent(item, customer);
			}
		};
		return createButton(widget, buttonLabelName, eventListener, disabled);
	}

	private Button createButton(final Widget widget, final String buttonLabelName, final EventListener eventListener,
			final boolean disabled)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setDisabled(disabled);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
		return button;
	}

	protected void handleEditButtonClickEvent(final Widget widget, final String springWidgetName, final String popupCode,
			final String cssClass, final String popupTitleLabelName, final AddressModel item, final Div content)
	{
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(item);
		((CustomerController) widget.getWidgetController()).setCurrentAddress(itemTypedObject);
		this.getPopupWidgetHelper().createPopupWidget(content, springWidgetName, popupCode, cssClass,
				LabelUtils.getLabel(widget, popupTitleLabelName, new Object[0]), 400);
	}

	private void handleDeleteButtonClickEvent(final AddressModel addressModel, final BraintreeCustomerDetailsModel customer)
	{
		try
		{
			Messagebox.show(getLabel(WIDGET_REMOVE_ADDRESS_ASK_MESSAGE), getLabel(WIDGET_REMOVE_ADDRESS_ASK_TITLE), Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION, new EventListener()
			{
				@Override
				public void onEvent(final Event evt) throws InterruptedException
				{
					if ("onOK".equals(evt.getName()))
					{
						removeAddress(addressModel, customer);
					}
				}
			});
		}
		catch (final InterruptedException exception)
		{
			LOG.debug("Errors occurred while showing message box!", exception);
		}
	}

	private void removeAddress(final AddressModel addressModel, final BraintreeCustomerDetailsModel customer)
	{
		final BrainTreeAddressResult result = getCsBrainTreeFacade().removeAddress(addressModel.getBrainTreeAddressId(),
				customer.getId());
		if (result.isSuccess())
		{
			MessageShowUtils.showSuccessRemoveAddressMessage();
			final BraintreeCustomerDetailsModel customerDetailsModel = getDefaultUpdateCustomerStrategy().update(customer,
					addressModel.getBrainTreeAddressId());
			final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customerDetailsModel);
			getItemAppender().add(itemTypedObject, 1L);
		}
		else
		{
			MessageShowUtils.showErrorRemoveAddressMessage(result);
		}
	}

	private void fillInAddressCell(final AddressModel item, final AbstractComponent cell)
	{
		final Div nameSpan = new Div();
		nameSpan.appendChild(new Label(getFullName(item.getFirstname(), item.getLastname())));
		cell.appendChild(nameSpan);
		final Div companySpan = new Div();
		companySpan.appendChild(new Label(item.getCompany()));
		cell.appendChild(companySpan);

		final Div streetAddressSpan = new Div();
		streetAddressSpan.appendChild(new Label(item.getStreetname()));
		cell.appendChild(streetAddressSpan);

		final Div extendedAddressSpan = new Div();
		extendedAddressSpan.appendChild(new Label(item.getStreetnumber()));
		cell.appendChild(extendedAddressSpan);

		final Div localitySpan = new Div();
		localitySpan.appendChild(new Label(item.getTown()));
		cell.appendChild(localitySpan);

		final Div regionSpan = new Div();
		regionSpan.appendChild(new Label(item.getRegion() != null ? DefaultLocaleUtils.getRegionName(item.getRegion()) : item
				.getZone()));
		cell.appendChild(regionSpan);

		final Div postalCodeSpan = new Div();
		postalCodeSpan.appendChild(new Label(item.getPostalcode()));
		cell.appendChild(postalCodeSpan);

		final Div countryNameSpan = new Div();
		countryNameSpan.appendChild(new Label(DefaultLocaleUtils.getCountryName(item.getCountry())));
		cell.appendChild(countryNameSpan);
	}

	private String getFullName(final String firstName, final String lastName)
	{
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(firstName))
		{
			sb.append(firstName).append(" ");
		}
		if (StringUtils.isNotBlank(lastName))
		{
			sb.append(lastName);
		}
		return sb.toString();
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public UpdateCustomerStrategy getDefaultUpdateCustomerStrategy()
	{
		return defaultUpdateCustomerStrategy;
	}

	public void setDefaultUpdateCustomerStrategy(final UpdateCustomerStrategy defaultUpdateCustomerStrategy)
	{
		this.defaultUpdateCustomerStrategy = defaultUpdateCustomerStrategy;
	}

	public ItemAppender<TypedObject> getItemAppender()
	{
		return itemAppender;
	}

	public void setItemAppender(final ItemAppender<TypedObject> itemAppender)
	{
		this.itemAppender = itemAppender;
	}
}
