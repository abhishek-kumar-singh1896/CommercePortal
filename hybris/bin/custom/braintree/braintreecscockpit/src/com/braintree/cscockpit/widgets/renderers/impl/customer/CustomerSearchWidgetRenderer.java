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

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.model.data.DataObject;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextSearchCommand;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.cscockpit.widgets.models.impl.TextSearchWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractSearcherWidgetRenderer;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.converters.BraintreeCustomerDetailsConverter;
import com.braintree.cscockpit.widgets.services.search.generic.query.DefaultTransactionSearchQueryBuilder;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.Customer;


public class CustomerSearchWidgetRenderer extends AbstractSearcherWidgetRenderer
{
	private BraintreeCustomerDetailsConverter customerDetailsPopulator;
	private CsBrainTreeFacade csBrainTreeFacade;

	@Override
	protected HtmlBasedComponent createSearchPane(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget)
	{
		final Div searchPane = new Div();

		final Textbox customerID = createCustomerIDField(widget, searchPane);
		final Textbox customerEmail = createCustomerEmailField(widget, searchPane);
		final Button searchBtn = createSearchButton(widget, searchPane);

		this.attachSearchEventListener(widget, createSearchEventListener(widget, customerID, customerEmail),
				new AbstractComponent[]
				{ customerID, customerEmail, searchBtn });
		return searchPane;
	}

	protected Textbox createCustomerEmailField(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget, final Div parent)
	{
		final Textbox customerEmail = this.createSearchTextField(widget, parent, "customerEmail",
				DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL, "SearchForCustomer_CustomerEmail_input");
		customerEmail.setConstraint(new Constraint()
		{

			@Override
			public void validate(final Component paramComponent, final Object paramObject) throws WrongValueException
			{
				if (paramObject instanceof String)
				{
					final String email = (String) paramObject;
					if (StringUtils.isNotEmpty(email) && !email.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"))
					{
						throw new WrongValueException(paramComponent, Labels.getLabel("validation.incorrect.email"));
					}
				}
			}
		});

		return customerEmail;
	}

	protected Textbox createCustomerIDField(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget, final Div parent)
	{
		return this.createSearchTextField(widget, parent, "customer", DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID,
				"SearchForCustomer_CustomerID_input");
	}

	protected Button createSearchButton(final ListboxWidget widget, final Div parent)
	{
		return createButton(widget, parent, "searchBtn", "SearchForCustomer_Search_button");
	}

	protected EventListener createSearchEventListener(final ListboxWidget widget, final InputElement customerId,
			final InputElement customerEmail)
	{
		return new CustomerSearchWidgetRenderer.SearchEventListener(widget, customerId, customerEmail);
	}

	@Override
	protected void renderListbox(final Listbox listBox, final ListboxWidget widget, final HtmlBasedComponent rootContainer)
	{
		final TextSearchWidgetModel widgetModel = (TextSearchWidgetModel) widget.getWidgetModel();
		if (widgetModel != null)
		{
			final List items = widgetModel.getItems();
			if (items != null && !items.isEmpty())
			{
				final Listhead headRow = new Listhead();
				headRow.setParent(listBox);

				populateHeaderRow(widget, headRow);

				final Listheader colHeader = new Listheader(LabelUtils.getLabel((Widget) widget, "actions", new Object[0]));
				colHeader.setWidth("80px");
				colHeader.setParent(headRow);
				final Iterator itemsIterator = items.iterator();

				while (itemsIterator.hasNext())
				{
					final DataObject metaItem = (DataObject) itemsIterator.next();
					final Listitem row = new Listitem();
					row.setParent(listBox);
					row.setSclass("csSearchResultRow");

					populateDataRow(widget, row, (Customer) metaItem.getItem());

					addCustomerDetailsButton(widget, metaItem, row);
				}
			}
		}
	}

	private void addCustomerDetailsButton(final ListboxWidget widget, final DataObject metaItem, final Listitem row)
	{
		if (metaItem.getItem() instanceof Customer)
		{
			final EventListener selectItemEventListener = createSelectDetailItemEventListener(widget, metaItem);
			row.addEventListener("onOK", selectItemEventListener);
			UIElementUtils.createAndAttachCustomButton(widget, row, "detailBtn", "btnblue", selectItemEventListener);
		}
	}

	protected EventListener createSelectDetailItemEventListener(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final DataObject metaItem)
	{
		return new CustomerSelectItemEventListener(widget, metaItem);
	}

	protected void handleCustomerSelectItem(
			final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
			final TypedObject item) throws Exception
	{
		this.getItemAppender().add(item, 1L);
	}

	protected void populateDataRow(final Widget widget, final Listitem row, final Customer item)
	{
		if (item != null)
		{
			final Listcell customerIdCell = new Listcell(item.getId());
			final Listcell customerCell = new Listcell(formedName(item));
			final Listcell emailCell = new Listcell(item.getEmail());

			row.appendChild(customerIdCell);
			row.appendChild(customerCell);
			row.appendChild(emailCell);
		}
	}

	private String formedName(final Customer customer)
	{
		final String firstName = customer.getFirstName();
		final String lastName = customer.getLastName();
		if (StringUtils.isNotBlank(firstName))
		{
			if (StringUtils.isNotBlank(lastName))
			{
				return firstName + " " + lastName;
			}
			return firstName;
		}
		return StringUtils.EMPTY;
	}

	protected void populateHeaderRow(final Widget widget, final Listhead row)
	{
		final String[] columns = new String[]
		{ "customerId", "name", "email" };
		for (final String column : columns)
		{
			final Listheader header = new Listheader(LabelUtils.getLabel(widget, column, new Object[0]));
			header.setTooltiptext(column);
			row.appendChild(header);
		}
	}

	/**
	 * @return the customerDetailsPopulator
	 */
	public BraintreeCustomerDetailsConverter getCustomerDetailsPopulator()
	{
		return customerDetailsPopulator;
	}

	/**
	 * @param customerDetailsPopulator
	 *           the customerDetailsPopulator to set
	 */
	public void setCustomerDetailsPopulator(final BraintreeCustomerDetailsConverter customerDetailsPopulator)
	{
		this.customerDetailsPopulator = customerDetailsPopulator;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	protected class SearchEventListener extends AbstractSearchEventListener
	{
		private final transient InputElement customerId;
		private final transient InputElement customerEmail;

		public SearchEventListener(final ListboxWidget widget, final InputElement customerId, final InputElement customerEmail)
		{
			super(widget);
			this.customerId = customerId;
			this.customerEmail = customerEmail;
		}

		@Override
		protected void fillSearchCommand(final DefaultCsTextSearchCommand command)
		{
			command.setText(DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_ID, this.customerId.getText());
			command.setText(DefaultTransactionSearchQueryBuilder.TextField.CUSTOMER_EMAIL, this.customerEmail.getText());
		}
	}

	protected class CustomerSelectItemEventListener implements EventListener
	{
		private final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget;
		private final DataObject metaItem;

		public CustomerSelectItemEventListener(
				final ListboxWidget<TextSearchWidgetModel, SearchCommandController<DefaultCsTextSearchCommand>> widget,
				final DataObject metaItem)
		{
			this.widget = widget;
			this.metaItem = metaItem;
		}

		@Override
		public void onEvent(final Event event) throws Exception
		{
			final Customer customerItem = getCustomer();
			final BraintreeCustomerDetailsModel customer = convertCustomer(customerItem);
			final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customer);
			handleCustomerSelectItem(this.widget, itemTypedObject);

		}

		private BraintreeCustomerDetailsModel convertCustomer(final Customer customerItem)
		{
			if (customerItem != null)
			{
				return getCustomerDetailsPopulator().convert(customerItem);
			}
			return null;
		}

		private Customer getCustomer()
		{
			final Customer customerItem = (Customer) metaItem.getItem();
			final Customer up2dateCustomer = getCsBrainTreeFacade().findCustomer(customerItem.getId());
			if (up2dateCustomer != null)
			{
				return up2dateCustomer;
			}
			return null;
		}
	}
}
