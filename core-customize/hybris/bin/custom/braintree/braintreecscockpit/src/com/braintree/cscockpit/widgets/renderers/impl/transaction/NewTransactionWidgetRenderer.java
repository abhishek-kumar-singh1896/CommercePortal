package com.braintree.cscockpit.widgets.renderers.impl.transaction;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Textbox;

import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.cscockpit.constraint.EmailConstraint;
import com.braintree.cscockpit.constraint.RequiredAmountConstraint;
import com.braintree.cscockpit.data.BrainTreeInfo;
import com.braintree.cscockpit.widgets.controllers.TransactionController;
import com.braintree.cscockpit.widgets.models.impl.TransactionItemWidgetModel;
import com.braintree.hybris.data.BrainTreeResponseResultData;
import com.braintree.payment.dto.BraintreeInfo;


public class NewTransactionWidgetRenderer extends AbstractNewTransactionWidgetRenderer
{
	private static final String BRAINTREE_HOP_PAGE_URL = "/braintree";
	private CommonI18NService commonI18NService;

	@Override
	protected HtmlBasedComponent createContentInternal(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final HtmlBasedComponent htmlBasedComponent)
	{
		final Div content = new Div();

		addPrettyTitle(widget, content, "validateCard");

		addBraintreeIFrame(content);

		addPrettyTitle(widget, content, "paymentDetails");

		final Div paymentDetailsContent = new Div();
		paymentDetailsContent.setParent(content);

		final Textbox currency = createTextField(widget, paymentDetailsContent, "currency");
		setCurrentCurrency(currency);


		final Textbox amount = createTextField(widget, paymentDetailsContent, "amount", "required");
		amount.setConstraint(new RequiredAmountConstraint());

		final Textbox tax = createTextField(widget, paymentDetailsContent, "tax");
		final Textbox custom = createTextField(widget, paymentDetailsContent, "custom", "exampleCustom");
		custom.setMultiline(true);

		addPrettyTitle(widget, content, "customerDetails");

		final Textbox firstName = createTextField(widget, content, "firstName");
		final Textbox lastName = createTextField(widget, content, "lastName");
		final Textbox email = createTextField(widget, content, "email");
		email.setConstraint(new EmailConstraint());

		final Textbox billingPostCode = createTextField(widget, content, "billingPostCode");
		final Textbox billingAddress = createTextField(widget, content, "billingAddress");

		final Textbox shippingPostCode = createTextField(widget, content, "shippingPostCode");
		final Textbox shippingAddress = createTextField(widget, content, "shippingAddress");

		createButton(widget, content, "saveButton", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				NewTransactionWidgetRenderer.this.handleSaveButtonClickEvent(widget, currency, amount, tax, custom, firstName,
						lastName, email, billingAddress, billingPostCode, shippingAddress, shippingPostCode);
			}
		});

		return content;
	}

	private void addBraintreeIFrame(final Div content)
	{
		final Iframe iframe = new Iframe(BRAINTREE_HOP_PAGE_URL);

		iframe.setWidth("450px");
		iframe.setHeight("200px");
		iframe.setParent(content);
	}

	private void handleSaveButtonClickEvent(final Widget<TransactionItemWidgetModel, TransactionController> widget,
			final Textbox currency, final Textbox amount, final Textbox tax, final Textbox custom, final Textbox firstName,
			final Textbox lastName, final Textbox email, final Textbox billingAddress, final Textbox billingPostCode,
			final Textbox shippingAddress, final Textbox shippingPostCode) throws InterruptedException
	{

		final BrainTreeInfo brainTreeInfo = new BrainTreeInfo();
		brainTreeInfo.setAmount(amount.getValue()).setTax(tax.getValue()).setCurrency(currency.getValue());
		setCustomFields(brainTreeInfo, custom);
		brainTreeInfo.setFirstName(firstName.getValue()).setLastName(lastName.getValue()).setEmail(email.getValue());
		brainTreeInfo.setBillingAddress(billingAddress.getValue()).setBillingPostCode(billingPostCode.getValue());
		brainTreeInfo.setShippingAddress(shippingAddress.getValue()).setShippingPostCode(shippingPostCode.getValue());

		final BraintreeInfo btInfo = getBraintreeInfo();
		if (btInfo != null)
		{
			if (StringUtils.isNotBlank(btInfo.getNonce()))
			{
				SubscriptionResult customer = getCsBrainTreeFacade().createCustomer(brainTreeInfo);
				if (customer == null || StringUtils.isBlank(customer.getRequestId()))
				{
					showErrorMessage(Labels
							.getLabel("cscockpit.widget.transaction.transactionmanagement.newTransaction.message.error"));
				}
				else
				{
					createTransaction(widget, brainTreeInfo, btInfo, customer.getMerchantTransactionCode());
				}
			}
			else
			{
				showErrorMessage(Labels
						.getLabel("cscockpit.widget.transaction.transactionmanagement.newTransaction.message.error.noPaymentMethodToken"));
			}
		}
	}

	private void createTransaction(Widget<TransactionItemWidgetModel, TransactionController> widget, BrainTreeInfo brainTreeInfo,
			BraintreeInfo btInfo, String customerID) throws InterruptedException
	{
		BrainTreePaymentMethodResult creditCardPaymentMethod = getCsBrainTreeFacade().createCreditCardPaymentMethod(customerID,
				btInfo.getNonce(), btInfo.getCardholderName(), false, StringUtils.EMPTY);
		if (creditCardPaymentMethod.isSuccess())
		{
			brainTreeInfo.setCardHolder(btInfo.getCardholderName());
			brainTreeInfo.setPaymentMethodToken(creditCardPaymentMethod.getPaymentMethodToken());
			final BrainTreeResponseResultData transaction = getCsBrainTreeFacade().createTransaction(brainTreeInfo);
			processResult(widget, transaction);

		}
		else
		{
			getCsBrainTreeFacade().removeCustomer(customerID);
			showErrorMessage(Labels.getLabel("braintree.verify.card.general.error.msg"));
		}
	}

	private BraintreeInfo getBraintreeInfo() throws InterruptedException
	{
		BraintreeInfo braintreeInfo = null;

		try
		{
			braintreeInfo = getCsBrainTreeFacade().getBraintreeInfo();

			return braintreeInfo;
		}
		catch (final IllegalStateException e)
		{
			LOG.error("Error during additional information retrieving: brainTreeInfo ", e);
			showErrorMessage(Labels
					.getLabel("cscockpit.widget.transaction.transactionmanagement.newTransaction.message.error.noPaymentMethodToken"));
		}

		return braintreeInfo;
	}

	private void setCurrentCurrency(final Textbox currency)
	{
		final CurrencyModel currentCurrency = getCommonI18NService().getCurrentCurrency();
		if (currentCurrency != null)
		{
			currency.setValue(currentCurrency.getIsocode());
		}
		currency.setReadonly(true);
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

}
