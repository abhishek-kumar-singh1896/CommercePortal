package com.braintree.cscockpit.widgets.renderers.utils;

import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_ADDRESS_REMOVE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_CUSTOMER_REMOVE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_EDIT_PAYMENT_METHOD_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_ADDRESS_REMOVE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_ADDRESS_REMOVE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_REMOVE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_CUSTOMER_REMOVE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_SUCCESS;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_MESSAGE_PAYMENT_METHOD_NONCE_ERROR;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PAYMENT_METHOD_CREATE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PAYMENT_METHOD_DELETE_TITLE;
import static com.braintree.constants.BraintreecscockpitConstants.TransactionManagementActionsWidgetRenderer.WIDGET_PAYMENT_METHOD_NONCE_TITLE;
import static org.zkoss.util.resource.Labels.getLabel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.braintree.command.result.BrainTreeAddressResult;
import com.braintree.command.result.BrainTreePaymentMethodResult;
import com.braintree.hybris.data.BrainTreeResponseResultData;


public class MessageShowUtils
{
	private static final Logger LOG = Logger.getLogger(MessageShowUtils.class);

	private MessageShowUtils()
	{
	}

	public static void showErrorRemoveCustomerMessage(final BrainTreeResponseResultData result)
	{
		final String errorMessage = getErrorMessage(result.getErrorMessage(), WIDGET_MESSAGE_CUSTOMER_REMOVE_ERROR);
		showMessageBox(errorMessage, WIDGET_CUSTOMER_REMOVE_TITLE, Messagebox.ERROR);
	}

	public static void showSuccessRemoveCustomerMessage()
	{
		showMessageBox(getLabel(WIDGET_MESSAGE_CUSTOMER_REMOVE_SUCCESS), WIDGET_CUSTOMER_REMOVE_TITLE, Messagebox.INFORMATION);
	}

	public static void showSuccessEditPaymentMethodMessage()
	{
		showMessageBox(getLabel(WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_SUCCESS), WIDGET_EDIT_PAYMENT_METHOD_TITLE,
				Messagebox.INFORMATION);
	}

	public static void showErrorEditPaymentMethod(final BrainTreeResponseResultData result)
	{
		final String errorMessage = getErrorMessage(result.getErrorMessage(), WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_ERROR);
		showMessageBox(errorMessage, WIDGET_EDIT_PAYMENT_METHOD_TITLE, Messagebox.ERROR);
	}

	public static void showErrorRemovePaymentMethodMessage(final BrainTreePaymentMethodResult result)
	{
		final String errorMessage = getErrorMessage(result.getErrorMessage(), WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_ERROR);
		showMessageBox(errorMessage, WIDGET_PAYMENT_METHOD_DELETE_TITLE, Messagebox.ERROR);
	}

	public static void showSuccessRemovePaymentMethodMessage()
	{
		showMessageBox(getLabel(WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_SUCCESS), WIDGET_PAYMENT_METHOD_DELETE_TITLE,
				Messagebox.INFORMATION);
	}

	public static void showErrorPaymentMethodNonceRetrieveMessage()
	{
		showMessageBox(getLabel(WIDGET_MESSAGE_PAYMENT_METHOD_NONCE_ERROR), WIDGET_PAYMENT_METHOD_NONCE_TITLE, Messagebox.ERROR);
	}

	public static void showSuccessCreatePaymentMethodMessage()
	{
		showMessageBox(getLabel(WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_SUCCESS), WIDGET_PAYMENT_METHOD_CREATE_TITLE,
				Messagebox.INFORMATION);
	}

	public static void showErrorCreatePaymentMethodMessage(final BrainTreePaymentMethodResult result)
	{
		final String errorMessage = getErrorMessage(result.getErrorMessage(), WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_ERROR);
		showMessageBox(errorMessage, WIDGET_PAYMENT_METHOD_CREATE_TITLE, Messagebox.ERROR);
	}

	private static void showMessageBox(final String message, final String title, final String type)
	{
		try
		{
			Messagebox.show(message, getLabel(title), Messagebox.OK, type);
		}
		catch (final InterruptedException exception)
		{
			LOG.error(exception.getMessage());
		}
	}

	private static String getErrorMessage(final String errorMessage, final String defaultErrorMessage)
	{
		String message;
		if (StringUtils.isNotBlank(errorMessage))
		{
			message = errorMessage;
		}
		else
		{
			message = getLabel(defaultErrorMessage);
		}
		return message;
	}

	public static void showSuccessRemoveAddressMessage()
	{

		showMessageBox(getLabel(WIDGET_MESSAGE_ADDRESS_REMOVE_SUCCESS), WIDGET_ADDRESS_REMOVE_TITLE, Messagebox.INFORMATION);
	}

	public static void showErrorRemoveAddressMessage(final BrainTreeAddressResult result)
	{
		final String errorMessage = getErrorMessage(result.getErrorMessage(), WIDGET_MESSAGE_ADDRESS_REMOVE_ERROR);
		showMessageBox(errorMessage, WIDGET_ADDRESS_REMOVE_TITLE, Messagebox.ERROR);
	}
}
