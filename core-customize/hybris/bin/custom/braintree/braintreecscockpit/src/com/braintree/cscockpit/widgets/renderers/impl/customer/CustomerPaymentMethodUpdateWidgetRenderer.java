package com.braintree.cscockpit.widgets.renderers.impl.customer;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.data.BraintreePaymentMethodData;
import com.braintree.cscockpit.data.BraintreePaymentMethodInfo;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.cscockpit.strategy.UpdateCustomerStrategy;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.DefaultLocaleUtils;
import com.braintree.cscockpit.widgets.renderers.utils.MessageShowUtils;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.enums.BrainTreePaymentMethod;
import com.braintree.model.BrainTreePaymentInfoModel;
import com.braintree.model.BraintreeCustomerDetailsModel;


public class CustomerPaymentMethodUpdateWidgetRenderer extends AbstractPaymentMethodWidgetRenderer
{
	private CsBrainTreeFacade csBrainTreeFacade;
	private UpdateCustomerStrategy defaultUpdateCustomerStrategy;
	private ItemAppender<TypedObject> itemAppender;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();
		final TypedObject currentPaymentMethod = widget.getWidgetController().getCurrentPaymentMethod();
		if (currentPaymentMethod != null && currentPaymentMethod.getObject() instanceof BrainTreePaymentInfoModel)
		{
			final BrainTreePaymentInfoModel paymentMethod = (BrainTreePaymentInfoModel) currentPaymentMethod.getObject();

			if (BrainTreePaymentMethod.PAYPAL.toString().equals(paymentMethod.getPaymentProvider()))
			{
				final Textbox paymentMethodToken = createTextField(widget, content, "paymentMethodToken",
						paymentMethod.getPaymentMethodToken());
				final Checkbox defaultPaymentMethod = createCheckBoxField(widget, content, "defaultPaymentMethod",
						paymentMethod.isIsDefault());
				createUpdateButton(widget, content, paymentMethodToken, defaultPaymentMethod, paymentMethod.getPaymentMethodToken());
			}
			else
			{
				final Textbox paymentMethodToken = createTextField(widget, content, "paymentMethodToken",
						paymentMethod.getPaymentMethodToken());
				final Textbox cardholderName = createTextField(widget, content, "cardholderName", paymentMethod.getCardholderName());
				final Textbox creditCardNumber = createTextField(widget, content, "creditCardNumber", paymentMethod.getCardNumber());
				final Textbox creditCardCVV = createTextField(widget, content, "creditCardCVV", StringUtils.EMPTY);
				final Textbox expirationDate = createTextField(widget, content, "expirationDate",
						String.format("%s/%s", paymentMethod.getExpirationMonth(), paymentMethod.getExpirationYear()));

				creditCardNumber.setReadonly(true);

				final Checkbox defaultPaymentMethod = createCheckBoxField(widget, content, "defaultPaymentMethod",
						paymentMethod.isIsDefault());

				addViewFields(widget, content, paymentMethod);

				final Listbox customerAddresses = getCustomerAddresses(content, widget);

				createUpdateButton(widget, content, paymentMethodToken, cardholderName, creditCardNumber, expirationDate,
						creditCardCVV, defaultPaymentMethod, paymentMethod.getPaymentMethodToken(), customerAddresses);
			}
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noPaymentMethodSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	private void addViewFields(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final BrainTreePaymentInfoModel paymentMethod)
	{
		final AddressModel billingAddress = paymentMethod.getBillingAddress();
		if (billingAddress != null)
		{
			addPrettyTitle(widget, content, "billingAddressTitle");
			addHelpTitle(widget, content, "helpAddressTitle");

			createViewTextField(widget, content, "billingFirstName", billingAddress.getFirstname());
			createViewTextField(widget, content, "billingLastName", billingAddress.getLastname());
			createViewTextField(widget, content, "billingCountryCode",
					DefaultLocaleUtils.getCountryName(billingAddress.getCountry()));
			createViewTextField(widget, content, "billingStreetAddress", billingAddress.getStreetname());
			createViewTextField(widget, content, "billingExtendedAddress", billingAddress.getStreetnumber());
			createViewTextField(widget, content, "billingAddressLocality", billingAddress.getTown());
			createRegionViewTextField(widget, content, billingAddress);
			createViewTextField(widget, content, "billingPostalCode", billingAddress.getPostalcode());



		}
	}

	private void createRegionViewTextField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final AddressModel billingAddress)
	{
		if (billingAddress.getRegion() != null)
		{
			createViewTextField(widget, content, "billingRegion", DefaultLocaleUtils.getRegionName(billingAddress.getRegion()));
		}
		else
		{
			createViewTextField(widget, content, "billingRegion", billingAddress.getZone());
		}
	}

	private Checkbox createCheckBoxField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final String label, final boolean isDefault)
	{
		final Checkbox checkbox = new Checkbox(LabelUtils.getLabel(widget, label, new Object[0]));
		checkbox.setChecked(isDefault);
		content.appendChild(checkbox);
		return checkbox;
	}

	private void createUpdateButton(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final Textbox paymentMethodToken, final Checkbox defaultPaymentMethod, final String token)
	{
		final Div refundButtonBox = new Div();
		refundButtonBox.setClass("btCustomerDiv");
		refundButtonBox.setParent(content);
		createButton(widget, refundButtonBox, "updateButton",
				createEditEventListener(widget, paymentMethodToken, defaultPaymentMethod, token));
	}

	private void createUpdateButton(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content,
			final Textbox paymentMethodToken, final Textbox cardholderName, final Textbox creditCardNumber,
			final Textbox expirationDate, final Textbox creditCardCVV, final Checkbox defaultPaymentMethod, final String token,
			final Listbox customerAddresses)
	{
		final Div refundButtonBox = new Div();
		refundButtonBox.setClass("btCustomerDiv");
		refundButtonBox.setParent(content);
		createButton(widget, refundButtonBox, "updateButton", createEditEventListener(widget, paymentMethodToken, cardholderName,
				creditCardNumber, expirationDate, creditCardCVV, defaultPaymentMethod, token, customerAddresses));
	}

	private EventListener createEditEventListener(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final Textbox paymentMethodToken, final Textbox cardholderName, final Textbox creditCardNumber,
			final Textbox expirationDate, final Textbox creditCardCVV, final Checkbox defaultPaymentMethod, final String token,
			final Listbox customerAddresses)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				CustomerPaymentMethodUpdateWidgetRenderer.this.handleEditButtonClickEvent(widget, paymentMethodToken, cardholderName,
						creditCardNumber, expirationDate, creditCardCVV, defaultPaymentMethod, token, customerAddresses);
			}
		};
	}

	private EventListener createEditEventListener(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final Textbox paymentMethodToken, final Checkbox defaultPaymentMethod, final String token)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				CustomerPaymentMethodUpdateWidgetRenderer.this.handleEditButtonClickEvent(widget, paymentMethodToken,
						defaultPaymentMethod, token);
			}
		};
	}

	private void handleEditButtonClickEvent(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final Textbox paymentMethodToken, final Checkbox defaultPaymentMethod, final String token)
	{
		final BraintreePaymentMethodInfo braintreeInfo = new BraintreePaymentMethodInfo();
		braintreeInfo.setPaymentMethodToken(token);
		braintreeInfo.setDefaultPaymentMethod(defaultPaymentMethod.isChecked());
		braintreeInfo.setNewPaymentMethodToken(paymentMethodToken.getValue());

		handleClickEvent(widget, token, braintreeInfo);
	}

	private void handleEditButtonClickEvent(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final Textbox paymentMethodToken, final Textbox cardholderName, final Textbox creditCardNumber,
			final Textbox expirationDate, final Textbox creditCardCVV, final Checkbox defaultPaymentMethod, final String token,
			final Listbox customerAddresses)
	{
		final BraintreePaymentMethodInfo braintreeInfo = new BraintreePaymentMethodInfo();
		braintreeInfo.setPaymentMethodToken(token);
		braintreeInfo.setCardHolder(cardholderName.getValue());
		braintreeInfo.setCardNumber(creditCardNumber.getValue());
		braintreeInfo.setExpirationDate(expirationDate.getValue());
		braintreeInfo.setDefaultPaymentMethod(defaultPaymentMethod.isChecked());
		braintreeInfo.setNewPaymentMethodToken(paymentMethodToken.getValue());
		braintreeInfo.setBillingAddress(getSelectedAddress(customerAddresses));
		braintreeInfo.setCvv(creditCardCVV.getValue());

		handleClickEvent(widget, token, braintreeInfo);
	}

	private void handleClickEvent(final Widget<CustomerItemWidgetModel, CustomerController> widget, final String originToken,
			final BraintreePaymentMethodInfo braintreeInfo)
	{
		final BraintreePaymentMethodData result = getCsBrainTreeFacade().updatePaymentMethod(originToken, braintreeInfo);
		if (result.isSuccess())
		{
			MessageShowUtils.showSuccessEditPaymentMethodMessage();
			getPopupWidgetHelper().dismissCurrentPopup();

			final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
			if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
			{
				final BraintreeCustomerDetailsModel updatedCustomer = getDefaultUpdateCustomerStrategy()
						.update((BraintreeCustomerDetailsModel) currentCustomer.getObject(), result, originToken);
				final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(updatedCustomer);
				getItemAppender().add(itemTypedObject, 1L);
			}
		}
		else
		{
			MessageShowUtils.showErrorEditPaymentMethod(result);
		}
	}

	private void createButton(final Widget<CustomerItemWidgetModel, CustomerController> widget, final HtmlBasedComponent component,
			final String buttonLabelName, final EventListener eventListener)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(component);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
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

	public Listbox getCustomerAddresses(final Div content, final Widget<CustomerItemWidgetModel, CustomerController> widget)
	{

		final TypedObject currentCustomer = widget.getWidgetController().getCurrentCustomer();
		if (currentCustomer != null && currentCustomer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			return UIElementUtils.createCustomerUpdateAddressesListBox(widget, content, "addresses",
					(BraintreeCustomerDetailsModel) currentCustomer.getObject());
		}
		return null;
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
