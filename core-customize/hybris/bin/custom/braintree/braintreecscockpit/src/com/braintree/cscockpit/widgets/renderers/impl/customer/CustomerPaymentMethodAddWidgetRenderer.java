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
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.payment.AdapterException;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.strategy.UpdateCustomerStrategy;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.MessageShowUtils;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintree.payment.dto.BraintreeInfo;


public class CustomerPaymentMethodAddWidgetRenderer extends AbstractPaymentMethodWidgetRenderer
{
	private static final Logger LOG = Logger.getLogger(CustomerPaymentMethodAddWidgetRenderer.class);

	//TODO: IP - Move to some configuration
	private static final String BRAINTREE_HOP_PAGE_URL = "/braintree";

	private CsBrainTreeFacade csBrainTreeFacade;
	private ItemAppender<TypedObject> itemAppender;
	private UpdateCustomerStrategy defaultUpdateCustomerStrategy;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent component)
	{
		final Div content = new Div();
		final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
		if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			addPrettyTitle(widget, content, "creditCardValidationHostedFields");
			addIFrame(content);

			final Listbox addresses = UIElementUtils.createCustomerAddressesListBox(widget, content, "addresses",
					(BraintreeCustomerDetailsModel) currentCustomer.getObject());

			final Checkbox defaultPaymentMethod = createCheckBoxField(widget, content, "defaultPaymentMethod", false);

			createButton(widget, content, "createButton",
					getEventListener(defaultPaymentMethod, addresses, (BraintreeCustomerDetailsModel) currentCustomer.getObject()));
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noCustomerSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	protected void addIFrame(final Div content)
	{
		final Iframe iframe = new Iframe(BRAINTREE_HOP_PAGE_URL);

		iframe.setWidth("450px");
		iframe.setHeight("200px");
		iframe.setParent(content);
	}

	private EventListener getEventListener(final Checkbox defaultPaymentMethod, final Listbox addresses,
			final BraintreeCustomerDetailsModel currentCustomer)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				CustomerPaymentMethodAddWidgetRenderer.this.handleCreateEventClickListener(defaultPaymentMethod, currentCustomer,
						addresses);
			}
		};
	}

	private void handleCreateEventClickListener(final Checkbox defaultPaymentMethod,
			final BraintreeCustomerDetailsModel currentCustomer, final Listbox addresses)
	{
		final BraintreeInfo braintreeInfo = getBaintreeInfo();
		final boolean checked = defaultPaymentMethod.isChecked();
		final String customerId = currentCustomer.getId();
		LOG.info(String.format("BT payment Method creation fields: nonce=%s isDefault=%s customerId=%s", braintreeInfo.getNonce(),
				Boolean.valueOf(checked), customerId));

		if (StringUtils.isBlank(braintreeInfo.getNonce()))
		{
			MessageShowUtils.showErrorPaymentMethodNonceRetrieveMessage();
		}
		else
		{
			final String billingAddressId = getSelectedAddress(addresses);
			final BrainTreePaymentMethodResult result = getCsBrainTreeFacade().createCreditCardPaymentMethod(customerId,
					braintreeInfo.getNonce(), braintreeInfo.getCardholderName(), checked, billingAddressId);

			if (result.isSuccess() && result.getPaymentMethod() != null)
			{
				MessageShowUtils.showSuccessCreatePaymentMethodMessage();
				getPopupWidgetHelper().dismissCurrentPopup();
				final BraintreeCustomerDetailsModel updatedCustomer = getDefaultUpdateCustomerStrategy().update(currentCustomer,
						result.getPaymentMethod());
				final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(updatedCustomer);
				getItemAppender().add(itemTypedObject, 1L);
			}
			else
			{
				MessageShowUtils.showErrorCreatePaymentMethodMessage(result);
			}
		}
	}

	private String getSelectedAddress(final Listbox addresses)
	{
		if (addresses != null && addresses.getSelectedItem() != null)
		{
			return (String) addresses.getSelectedItem().getValue();
		}
		return null;
	}

	protected BraintreeInfo getBaintreeInfo()
	{
		return getCsBrainTreeFacade().getBraintreeInfo();
	}

	protected void createButton(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		final Div saveButtonBox = new Div();
		saveButtonBox.setClass("btCustomerDiv");
		saveButtonBox.setParent(component);

		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(saveButtonBox);
		button.addEventListener(Events.ON_CLICK, eventListener);
	}

	private Checkbox createCheckBoxField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final String label, final boolean isDefault)
	{
		final Checkbox checkbox = new Checkbox(LabelUtils.getLabel(widget, label, new Object[0]));
		checkbox.setClass("btCustomerCheckbox");
		checkbox.setChecked(isDefault);
		content.appendChild(checkbox);
		return checkbox;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

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

	public UpdateCustomerStrategy getDefaultUpdateCustomerStrategy()
	{
		return defaultUpdateCustomerStrategy;
	}

	public void setDefaultUpdateCustomerStrategy(final UpdateCustomerStrategy defaultUpdateCustomerStrategy)
	{
		this.defaultUpdateCustomerStrategy = defaultUpdateCustomerStrategy;
	}

	class ResourceLoader
	{
		private final String filePath;

		public ResourceLoader(final String filePath)
		{
			this.filePath = filePath;
		}

		public InputStream getResource()
		{
			final ClassLoader classLoader = this.getClass().getClassLoader();

			final InputStream inputStream = classLoader.getResourceAsStream(filePath);

			if (inputStream == null)
			{
				throw new AdapterException("Not found");
			}

			return inputStream;
		}
	}
}
