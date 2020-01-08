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

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_PAYMENT_METHOD_ASK_MESSAGE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_PAYMENT_METHOD_ASK_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.jalo.JaloSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.MessageShowUtils;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.model.BraintreeCustomerDetailsModel;


public class CustomerPaymentMethodsWidgetRenderer extends
		AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerController>>
{
	private static final Logger LOG = Logger.getLogger(CustomerPaymentMethodsWidgetRenderer.class);

	private static final String HEADER_FIELD_TOKEN = "token";
	private static final String HEADER_FIELD_PAYMENT_METHOD = "payment.method";
	private static final String HEADER_FIELD_LAST_USED = "last.used";
	private static final String HEADER_FIELD_ACTIONS = "actions";

	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
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
			final List<BrainTreePaymentInfoModel> items = ((BraintreeCustomerDetailsModel) currentCustomer.getObject())
					.getPaymentMethods();
			if (items != null && !items.isEmpty())
			{
				final Listhead headRow = new Listhead();
				headRow.setParent(listBox);

				populateHeaderRow(widget, headRow);

				final Listheader header = new Listheader(LabelUtils.getLabel((Widget) widget, HEADER_FIELD_ACTIONS, new Object[0]));
				header.setWidth("200px");
				header.setParent(headRow);

				final Iterator itemsIterator = items.iterator();
				while (itemsIterator.hasNext())
				{
					final BrainTreePaymentInfoModel item = (BrainTreePaymentInfoModel) itemsIterator.next();
					final Listitem row = new Listitem();
					row.setParent(listBox);
					row.setSclass("csSearchResultRow");

					populateDataRow(widget, row, item);

					addButtons(widget, item, row, content);
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

	private void addButtons(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final BrainTreePaymentInfoModel item, final Listitem row, final Div content)
	{
		final Listcell actionCell = new Listcell();
		actionCell.setParent(row);

		this.createButton(widget, actionCell, "detailsPaymentMethod", "defaultCsDetailsPaymentMethodWidgetConfig",
				"csDetailsBtPaymentMethod-Popup", "csDetailsPaymentMethod", "popup.detailsPaymentMethodCreate", false, item, content);
		this.createButton(widget, actionCell, "updatePaymentMethod", "defaultCsUpdatePaymentMethodWidgetConfig",
				"csDetailsBtUpdatePaymentMethod-Popup", "csUpdatePaymentMethod", "popup.updatePaymentMethodCreate", false, item,
				content);
		this.createDeleteButton(widget, actionCell, "deletePaymentMethod", false, item);
	}

	protected void createButton(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent container, final String buttonLabelName, final String springWidgetName, final String popupCode,
			final String cssClass, final String popupTitleLabelName, final boolean disabled, final BrainTreePaymentInfoModel item,
			final Div content)
	{
		final EventListener eventListener = new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				handleDetailsButtonClickEvent(widget, springWidgetName, popupCode, cssClass, popupTitleLabelName, item, content);
			}
		};
		this.createButton(widget, container, buttonLabelName, eventListener, disabled);
	}

	protected void createDeleteButton(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent container, final String buttonLabelName, final boolean disabled,
			final BrainTreePaymentInfoModel item)
	{
		final EventListener eventListener = new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				handleDeleteButtonClickEvent(widget, item);
			}
		};
		this.createButton(widget, container, buttonLabelName, eventListener, disabled);
	}

	private void createButton(final Widget<CustomerItemWidgetModel, CustomerController> widget, final HtmlBasedComponent parent,
			final String buttonLabelName, final EventListener eventListener, final boolean disabled)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(parent);
		button.setDisabled(disabled);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	protected void handleDetailsButtonClickEvent(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final String springWidgetName, final String popupCode, final String cssClass, final String popupTitleLabelName,
			final BrainTreePaymentInfoModel item, final Div content)
	{
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(item);
		widget.getWidgetController().setCurrentPaymentMethod(itemTypedObject);
		this.getPopupWidgetHelper().createPopupWidget(content, springWidgetName, popupCode, cssClass,
				LabelUtils.getLabel(widget, popupTitleLabelName, new Object[0]), 400);
	}


	protected void handleDeleteButtonClickEvent(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final BrainTreePaymentInfoModel item)
	{
		try
		{
			Messagebox.show(getLabel(WIDGET_REMOVE_PAYMENT_METHOD_ASK_MESSAGE), getLabel(WIDGET_REMOVE_PAYMENT_METHOD_ASK_TITLE),
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if ("onOK".equals(evt.getName()))
							{
								removePaymentMethod(item, widget);
							}
						}
					});
		}
		catch (final InterruptedException exception)
		{
			LOG.debug("Errors occurred while showing message box!", exception);
		}
	}

	private void removePaymentMethod(final BrainTreePaymentInfoModel item,
			final Widget<CustomerItemWidgetModel, CustomerController> widget)
	{
		final BrainTreePaymentMethodResult result = getCsBrainTreeFacade().deletePaymentMethod(item);

		if (result.isSuccess())
		{
			final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
			final BraintreeCustomerDetailsModel customerDetailsModel = (BraintreeCustomerDetailsModel) currentCustomer.getObject();
			final List<BrainTreePaymentInfoModel> items = customerDetailsModel.getPaymentMethods();
			final Iterator itemsIterator = items.iterator();

			while (itemsIterator.hasNext())
			{
				final BrainTreePaymentInfoModel itm = (BrainTreePaymentInfoModel) itemsIterator.next();
				if (item.getPaymentMethodToken().equals(itm.getPaymentMethodToken()))
				{
					itemsIterator.remove();
				}
			}
			MessageShowUtils.showSuccessRemovePaymentMethodMessage();
			final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customerDetailsModel);
			getItemAppender().add(itemTypedObject, 1L);
		}
		else
		{
			MessageShowUtils.showErrorRemovePaymentMethodMessage(result);
		}
	}

	protected void populateHeaderRow(final Widget widget, final Listhead row)
	{
		final String[] columns = new String[]
		{ HEADER_FIELD_TOKEN, HEADER_FIELD_PAYMENT_METHOD, HEADER_FIELD_LAST_USED };
		for (final String column : columns)
		{
			final Listheader header = new Listheader(LabelUtils.getLabel(widget, column, new Object[0]));
			if (HEADER_FIELD_TOKEN.equals(column))
			{
				header.setWidth("355px");
			}
			header.setTooltiptext(column);
			row.appendChild(header);
		}
	}

	protected void populateDataRow(final Widget widget, final Listitem row, final BrainTreePaymentInfoModel item)
	{
		if (item != null)
		{
			final Listcell tokenCell = new Listcell();
			final Label label = new Label(item.getPaymentMethodToken());
			label.setStyle("color: #2DBB6E");
			tokenCell.appendChild(label);
			if (item.isIsDefault())
			{
				final Label aDefault = new Label("Default");
				aDefault.setStyle("font-size: 85%");
				tokenCell.appendChild(aDefault);
			}
			final Listcell paymentMethodCell = new Listcell();
			final Image paymentLogo = new Image(item.getImageSource());
			paymentLogo.setStyle("width: 28px");
			paymentMethodCell.appendChild(paymentLogo);
			paymentMethodCell.appendChild(new Label(item.getPaymentInfo()));
			final Listcell lastUsedCell = new Listcell(formatDate(item.getUpdatedAt()));

			row.appendChild(tokenCell);
			row.appendChild(paymentMethodCell);
			row.appendChild(lastUsedCell);
		}
	}

	private String formatDate(final Date date)
	{
		if (date != null)
		{
			final Locale userLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();
			final DateFormat dateFormat = new SimpleDateFormat(BraintreeConstants.TRANSACTION_SEARCH_DATE_FORMAT,
					userLocale);
			return dateFormat.format(date);
		}
		return StringUtils.EMPTY;
	}

	public PopupWidgetHelper getPopupWidgetHelper()
	{
		return popupWidgetHelper;
	}

	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	/**
	 * @return the csBrainTreeFacade
	 */
	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	/**
	 * @param csBrainTreeFacade
	 *           the csBrainTreeFacade to set
	 */
	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
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
