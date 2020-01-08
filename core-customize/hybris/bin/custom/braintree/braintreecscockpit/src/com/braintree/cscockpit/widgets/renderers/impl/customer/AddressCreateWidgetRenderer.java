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

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_CUSTOMER_ADDRESS_CREATE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR_NO_COUNTRY;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_SUCCESS;
import static org.zkoss.util.resource.Labels.getLabel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.BraintreeCallContextWidgetModel;
import com.braintree.model.BraintreeCustomerDetailsModel;


public class AddressCreateWidgetRenderer extends
		AbstractCountryRegionSelectWidgetRenderer<Widget<BraintreeCallContextWidgetModel, CustomerController>>
{
	private static final Logger LOG = Logger.getLogger(AddressCreateWidgetRenderer.class);

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<BraintreeCallContextWidgetModel, CustomerController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();

		final String firstName = "";
		final String lastName = "";
		final String company = "";
		final String streetAddress = "";
		final String extendedAddress = "";
		final String cityLocality = "";
		final String stateProvinceRegion = "";
		final String postalCode = "";
		final String selectedCountryIso = "";

		final Textbox firstNameField = createTextField(widget, createDiv(content), "firstName", firstName);
		final Textbox lastNameField = createTextField(widget, createDiv(content), "lastName", lastName);
		final Textbox companyField = createTextField(widget, createDiv(content), "company", company);
		final Textbox streetAddressField = createTextField(widget, createDiv(content), "streetAddress", streetAddress);
		final Textbox extendedAddressField = createTextField(widget, createDiv(content), "extendedAddress", extendedAddress);
		final Textbox cityLocalityField = createTextField(widget, createDiv(content), "cityLocality", cityLocality);
		final Textbox postalCodeField = createTextField(widget, createDiv(content), "postalCode", postalCode);

		final InternalControls internalControls = createCountryAndRegionControls(widget, content, selectedCountryIso,
				stateProvinceRegion);

		final Div saveButtonBox = new Div();

		saveButtonBox.setClass("btCustomerAddressButton");
		saveButtonBox.setParent(content);
		createButton(
				widget,
				saveButtonBox,
				"saveButton",
				createEditEventListener(widget, firstNameField, lastNameField, companyField, streetAddressField,
						extendedAddressField, cityLocalityField, internalControls.getStateProvinceRegionField(), postalCodeField,
						internalControls.getCountrySelectField(), internalControls.getRegionSelectField()));

		return content;
	}

	private void createButton(final Widget widget, final HtmlBasedComponent component, final String buttonLabelName,
			final EventListener eventListener)
	{
		final Button button = new Button();
		button.setLabel(LabelUtils.getLabel(widget, buttonLabelName, new Object[0]));
		button.setParent(component);
		if (eventListener != null)
		{
			button.addEventListener(Events.ON_CLICK, eventListener);
		}
	}

	private EventListener createEditEventListener(final Widget widget, final Textbox firstNameField, final Textbox lastNameField,
			final Textbox companyField, final Textbox streetAddressField, final Textbox extendedAddressField,
			final Textbox cityLocalityField, final Textbox stateProvinceRegionField, final Textbox postalCodeField,
			final Listbox countrySelectField, final Listbox regionSelectField)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				handleSaveButtonClickEvent(widget, firstNameField, lastNameField, companyField, streetAddressField,
						extendedAddressField, cityLocalityField, stateProvinceRegionField, postalCodeField, countrySelectField,
						regionSelectField);
			}
		};
	}

	private void handleSaveButtonClickEvent(final Widget widget, final Textbox firstNameField, final Textbox lastNameField,
			final Textbox companyField, final Textbox streetAddressField, final Textbox extendedAddressField,
			final Textbox cityLocalityField, final Textbox stateProvinceRegionField, final Textbox postalCodeField,
			final Listbox countrySelectField, final Listbox regionSelectField)
	{
		try
		{
			String customerId = "";
			final TypedObject customer = ((CustomerController) widget.getWidgetController()).getCurrentCustomer();
			if (customer != null && customer.getObject() instanceof BraintreeCustomerDetailsModel)
			{
				customerId = ((BraintreeCustomerDetailsModel) customer.getObject()).getId();
			}
			String selectedCountryIso = "";
			if (countrySelectField.getSelectedItem() != null && countrySelectField.getSelectedItem().getValue() != null)
			{
				selectedCountryIso = (String) countrySelectField.getSelectedItem().getValue();
			}

			if (StringUtils.isBlank(selectedCountryIso))
			{
				Messagebox.show(getLabel(WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR_NO_COUNTRY),
						getLabel(WIDGET_CUSTOMER_ADDRESS_CREATE_TITLE), Messagebox.OK, Messagebox.ERROR);
                return;
			}

			String selectedRegion = null;
			if (regionSelectField.getSelectedItem() != null
					&& StringUtils.isNotBlank((String) regionSelectField.getSelectedItem().getValue()))
			{
				selectedRegion = (String) regionSelectField.getSelectedItem().getValue();
			}
			String region = (regionSelectField.isVisible()) ? selectedRegion : stateProvinceRegionField.getValue();

			final BrainTreeAddressResult result = getCsBrainTreeFacade().addAddress(customerId, firstNameField.getValue(),
					lastNameField.getValue(), companyField.getValue(), streetAddressField.getValue(), extendedAddressField.getValue(),
					cityLocalityField.getValue(), region, postalCodeField.getValue(), selectedCountryIso);

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

	private void showSuccessMessage() throws InterruptedException
	{
		Messagebox.show(getLabel(WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_SUCCESS), getLabel(WIDGET_CUSTOMER_ADDRESS_CREATE_TITLE),
				Messagebox.OK, Messagebox.INFORMATION);
	}

	private void showErrorMessage(final BrainTreeAddressResult result) throws InterruptedException
	{
		String errorMessage;
		if (StringUtils.isNotBlank(result.getErrorMessage()))
		{
			errorMessage = result.getErrorMessage();
		}
		else
		{
			errorMessage = getLabel(WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR);
		}
		Messagebox.show(errorMessage, getLabel(WIDGET_CUSTOMER_ADDRESS_CREATE_TITLE), Messagebox.OK, Messagebox.ERROR);
	}
}
