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
package com.braintree.controllers;

/**
 */
public interface Braintreeb2baddonControllerConstants
{
	final String ADDON_PREFIX = "addon:/braintreeb2baddon/";
	final String CLIENT_TOKEN = "client_token";
	final String PAY_PAL_CHECKOUT_DATA = "payPalCheckoutData";
	final String IS_ADDRESS_OPEN = "is_credit_card_select";
	final String PAY_PAL_CONFIGURATION_DATA = "payPalConfigurationData";

	//localized error messages
	public static final String GENERAL_HEAD_ERROR = "braintree.checkout.general.error";
	public static final String GENERAL_HEAD_ERROR_MESSAGE = "braintree.checkout.general.error.message";

	public static final String PAY_PAL_GUEST_REGISTER_ERROR = "braintree.checkout.guest.error";
	public static final String PAY_PAL_ADDRESS_ERROR = "braintree.paypal.shipping.error";
	public static final String PAY_PAL_HAED_ERROR = "braintree.paypal.head.error";

	interface Views
	{
		interface Pages
		{
			interface MultiStepCheckout
			{
				String SilentOrderPostPage = ADDON_PREFIX + "pages/checkout/multi/silentOrderPostPage";
				String CheckoutOrderPageErrorPage = ADDON_PREFIX + "pages/checkout/multi/checkoutErrorPage";
				String CheckoutSummaryPage = ADDON_PREFIX + "pages/checkout/multi/checkoutSummaryPage";
			}

			interface Checkout
			{
				String CheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/checkoutConfirmationPage";
				String ReplenishmentCheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/replenishmentCheckoutConfirmationPage";
				String CheckoutLoginPage = "pages/checkout/checkoutLoginPage";
			}

			interface Account
			{
				String AccountLayoutPage = ADDON_PREFIX + "pages/account/accountLayoutPage";
			}
		}

		interface Fragments
		{
			interface Cart
			{
				String CartPopup = ADDON_PREFIX + "fragments/cart/cartPopup";
				String AddToCartPopup = ADDON_PREFIX + "fragments/cart/addToCartPopup";
			}
		}
	}
}
