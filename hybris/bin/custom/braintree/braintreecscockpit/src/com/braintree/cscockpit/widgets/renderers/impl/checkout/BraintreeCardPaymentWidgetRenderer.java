package com.braintree.cscockpit.widgets.renderers.impl.checkout;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultItemWidgetModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.impl.CardPaymentWidget;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CardPaymentWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.BasicPropertyDescriptor;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.BasicPropertyEditorRowConfiguration;
import de.hybris.platform.cscockpit.widgets.renderers.utils.edit.ComposedTypeReferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;


public class BraintreeCardPaymentWidgetRenderer extends CardPaymentWidgetRenderer
{
	private static final Logger LOG = Logger.getLogger(BraintreeCardPaymentWidgetRenderer.class);

	//TODO: IP - Move to some configuration
	private static final String BRAINTREE_HOP_PAGE_URL = "/braintree";

	@Override
	protected HtmlBasedComponent createContentInternal(final CardPaymentWidget widget, final HtmlBasedComponent rootContainer)
	{
		final Div content = new Div();

		try
		{
			addBraintreeIFrame(content);

			final Div paymentOptionContent = new Div();
			paymentOptionContent.setParent(content);
			final ObjectValueContainer paymentOptionObjectValueContainer = loadAndCreateEditors(widget, paymentOptionContent);

			final Div itemCreationContent = new Div();
			itemCreationContent.setSclass("itemCreationContent");
			itemCreationContent.setParent(content);

			final Button createButton = new Button(LabelUtils.getLabel(widget, "createButton", new Object[0]));
			if (UISessionUtils.getCurrentSession().isUsingTestIDs())
			{
				UITools.applyTestID(createButton, "Payments_NewPayment_Add_button");
			}

			createButton.setParent(itemCreationContent);
			createButton.addEventListener("onClick", createCreateItemEventListener(widget, paymentOptionObjectValueContainer));
		}
		catch (final ValueHandlerException e)
		{
			LOG.error("unable to render new payment option creation widget", e);
		}

		return content;
	}

	private void addBraintreeIFrame(final Div content)
	{
		final Iframe iframe = new Iframe(BRAINTREE_HOP_PAGE_URL);

		iframe.setWidth("450px");
		iframe.setHeight("200px");
		iframe.setParent(content);
	}

	@Override
	protected List<PropertyDescriptor> getProperties(final CardPaymentWidget widget)
	{
		if (CollectionUtils.isEmpty(widget.getProperties()))
		{
			final List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();

			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.firstName", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.lastName", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.street1", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.street2", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.city", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.state", "TEXT"));
			final BasicPropertyDescriptor bpdCountry = new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.country",
					"REFERENCE");
			bpdCountry.setReferenceComposedTypeCode("country");
			bpdCountry.addComposedTypeReferenceHelper(createCountryLookup());

			properties.add(bpdCountry);
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.postalCode", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.phoneNumber", "TEXT"));
			properties.add(new BasicPropertyDescriptor("PaymentOption.CardInfo.BillingInfo.email", "TEXT"));


			final PropertyDescriptor currencyDescriptor = getCockpitTypeService().getPropertyDescriptor("AbstractOrder.currency");

			final BasicPropertyDescriptor readonlyCurrencyDescriptor = new BasicPropertyDescriptor(
					currencyDescriptor.getQualifier(), getCockpitTypeService().getAttributeCodeFromPropertyQualifier(
							currencyDescriptor.getQualifier()), currencyDescriptor.getEditorType(), currencyDescriptor.getDescription(),
					true, false, currencyDescriptor.getName());

			readonlyCurrencyDescriptor.setReferenceComposedTypeCode("Currency");
			readonlyCurrencyDescriptor.addComposedTypeReferenceHelper(createCurrencyLookup());
			properties.add(readonlyCurrencyDescriptor);
			properties.add(new BasicPropertyDescriptor("PaymentOption.amount", "DECIMAL"));

			widget.setProperties(properties);
		}

		return widget.getProperties();
	}

	@Override
	protected Map<PropertyDescriptor, EditorRowConfiguration> getPropertyEditorRowConfigurations(CardPaymentWidget widget)
	{
		if (widget.getPropertyEditorRowConfigurations() == null)
		{
			HashMap<PropertyDescriptor, EditorRowConfiguration> propertyEditorRowConfigurations = new HashMap<>();
			Iterator propertyDescriptorIterator = this.getProperties(widget).iterator();

			while (propertyDescriptorIterator.hasNext())
			{
				PropertyDescriptor property = (PropertyDescriptor) propertyDescriptorIterator.next();
				BasicPropertyEditorRowConfiguration basicPropertyEditorRowConfiguration = null;
				if ("PaymentOption.amount".equals(property.getQualifier()))
				{
					basicPropertyEditorRowConfiguration = new BasicPropertyEditorRowConfiguration(false, false, null, property);
				}
				else
				{
					basicPropertyEditorRowConfiguration = new BasicPropertyEditorRowConfiguration(true, true, null, property);
				}
				propertyEditorRowConfigurations.put(property, basicPropertyEditorRowConfiguration);
			}

			widget.setPropertyEditorRowConfigurations(propertyEditorRowConfigurations);
		}

		return widget.getPropertyEditorRowConfigurations();
	}

	private ComposedTypeReferenceHelper createCurrencyLookup()
	{
		return new ComposedTypeReferenceHelper()

		{
			@Override
			public CurrencyModel getComposedTypeObject(final String code)
			{
				return getCommonI18NService().getCurrency(code);

			}

			@Override
			public String getCode(final TypedObject object)
			{
				return ((object == null) ? null : ((CurrencyModel) object.getObject()).getIsocode());

			}
		};
	}

	private ComposedTypeReferenceHelper createCountryLookup()
	{
		return new ComposedTypeReferenceHelper()

		{
			@Override
			public CountryModel getComposedTypeObject(final String code)
			{
				return getCommonI18NService().getCountry(code);
			}

			@Override
			public String getCode(final TypedObject object)
			{
				return ((object == null) ? null : ((CountryModel) object.getObject()).getIsocode());
			}
		};
	}

	@Override
	protected void handleCreateItemEvent(final CardPaymentWidget widget, final Event event,
			final ObjectValueContainer valueContainer) throws Exception
	{
		try
		{
			if (widget.getWidgetController().processPayment(valueContainer))
			{
				((DefaultItemWidgetModel) widget.getWidgetModel()).notifyListeners();
				getPopupWidgetHelper().dismissCurrentPopup();

				widget.getWidgetController().dispatchEvent(null, widget, null);
				widget.getWidgetController().getCustomerController().dispatchEvent(null, widget, null);
			}

		}
		catch (final PaymentException e)
		{
			Messagebox.show(e.getMessage(), LabelUtils.getLabel(widget, "failedToAuthorise", new Object[0]), 1,
					"z-msgbox z-msgbox-error");
			widget.getWidgetController().dispatchEvent(null, widget, null);
		}
		catch (final ValidationException e)
		{
			LOG.error("Failed to create new payment option, validation error", e);
			Messagebox.show(
					e.getMessage()
							+ ((e.getCause() == null) ? "" : new StringBuilder(" - ").append(e.getCause().getMessage()).toString()),
					LabelUtils.getLabel(widget, "failedToValidate", new Object[0]), 1, "z-msgbox z-msgbox-error");
		}
		catch (final Exception e)
		{
			LOG.error("Failed to create new payment option", e);
			throw e;
		}
	}
}
