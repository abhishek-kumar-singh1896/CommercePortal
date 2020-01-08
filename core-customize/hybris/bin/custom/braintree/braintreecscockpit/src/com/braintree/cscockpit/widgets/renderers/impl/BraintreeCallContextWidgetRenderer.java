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
package com.braintree.cscockpit.widgets.renderers.impl;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.session.impl.OrderedConfigurableBrowserArea;
import de.hybris.platform.cscockpit.utils.CssUtils;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.models.impl.CallContextWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CallContextWidgetRenderer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

import com.braintree.cscockpit.widgets.services.security.BraintreeAccessRightsService;


public class BraintreeCallContextWidgetRenderer extends CallContextWidgetRenderer
{
	private static final int TRANSACTION_WIDGET_WIDTH = 1050;
	private static final int NEW_TRANSACTION_WIDGET_WIDTH = 460;
	private static final int CUSTOMER_WIDGET_WIDTH = 700;

	private BraintreeAccessRightsService braintreeAccessRightsService;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CallContextWidgetModel, CallContextController> widget,
			final HtmlBasedComponent rootContainer)
	{
		final CockpitEventAcceptor addressNotificationEventAcceptor = this.createAddressNotificationEventAcceptor(widget);
		final OrderedConfigurableBrowserArea browserArea = (OrderedConfigurableBrowserArea) UISessionUtils.getCurrentSession()
				.getCurrentPerspective().getBrowserArea();
		browserArea.addNotificationListener(widget.getWidgetCode(), addressNotificationEventAcceptor);
		final Div content = new Div();
		content.appendChild(createSiteContent(widget));
		content.appendChild(createCustomerContent(widget));
		content.appendChild(createOrderContent(widget));

		createBraintreeContent(content, widget);

		content.appendChild(createTicketContent(widget));
		content.appendChild(createCurrencyContent(widget));
		content.appendChild(createEndCallContent(widget));
		return content;
	}

	protected void createBraintreeContent(final Component parent,
			final Widget<CallContextWidgetModel, CallContextController> widget)
	{
		if (isBraintreeActionsAllowed())
		{
			createBraintreeTransactionContent(parent, widget);
			createBraintreeCustomerContent(parent, widget);
		}
	}

	protected boolean isBraintreeActionsAllowed()
	{
		return getBraintreeAccessRightsService().isBraintreeActionsAllowed();
	}

	protected void createBraintreeTransactionContent(final Component parent,
			final Widget<CallContextWidgetModel, CallContextController> widget)
	{
		final Div container = new Div();
		parent.appendChild(container);

		container.setClass("csCallContextContainer");
		final Div transactionInfoContainer = new Div();
		transactionInfoContainer.setParent(container);

		final Label customerLabel = new Label(LabelUtils.getLabel(widget, "braintree", new Object[0]));
		customerLabel.setSclass(CssUtils.combine("infoLabel"));
		customerLabel.setParent(transactionInfoContainer);

		findTransactionContent(widget, container);
		newTransactionContent(widget, container);
		newTransactionByTokenContent(widget, container);
	}

	protected void createBraintreeCustomerContent(final Component parent,
			final Widget<CallContextWidgetModel, CallContextController> widget)
	{
		if (getBraintreeAccessRightsService().canFindCustomer())
		{
			final Div container = new Div();
			parent.appendChild(container);

			container.setClass("csCallContextContainer");
			final Div customerInfoContainer = new Div();
			customerInfoContainer.setParent(container);

			final Toolbarbutton searchBraintreeCustomerBtn = new Toolbarbutton(
					LabelUtils.getLabel(widget, "findBraintreeCustomer", new Object[0]));
			searchBraintreeCustomerBtn.setParent(container);
			searchBraintreeCustomerBtn.setSclass(CssUtils.combine("csCallContextContainerPopupBtn", "blueLink"));
			searchBraintreeCustomerBtn.addEventListener("onClick", new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception
				{
					handleOpenBraintreeCustomerSearchEvent(widget, event, container);
				}
			});
		}
	}

	private void findTransactionContent(final Widget<CallContextWidgetModel, CallContextController> widget, final Div container)
	{
		if (getBraintreeAccessRightsService().canFindTransaction())
		{
			final Toolbarbutton searchBraintreeTransactionBtn = new Toolbarbutton(
					LabelUtils.getLabel(widget, "findTransaction", new Object[0]));
			searchBraintreeTransactionBtn.setParent(container);
			searchBraintreeTransactionBtn.setSclass(CssUtils.combine("csCallContextContainerPopupBtn", "blueLink"));
			searchBraintreeTransactionBtn.addEventListener("onClick", new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception
				{
					handleOpenBraintreeTransactionSearchEvent(widget, event, container);
				}
			});
		}
	}

	private void newTransactionContent(final Widget<CallContextWidgetModel, CallContextController> widget, final Div container)
	{
		if (getBraintreeAccessRightsService().canCreateNewTransaction())
		{
			final Toolbarbutton newTransactionBtn = new Toolbarbutton(
					LabelUtils.getLabel(widget, "createTransaction", new Object[0]));
			newTransactionBtn.setParent(container);
			newTransactionBtn.setSclass(CssUtils.combine("csCallContextContainerPopupBtn", "blueLink"));
			newTransactionBtn.addEventListener("onClick", new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception
				{
					handleOpenCreateBraintreeTransactionEvent(widget, event, container);
				}
			});
		}
	}

	private void newTransactionByTokenContent(final Widget<CallContextWidgetModel, CallContextController> widget,
			final Div container)
	{
		if (getBraintreeAccessRightsService().canCreateNewTransactionByVault())
		{
			final Toolbarbutton newTransactionBtn = new Toolbarbutton(
					LabelUtils.getLabel(widget, "createByTokenTransaction", new Object[0]));
			newTransactionBtn.setParent(container);
			newTransactionBtn.setSclass(CssUtils.combine("csCallContextContainerPopupBtn", "blueLink"));
			newTransactionBtn.addEventListener("onClick", new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception
				{
					handleOpenCreateBraintreeTransactionByTokenEvent(widget, event, container);
				}
			});
		}
	}


	protected void handleOpenBraintreeTransactionSearchEvent(final Widget widget, final Event event, final Div container)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, "csBraintreeTransactionSearchWidgetConfig",
				"csBraintreeTransactionSearchWidget-Popup", "csBraintreeTransactionSearchPopup",
				LabelUtils.getLabel(widget, "popup.btTransactionSearchTitle", new Object[0]), TRANSACTION_WIDGET_WIDTH);
	}

	private void handleOpenBraintreeCustomerSearchEvent(final Widget widget, final Event event, final Div container)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, "csBraintreeCustomerSearchWidgetConfig",
				"csBraintreeCustomerSearchWidget-Popup", "csBraintreeCustomerSearchPopup",
				LabelUtils.getLabel(widget, "popup.btCustomerSearchTitle", new Object[0]), CUSTOMER_WIDGET_WIDTH);
	}

	private void handleOpenCreateBraintreeTransactionEvent(final Widget<CallContextWidgetModel, CallContextController> widget,
			final Event event, final Div container)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, "defaultCsNewTransactionWidgetConfig",
				"csBraintreeNewTransactionWidget-Popup", "csBraintreeNewTransactionPopup",
				LabelUtils.getLabel(widget, "popup.btNewTransactionTitle", new Object[0]), NEW_TRANSACTION_WIDGET_WIDTH);
	}

	private void handleOpenCreateBraintreeTransactionByTokenEvent(
			final Widget<CallContextWidgetModel, CallContextController> widget, final Event event, final Div container)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, "defaultCsNewTransactionByTokenWidgetConfig",
				"csBraintreeNewTransactionWidget-Popup", "csBraintreeNewTransactionPopup",
				LabelUtils.getLabel(widget, "popup.btNewTransactionTitle", new Object[0]), NEW_TRANSACTION_WIDGET_WIDTH);
	}

	/**
	 * @return the braintreeAccessRightsService
	 */
	public BraintreeAccessRightsService getBraintreeAccessRightsService()
	{
		return braintreeAccessRightsService;
	}

	/**
	 * @param braintreeAccessRightsService
	 *           the braintreeAccessRightsService to set
	 */
	public void setBraintreeAccessRightsService(final BraintreeAccessRightsService braintreeAccessRightsService)
	{
		this.braintreeAccessRightsService = braintreeAccessRightsService;
	}
}
