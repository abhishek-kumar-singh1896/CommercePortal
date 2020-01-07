/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.braintree.constants;


/**
 * Global class for all braintreecscockpit constants. You can add global constants for your extension into this class.
 */
public final class BraintreecscockpitConstants extends GeneratedBraintreecscockpitConstants {

    public static final String EXTENSIONNAME = "braintreecscockpit";

    public static final String BRAINTREE_PAYMENT_METHOD_NONCE_SESSION_ATTRIBUTE = "BRAINTREE_PAYMENT_INFO";

    private BraintreecscockpitConstants() {
    }

    public interface TransactionManagementActionsWidgetRenderer {

        String WIDGET_ICON_SUCCESS = "z-msgbox z-msgbox-information";
        String WIDGET_ICON_ERROR = "z-msgbox z-msgbox-error";
        String WIDGET_VOID_TITLE = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.title";
        String WIDGET_MESSAGE_VOID_ORDER_ERROR = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.order.message.error";
        String WIDGET_MESSAGE_VOID_VALIDATION_ERROR = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.order.validation.message.error";
        String WIDGET_MESSAGE_VOID_ERROR = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.message.error";
        String WIDGET_MESSAGE_VOID_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.message.success";

        String WIDGET_CLONE_TITLE = "cscockpit.widget.transaction.transactionmanagement.cloneTransaction.title";
        String WIDGET_MESSAGE_CLONE_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.cloneTransaction.message.success";
        String WIDGET_MESSAGE_CLONE_ERROR = "cscockpit.widget.transaction.transactionmanagement.cloneTransaction.message.error";

        String WIDGET_VOID_ASK_TITLE = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.ask.title";
        String WIDGET_VOID_ASK_MESSAGE = "cscockpit.widget.transaction.transactionmanagement.voidTransaction.ask.message";

        String WIDGET_REFUND_TITLE = "cscockpit.widget.transaction.transactionmanagement.refundTransaction.title";
        String WIDGET_MESSAGE_REFUND_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.refundTransaction.message.success";
        String WIDGET_MESSAGE_REFUND_ERROR = "cscockpit.widget.transaction.transactionmanagement.refundTransaction.message.error";
        String WIDGET_MESSAGE_REFUND_ERROR_GO_TO_ORDER = "cscockpit.widget.transaction.transactionmanagement.refundTransaction.message.error.goToOrder";

        String WIDGET_MESSAGE_CUSTOMER_UPDATE_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.updateCustomer.message.success";
        String WIDGET_MESSAGE_CUSTOMER_UPDATE_ERROR = "cscockpit.widget.transaction.transactionmanagement.updateCustomer.message.error";
        String WIDGET_CUSTOMER_UPDATE_TITLE = "cscockpit.widget.transaction.transactionmanagement.updateCustomer.title";

        String WIDGET_MESSAGE_VOID_DURING_REFUND_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.refundVoidTransaction.message.info";

        String WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.newTransaction.message.success";
        String WIDGET_MESSAGE_TRANSACTION_CREATE_SUCCESS_POSTFIX = "cscockpit.widget.transaction.transactionmanagement.newTransaction.message.success.postfix";
        String WIDGET_MESSAGE_TRANSACTION_CREATE_ERROR = "cscockpit.widget.transaction.transactionmanagement.newTransaction.message.error";
        String WIDGET_TRANSACTION_CREATE_TITLE = "cscockpit.widget.transaction.transactionmanagement.newTransaction.title";

        String WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_SUCCESS = "cscockpit.widget.payment.method.delete.success";
        String WIDGET_MESSAGE_PAYMENT_METHOD_DELETE_ERROR = "cscockpit.widget.payment.method.delete.error";
        String WIDGET_PAYMENT_METHOD_DELETE_TITLE = "cscockpit.widget.payment.method.delete.title";

        String WIDGET_MESSAGE_CUSTOMER_REMOVE_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.removeCustomer.message.success";
        String WIDGET_MESSAGE_CUSTOMER_REMOVE_ERROR = "cscockpit.widget.transaction.transactionmanagement.removeCustomer.message.error";
        String WIDGET_CUSTOMER_REMOVE_TITLE = "cscockpit.widget.transaction.transactionmanagement.removeCustomer.title";

        String WIDGET_REMOVE_CUSTOMER_ASK_TITLE = "cscockpit.widget.transaction.transactionmanagement.removeCustomer.ask.title";
        String WIDGET_REMOVE_CUSTOMER_ASK_MESSAGE = "cscockpit.widget.transaction.transactionmanagement.removeCustomer.ask.message";

        String WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.updatePaymentMethod.message.success";
        String WIDGET_MESSAGE_EDIT_PAYMENT_METHOD_ERROR = "cscockpit.widget.transaction.transactionmanagement.updatePaymentMethod.message.error";
        String WIDGET_EDIT_PAYMENT_METHOD_TITLE = "cscockpit.widget.transaction.transactionmanagement.updatePaymentMethod.title";

        String WIDGET_REMOVE_PAYMENT_METHOD_ASK_TITLE = "cscockpit.widget.transaction.transactionmanagement.removePaymentMethod.ask.title";
        String WIDGET_REMOVE_PAYMENT_METHOD_ASK_MESSAGE = "cscockpit.widget.transaction.transactionmanagement.removePaymentMethod.ask.message";

        String WIDGET_MESSAGE_TRANSACTION_SFS_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.sfsTransaction.message.success";
        String WIDGET_MESSAGE_TRANSACTION_SFS_ERROR = "cscockpit.widget.transaction.transactionmanagement.sfsTransaction.message.error";
        String WIDGET_TRANSACTION_SFS_TITLE = "cscockpit.widget.transaction.transactionmanagement.sfsTransaction.title";
        String WIDGET_MESSAGE_TRANSACTION_SFS_REFRESH = "cscockpit.widget.transaction.transactionmanagement.sfsTransaction.message.refresh";

	String WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_SUCCESS = "cscockpit.widget.customer.address.create.message.success";
	String WIDGET_CUSTOMER_ADDRESS_CREATE_TITLE = "cscockpit.widget.customer.address.create.title";
	String WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR = "cscockpit.widget.customer.address.create.message.error";
        String WIDGET_MESSAGE_CUSTOMER_ADDRESS_EDIT_ERROR = "cscockpit.widget.customer.address.edit.message.error";

        String WIDGET_MESSAGE_PAYMENT_METHOD_NONCE_ERROR = "cscockpit.widget.transaction.transactionmanagement.nonce.message.error";
        String WIDGET_PAYMENT_METHOD_NONCE_TITLE = "cscockpit.widget.transaction.transactionmanagement.nonce.title";

        String WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_SUCCESS = "cscockpit.widget.transaction.transactionmanagement.createPaymentMethod.message.success";
        String WIDGET_MESSAGE_PAYMENT_METHOD_CREATE_ERROR = "cscockpit.widget.transaction.transactionmanagement.createPaymentMethod.message.error";
        String WIDGET_PAYMENT_METHOD_CREATE_TITLE = "cscockpit.widget.transaction.transactionmanagement.createPaymentMethod.title";

        String WIDGET_MESSAGE_CUSTOMER_ADDRESS_CREATE_ERROR_NO_COUNTRY = "cscockpit.widget.customer.address.create.message.error.no.selected.country";

        String WIDGET_MESSAGE_ADDRESS_REMOVE_SUCCESS = "cscockpit.widget.customer.address.remove.message.success";
        String WIDGET_MESSAGE_ADDRESS_REMOVE_ERROR = "cscockpit.widget.customer.address.remove.message.error";
        String WIDGET_ADDRESS_REMOVE_TITLE = "cscockpit.widget.customer.address.remove.title";

        String WIDGET_REMOVE_ADDRESS_ASK_TITLE = "cscockpit.widget.transaction.transactionmanagement.removeAddress.ask.title";
        String WIDGET_REMOVE_ADDRESS_ASK_MESSAGE = "cscockpit.widget.transaction.transactionmanagement.removeAddress.ask.message";

        String WIDGET_PARTIAL_CAPTURE_TITLE = "cscockpit.widget.order.capture.partial.title";
        String WIDGET_PARTIAL_CAPTURE_SUCCESS = "cscockpit.widget.order.capture.partial.message.success";
        String WIDGET_PARTIAL_CAPTURE_ERROR = "cscockpit.widget.order.capture.partial.message.error";
        String WIDGET_PARTIAL_CAPTURE_INCORRECT_AMOUNT_ERROR = "cscockpit.widget.order.capture.partial.message.error.amount.incorrect";
        String WIDGET_PARTIAL_CAPTURE_AMOUNT_NOT_MATH_ERROR = "cscockpit.widget.order.capture.partial.message.error.amount.not.match";
        String WIDGET_PARTIAL_AVAILABLE_AUTHORIZATION_TITLE = "cscockpit.widget.order.capture.partial.available.transaction";

        String WIDGET_PARTIAL_REFUND_TITLE = "cscockpit.widget.order.refund.partial.title";
        String WIDGET_PARTIAL_REFUND_SUCCESS = "cscockpit.widget.order.refund.partial.success";
        String WIDGET_PARTIAL_REFUND_ERROR = "cscockpit.widget.order.refund.partial.error";
        String WIDGET_PARTIAL_REFUND_BUTTON = "cscockpit.widget.order.refund.partial.refund.button";
        String WIDGET_PARTIAL_REFUND_AVAILABLE="cscockpit.widget.order.refund.partial.refund.available.message";
        String WIDGET_PARTIAL_REFUND_AMOUNT="cscockpit.widget.order.refund.partial.refund.amount";
        String WIDGET_PARTIAL_REFUND_AVAILABLE_TRANSACTIONS="cscockpit.widget.order.refund.partial.available.transactions";
        String WIDGET_PARTIAL_REFUND_TRANSACTION_ID = "cscockpit.widget.order.refund.partial.transaction.id";
        String WIDGET_PARTIAL_REFUND_INCORRECT_AMOUNT_ERROR = "cscockpit.widget.order.refund.partial.message.error.amount.incorrect";
        String WIDGET_VOID_AUTHORIZATION_AVAILABLE_TRANSACTIONS ="cscockpit.widget.order.void.authorization.available.transactions";
        String WIDGET_VOID_AUTHORIZATION_BUTTON = "cscockpit.widget.order.void.authorization.button";
        String WIDGET_VOID_AUTHORIZATION_SUCCESS = "cscockpit.widget.order.void.authorization.success";
        String WIDGET_VOID_AUTHORIZATION_TITLE = "cscockpit.widget.order.void.authorization.title";
        String WIDGET_VOID_AUTHORIZATION_ERROR = "cscockpit.widget.order.void.authorization.error";
    }

    public interface BraintreeHOP {

        String VALIDATION_PAYMENT_METHOD = "validation.payment.method";
    }
}
