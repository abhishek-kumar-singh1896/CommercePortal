package com.braintree.cscockpit.widgets.renderers.impl.customer;

import com.braintree.constants.BraintreeConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.jalo.JaloSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.braintree.cscockpit.widgets.controllers.CustomerController;
import com.braintree.cscockpit.widgets.models.impl.CustomerItemWidgetModel;
import com.braintree.cscockpit.widgets.renderers.utils.DefaultLocaleUtils;
import com.braintree.enums.BrainTreePaymentMethod;
import com.braintree.model.BrainTreePaymentInfoModel;


public class CustomerPaymentMethodDetailsWidgetRenderer extends AbstractPaymentMethodWidgetRenderer
{
	private static final String DEFAULT_YES_SIGN = "yes";
	private static final String DEFAULT_NO_SIGN = "no";

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<CustomerItemWidgetModel, CustomerController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{

		final Div content = new Div();
		final TypedObject currentPaymentMethod = widget.getWidgetController().getCurrentPaymentMethod();
		if (currentPaymentMethod != null && currentPaymentMethod.getObject() instanceof BrainTreePaymentInfoModel)
		{
			final BrainTreePaymentInfoModel paymentMethod = (BrainTreePaymentInfoModel) currentPaymentMethod.getObject();

			createViewTextField(widget, content, "paymentMethodToken", paymentMethod.getPaymentMethodToken());

			if (BrainTreePaymentMethod.PAYPAL.toString().equals(paymentMethod.getPaymentProvider()))
			{
				createViewTextField(widget, content, "cardType", BrainTreePaymentMethod.PAYPAL.toString());
				createViewTextField(widget, content, "customerID", paymentMethod.getCustomerId());
				createViewTextField(widget, content, "paypalEmail", paymentMethod.getPaymentInfo());
			}
			else
			{
				createViewTextField(widget, content, "cardType",
						paymentMethod.getCardType() != null ? paymentMethod.getCardType().toString() : StringUtils.EMPTY);
				createViewTextField(widget, content, "customerID", paymentMethod.getCustomerId());
				createViewTextField(widget, content, "creditCardHolderName", paymentMethod.getCardholderName());
				createViewTextField(widget, content, "creditCardNumber", paymentMethod.getCardNumber());
				createViewTextField(widget, content, "expirationDate",
						String.format("%s/%s", paymentMethod.getExpirationMonth(), paymentMethod.getExpirationYear()));

				createViewTextField(widget, content, "customerLocation", paymentMethod.getCustomerLocation());
			}
			createViewTextField(widget, content, "defaultPaymentMethod", createStringAnswer(paymentMethod.isIsDefault()));
			createViewTextField(widget, content, "createOn", formatDate(paymentMethod.getCreatedAt()));

			final AddressModel billingAddress = paymentMethod.getBillingAddress();
			if (billingAddress != null)
			{
				addPrettyTitle(widget, content, "billingAddressTitle");

				createViewTextField(widget, content, "billingFirstName", billingAddress.getFirstname());
				createViewTextField(widget, content, "billingLastName", billingAddress.getLastname());
				createViewTextField(widget, content, "billingCountryCode",
						DefaultLocaleUtils.getCountryName(billingAddress.getCountry()));
				createViewTextField(widget, content, "billingStreetAddress", billingAddress.getStreetname());
				createViewTextField(widget, content, "billingExtendedAddress", billingAddress.getStreetnumber());
				createViewTextField(widget, content, "billingAddressLocality", billingAddress.getTown());
				createViewTextField(widget, content, "billingRegion", billingAddress.getRegion() != null
						? DefaultLocaleUtils.getRegionName(billingAddress.getRegion()) : billingAddress.getZone());
				createViewTextField(widget, content, "billingPostalCode", billingAddress.getPostalcode());

			}
			createCancelButton(widget, content);
		}
		else
		{
			final Label dummyLabel = new Label(LabelUtils.getLabel(widget, "noPaymentMethodSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	private void createCancelButton(final Widget<CustomerItemWidgetModel, CustomerController> widget, final Div content)
	{
		final Div refundButtonBox = new Div();
		refundButtonBox.setClass("btCustomerDiv");
		refundButtonBox.setParent(content);
		createButton(widget, refundButtonBox, "cancelButton", createEditEventListener(widget));
	}

	private EventListener createEditEventListener(final Widget<CustomerItemWidgetModel, CustomerController> widget)
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				getPopupWidgetHelper().dismissCurrentPopup();
			}
		};
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

	private String createStringAnswer(final boolean isDefault)
	{
		return isDefault ? DEFAULT_YES_SIGN : DEFAULT_NO_SIGN;
	}
}
