/**
 *
 */
package com.braintree.constants;

public interface ControllerConstants
{

	static final String ADDON_PREFIX = "addon:/b2bacceleratoraddon/";
	static final String STOREFRONT_PREFIX = "/";

	interface Views
	{
		interface Pages
		{
			interface MultiStepCheckout
			{
				String ChoosePaymentTypePage = ADDON_PREFIX + "pages/checkout/multi/choosePaymentTypePage";
				String CheckoutSummaryPage = ADDON_PREFIX + "pages/checkout/multi/checkoutSummaryPage";
				String AddEditDeliveryAddressPage = ADDON_PREFIX + "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = ADDON_PREFIX + "pages/checkout/multi/chooseDeliveryMethodPage";
				String AddPaymentMethodPage = ADDON_PREFIX + "pages/checkout/multi/addPaymentMethodPage";
				String SilentOrderPostPage = "pages/checkout/multi/silentOrderPostPage";
				String HostedOrderPostPage = STOREFRONT_PREFIX + "pages/checkout/multi/hostedOrderPostPage";
				String CheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/checkoutConfirmationPage";
				String ReplenishmentCheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/replenishmentCheckoutConfirmationPage";
				String AccountLayoutPage = "pages/account/accountLayoutPage";
			}

			interface Account
			{
				String AccountLayoutPage = "pages/account/accountLayoutPage";
			}
		}
	}
}
