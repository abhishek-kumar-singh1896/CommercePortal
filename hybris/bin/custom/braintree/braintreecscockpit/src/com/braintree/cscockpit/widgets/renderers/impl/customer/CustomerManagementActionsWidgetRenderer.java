package com.braintree.cscockpit.widgets.renderers.impl.customer;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_CUSTOMER_ASK_MESSAGE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_REMOVE_CUSTOMER_ASK_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.converters.BraintreeCustomerDetailsConverter;
import com.braintree.cscockpit.widgets.controllers.CustomerManagementActionWidgetController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.MessageShowUtils;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.Customer;


public class CustomerManagementActionsWidgetRenderer extends
		AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController>>
{
	private static final Logger LOG = Logger.getLogger(CustomerManagementActionsWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;
	private CsBrainTreeFacade csBrainTreeFacade;
	private BraintreeCustomerDetailsConverter customerDetailsPopulator;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget,
			final HtmlBasedComponent rootContainer)
	{
		final Div component = new Div();
		component.setSclass("orderManagementActionsWidget");

		this.createButton(widget, component, "refreshCustomer", createRefreshListener(widget), !widget.getWidgetController()
				.isRefreshPossible());
		this.createButton(widget, component, "editBtCustomer", "defaultCsEditBtCustomerWidgetConfig", "csEditBtCustomer-Popup",
				"cseEitBtCustomer", "popup.editBtCustomerCreate", !widget.getWidgetController().isEditPossible());
		this.createButton(widget, component, "deleteCustomer", createRemoveEventListener(widget), !widget.getWidgetController()
				.isDeletePossible());
		this.createButton(widget, component, "addPaymentMethod", "defaultCsAddBtPaymentMethodWidgetConfig",
				"csAddPaymentMethod-Popup", "csAddPaymentMethod", "popup.addPaymentMethodCreate", !widget.getWidgetController()
						.isAddPaymentTokenPossible());
		this.createButton(widget, component, "addAddress", "defaultCsCreateBtAddressWidgetConfig", "csAddAddress-Popup",
				"csAddAddress", "popup.address", !widget.getWidgetController().isAddAddressPossible());
		return component;
	}

	protected void createButton(final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget,
			final Div container, final String buttonLabelName, final String springWidgetName, final String popupCode,
			final String cssClass, final String popupTitleLabelName, final boolean disabled)
	{
		final EventListener eventListener = new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				CustomerManagementActionsWidgetRenderer.this.handleButtonClickEvent(widget, event, container, springWidgetName,
						popupCode, cssClass, popupTitleLabelName);
			}
		};
		this.createButton(widget, container, buttonLabelName, eventListener, disabled);
	}

	private void createButton(final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener,
			final boolean disabled)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(component);
		button.setDisabled(disabled);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	protected void handleButtonClickEvent(final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget,
			final Event event, final Div container, final String springWidgetName, final String popupCode, final String cssClass,
			final String popupTitleLabelName)
	{
		this.getPopupWidgetHelper().createPopupWidget(container, springWidgetName, popupCode, cssClass,
				LabelUtils.getLabel(widget, popupTitleLabelName, new Object[0]), 400);
	}

	private EventListener createRefreshListener(
			final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				final Customer customerItem = getCustomer(widget);
				final BraintreeCustomerDetailsModel customer = convertCustomer(customerItem);
				final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customer);

				widget.getWidgetController().setBtCustomer(itemTypedObject);
				widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
			}
		};
	}

	private BraintreeCustomerDetailsModel convertCustomer(final Customer customerItem)
	{
		if (customerItem != null)
		{
			return getCustomerDetailsPopulator().convert(customerItem);
		}
		return null;
	}

	private Customer getCustomer(final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget)
	{
		final String customerId = ((BraintreeCustomerDetailsModel) widget.getWidgetController().getBtCustomer().getObject())
				.getId();
		final Customer up2dateCustomer = getCsBrainTreeFacade().findCustomer(customerId);
		if (up2dateCustomer != null)
		{
			return up2dateCustomer;
		}
		return null;
	}

	private EventListener createRemoveEventListener(
			final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				CustomerManagementActionsWidgetRenderer.this.handleRemoveButtonClickEvent(widget, event);
			}
		};
	}

	private void handleRemoveButtonClickEvent(
			final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget, final Event event)
	{
		try
		{
			Messagebox.show(getLabel(WIDGET_REMOVE_CUSTOMER_ASK_MESSAGE), getLabel(WIDGET_REMOVE_CUSTOMER_ASK_TITLE), Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION, new EventListener()
			{
				public void onEvent(final Event evt) throws InterruptedException
				{
					if ("onOK".equals(evt.getName()))
					{
						removeCustomer(widget);
					}
				}
			});
		}
		catch (final InterruptedException exception)
		{
			LOG.debug("Errors occurred while showing message box!", exception);
		}
	}

	private void removeCustomer(final Widget<CustomerItemWidgetModel, CustomerManagementActionWidgetController> widget)
	{
		final TypedObject currentCustomer = widget.getWidgetController().getBtCustomer();
		if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			final BraintreeCustomerDetailsModel customer = (BraintreeCustomerDetailsModel) currentCustomer.getObject();
			final BrainTreeResponseResultData result = getCsBrainTreeFacade().removeCustomer(customer.getId());
			if (result.isSuccess())
			{
				MessageShowUtils.showSuccessRemoveCustomerMessage();
				widget.getWidgetController().removeBTCustomer();
				widget.getWidgetController().dispatchEvent((String) null, widget, (Map) null);
			}
			else
			{
				MessageShowUtils.showErrorRemoveCustomerMessage(result);
			}
		}
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

}
