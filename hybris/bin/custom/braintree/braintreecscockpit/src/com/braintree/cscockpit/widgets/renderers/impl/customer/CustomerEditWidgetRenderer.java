package com.braintree.cscockpit.widgets.renderers.impl.customer;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_CUSTOMER_UPDATE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_UPDATE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_UPDATE_SUCCESS;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.dispatcher.ItemAppender;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.enumeration.EnumerationService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.cscockpit.constraint.EmailConstraint;
import com.braintree.cscockpit.facade.CsBrainTreeFacade;
import com.braintree.converters.BraintreeCustomerDetailsConverter;
import com.braintree.command.result.BrainTreeUpdateCustomerResult;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.UIElementUtils;
import com.braintree.model.BraintreeCustomerDetailsModel;
import com.braintreegateway.Customer;


public class CustomerEditWidgetRenderer extends AbstractCsWidgetRenderer<Widget<CustomerItemWidgetModel, CustomerController>>
{
	private static final Logger LOG = Logger.getLogger(CustomerEditWidgetRenderer.class);
	private PopupWidgetHelper popupWidgetHelper;
	private EnumerationService enumerationService;
	private CsBrainTreeFacade csBrainTreeFacade;
	private BraintreeCustomerDetailsConverter customerDetailsPopulator;
	private ItemAppender<TypedObject> itemAppender;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();
		final TypedObject customer = widget.getWidgetController().getCurrentCustomer();
		if (customer != null && customer.getObject() instanceof BraintreeCustomerDetailsModel)
		{
			final BraintreeCustomerDetailsModel currentCustomer = (BraintreeCustomerDetailsModel) customer.getObject();

			final Textbox customerIdField = createTextField(widget, createDiv(content), "customerId", currentCustomer.getId());
			final Textbox firstNameField = createTextField(widget, createDiv(content), "firstName", currentCustomer.getFirstName());
			final Textbox lastNameField = createTextField(widget, createDiv(content), "lastName", currentCustomer.getLastName());
			final Textbox companyField = createTextField(widget, createDiv(content), "company", currentCustomer.getCompany());
			final Textbox emailField = createTextField(widget, createDiv(content), "email", currentCustomer.getEmail());
			emailField.setConstraint(new EmailConstraint());
			final Textbox phoneField = createTextField(widget, createDiv(content), "phone", currentCustomer.getPhone());
			final Textbox faxField = createTextField(widget, createDiv(content), "fax", currentCustomer.getFax());
			final Textbox websiteField = createTextField(widget, createDiv(content), "website", currentCustomer.getWebsite());

			final Div refundButtonBox = new Div();

			refundButtonBox.setClass("btCustomerRefundButton");
			refundButtonBox.setParent(content);
			createButton(
					widget,
					refundButtonBox,
					"editButton",
					createEditEventListener(currentCustomer, customerIdField, firstNameField, lastNameField, emailField, phoneField,
							faxField, websiteField, companyField));
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noCustomerSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	private Div createDiv(final Div content)
	{
		final Div cloneFieldsBox = new Div();
		cloneFieldsBox.setParent(content);
		cloneFieldsBox.setClass("btCustomerEditDiv");
		return cloneFieldsBox;
	}

	private void createButton(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent component, final String buttonLabelName, final EventListener eventListener)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(component);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	protected Textbox createTextField(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div parent,
			final String label, final String value)
	{
		return UIElementUtils.createTextField(widget, parent, label, value);
	}

	private EventListener createEditEventListener(final BraintreeCustomerDetailsModel currentCustomer,
			final Textbox customerIdField, final Textbox firstNameField, final Textbox lastNameField, final Textbox emailField,
			final Textbox phoneField, final Textbox faxField, final Textbox websiteField, final Textbox companyField)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				CustomerEditWidgetRenderer.this.handleEditButtonClickEvent(currentCustomer, customerIdField, firstNameField,
						lastNameField, emailField, phoneField, faxField, websiteField, companyField);
			}
		};
	}

	private void handleEditButtonClickEvent(final BraintreeCustomerDetailsModel currentCustomer, final Textbox customerIdField,
			final Textbox firstNameField, final Textbox lastNameField, final Textbox emailField, final Textbox phoneField,
			final Textbox faxField, final Textbox websiteField, final Textbox companyField)
	{
		try
		{
			final BrainTreeUpdateCustomerResult result = getCsBrainTreeFacade().updateCustomer(currentCustomer.getId(),
					customerIdField.getValue(), firstNameField.getValue(), lastNameField.getValue(), emailField.getValue(),
					phoneField.getValue(), faxField.getValue(), websiteField.getValue(), companyField.getValue());

			if (result.isSuccess())
			{
				showSuccessMessage();
				processCustomerRendering(result);
			}
			else
			{
				showErrorMessage(result);
			}
		}
		catch (final InterruptedException e)
		{
			LOG.debug("Errors occurred while showing message box!", e);
		}
	}

	private void processCustomerRendering(final BrainTreeUpdateCustomerResult result)
	{
		getPopupWidgetHelper().dismissCurrentPopup();
		final Customer customerItem = result.getCustomer();
		final BraintreeCustomerDetailsModel customer = convertCustomer(customerItem);
		final TypedObject itemTypedObject = getCockpitTypeService().wrapItem(customer);
		getItemAppender().add(itemTypedObject, 1L);
	}

	private void showSuccessMessage() throws InterruptedException
	{
		Messagebox.show(getLabel(WIDGET_MESSAGE_CUSTOMER_UPDATE_SUCCESS), getLabel(WIDGET_CUSTOMER_UPDATE_TITLE), Messagebox.OK,
				Messagebox.INFORMATION);
	}

	private void showErrorMessage(final BrainTreeUpdateCustomerResult result) throws InterruptedException
	{
		String errorMessage;
		if (StringUtils.isNotBlank(result.getErrorMessage()))
		{
			errorMessage = result.getErrorMessage();
		}
		else
		{
			errorMessage = getLabel(WIDGET_MESSAGE_CUSTOMER_UPDATE_ERROR);
		}
		Messagebox.show(errorMessage, getLabel(WIDGET_CUSTOMER_UPDATE_TITLE), Messagebox.OK, Messagebox.ERROR);
	}

	private BraintreeCustomerDetailsModel convertCustomer(final Customer customerItem)
	{
		if (customerItem != null)
		{
			return getCustomerDetailsPopulator().convert(customerItem);
		}
		return null;
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

	protected PopupWidgetHelper getPopupWidgetHelper()
	{
		return this.popupWidgetHelper;
	}

	@Required
	public void setPopupWidgetHelper(final PopupWidgetHelper popupWidgetHelper)
	{
		this.popupWidgetHelper = popupWidgetHelper;
	}

	public CsBrainTreeFacade getCsBrainTreeFacade()
	{
		return csBrainTreeFacade;
	}

	@Required
	public void setCsBrainTreeFacade(final CsBrainTreeFacade csBrainTreeFacade)
	{
		this.csBrainTreeFacade = csBrainTreeFacade;
	}

	public BraintreeCustomerDetailsConverter getCustomerDetailsPopulator()
	{
		return customerDetailsPopulator;
	}

	@Required
	public void setCustomerDetailsPopulator(final BraintreeCustomerDetailsConverter customerDetailsPopulator)
	{
		this.customerDetailsPopulator = customerDetailsPopulator;
	}

	public ItemAppender<TypedObject> getItemAppender()
	{
		return itemAppender;
	}

	@Required
	public void setItemAppender(final ItemAppender<TypedObject> itemAppender)
	{
		this.itemAppender = itemAppender;
	}
}
