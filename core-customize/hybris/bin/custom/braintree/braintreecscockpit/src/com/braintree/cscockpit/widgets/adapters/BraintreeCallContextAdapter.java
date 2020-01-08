package com.braintree.cscockpit.widgets.adapters;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.browsers.WidgetBrowserModel;
import de.hybris.platform.cscockpit.widgets.adapters.CallContextAdapter;
import de.hybris.platform.cscockpit.widgets.events.CallContextEvent;

import com.braintree.cscockpit.widgets.controllers.BraintreeCallContextController;
import com.braintree.cscockpit.widgets.models.impl.BraintreeCallContextWidgetModel;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;


public class BraintreeCallContextAdapter extends CallContextAdapter
{
	private WidgetBrowserModel transactionBrowserModel;
	private WidgetBrowserModel btCustomerBrowserModel;


	@SuppressWarnings(value =
	{ "DMI_UNSUPPORTED_METHOD" }, justification = "because I know better")
	protected WidgetBrowserModel getTransactionBrowserModel()
	{
		if (this.transactionBrowserModel == null)
		{
			this.transactionBrowserModel = this.lookupTransactionBrowserModel();
		}
		return this.transactionBrowserModel;
	}

	@SuppressWarnings(value =
	{ "DMI_UNSUPPORTED_METHOD" }, justification = "because I know better")
	protected WidgetBrowserModel getBtCustomerBrowserModel()
	{
		if (this.btCustomerBrowserModel == null)
		{
			this.btCustomerBrowserModel = this.lookupBtCustomerBrowserModel();
		}
		return this.btCustomerBrowserModel;
	}

	protected WidgetBrowserModel lookupTransactionBrowserModel()
	{
		throw new UnsupportedOperationException(
				"please override BraintreeCallContextAdapter.lookupTransactionBrowserModel() or use <lookup-method>");
	}

	protected WidgetBrowserModel lookupBtCustomerBrowserModel()
	{
		throw new UnsupportedOperationException(
				"please override BraintreeCallContextAdapter.lookupTransactionBrowserModel() or use <lookup-method>");
	}

	@Override
	public void onCockpitEvent(final CockpitEvent cockpitEvent)
	{
		if (cockpitEvent instanceof CallContextEvent && !this.equals(cockpitEvent.getSource()))
		{
			boolean changed = false;
			final TypedObject currentSite = this.getWidgetController().getCurrentSite();
			final boolean siteChanged = this.getWidgetModel().setBaseSite(currentSite);
			changed |= siteChanged;
			final TypedObject currentCustomer = this.getWidgetController().getCurrentCustomer();
			final boolean customerChanged = this.getWidgetModel().setCustomer(currentCustomer);
			changed |= customerChanged;
			final TypedObject currentTransaction = ((BraintreeCallContextController) this.getWidgetController()).getCurrentTransaction();
			final boolean transactionChanged = ((BraintreeCallContextWidgetModel) this.getWidgetModel()).setTransaction(currentTransaction);
			changed |= transactionChanged;
			final TypedObject currentBtCustomer = ((BraintreeCallContextController) this.getWidgetController()).getCurrentBTCustomer();
			final boolean btCustomerChanged = ((BraintreeCallContextWidgetModel) this.getWidgetModel()).setBtCustomer(currentBtCustomer);
			changed |= btCustomerChanged;
			final TypedObject currentOrder = this.getWidgetController().getCurrentOrder();
			final boolean orderChanged = this.getWidgetModel().setOrder(currentOrder);
			changed |= orderChanged;
			final TypedObject currentTicket = this.getWidgetController().getCurrentTicket();
			final boolean ticketChanged = this.getWidgetModel().setTicket(currentTicket);
			changed |= ticketChanged;

			changed |= this.getWidgetModel().setCallContextCurrency(this.getWidgetController().getCallContextCurrency());
			if (changed)
			{
				this.setBrowserState(this.getCustomerBrowserModel(), currentCustomer != null);
				this.setBrowserState(this.getOrderBrowserModel(), currentOrder != null);
				this.setBrowserState(this.getTicketBrowserModel(), currentTicket != null);
				this.setBrowserState(this.getTransactionBrowserModel(), currentTransaction != null);
				this.setBrowserState(this.getBtCustomerBrowserModel(), currentBtCustomer != null);
				if (customerChanged)
				{
					this.setBrowserState(this.getCheckoutBrowserModel(), false);
				}

				if (currentTicket != null && ticketChanged)
				{
					this.getWidgetHelper().focusWidget(this.getTicketBrowserModel().getBrowserCode(), "ticketDetailsWidget");
				}
				else if (currentOrder != null && orderChanged)
				{
					this.getWidgetHelper().focusWidget(this.getOrderBrowserModel().getBrowserCode(), "orderDetailsWidget");
				}
				else if (currentCustomer != null && customerChanged)
				{
					this.getWidgetHelper().focusWidget(this.getCustomerBrowserModel().getBrowserCode(), "customerEditWidget");
				}
				else if (currentTransaction != null && transactionChanged)
				{
					this.getWidgetHelper().focusWidget(this.getTransactionBrowserModel().getBrowserCode(), "transactionDetailsWidget");
				}
				else if (currentBtCustomer != null && btCustomerChanged)
				{
					this.getWidgetHelper().focusWidget(this.getBtCustomerBrowserModel().getBrowserCode(), "btCustomerDetailsWidget");
				}

				this.getWidgetModel().notifyListeners();
			}
		}

	}

}
