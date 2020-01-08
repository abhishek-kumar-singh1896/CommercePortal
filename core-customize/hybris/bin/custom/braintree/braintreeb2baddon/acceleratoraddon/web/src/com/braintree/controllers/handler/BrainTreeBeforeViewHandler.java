/**
 *
 */
package com.braintree.controllers.handler;

import static com.braintree.constants.Braintreeb2baddonWebConstants.*;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.CLIENT_TOKEN;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.PAY_PAL_CHECKOUT_DATA;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.PAY_PAL_CONFIGURATION_DATA;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.Views.Pages.Checkout.ReplenishmentCheckoutConfirmationPage;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import braintreehac.services.PayPalButtonConfigurationService;
import de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee;
import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.config.ConfigIntf;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.braintree.configuration.BrainTreeConfigurationListener;
import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.configuration.service.BrainTreeSupportedLocaleConfig;
import com.braintree.constants.ControllerConstants;
import com.braintree.controllers.Braintreeb2baddonControllerConstants;
import com.braintree.facade.BrainTreeAccountFacade;
import com.braintree.facade.BrainTreeUserFacade;
import com.braintree.facade.impl.BrainTreeCheckoutFacade;
import com.braintree.facade.impl.BrainTreePaymentFacadeImpl;
import com.braintree.hybris.data.BrainTreePaymentInfoData;
import com.braintree.hybris.data.PayPalCheckoutData;
import com.braintree.hybris.data.PayPalConfigurationData;

public class BrainTreeBeforeViewHandler implements BeforeViewHandlerAdaptee

{
    private final static Logger LOG = Logger.getLogger(BrainTreeBeforeViewHandler.class);
    private I18NService i18NService;

    private static final String CART_PAGE = "pages/cart/cartPage";
    private static final String BRAIN_TREE_PAYMENT_DATA = "brainTreePaymentInfoData";
    private static final String ORDER_DATA = "orderData";
    private static final String B2C_ADD_TO_CART_POPUP_PAGE = "fragments/cart/addToCartPopup";
    private static final String B2C_CART_POPUP_PAGE = "fragments/cart/cartPopup";
    private static final String AccountLayoutPage = "pages/account/accountLayoutPage";
    private static final String CART_BUTTON_CONFIG = "cart.button.config";
    private static final String MARK_BUTTON_CONFIG = "mark.button.config";
    private static final String MINI_CART_BUTTON_CONFIG = "mini.cart.button.config";
    private static final String PAYPAL_BUTTON_CONFIG = "payPalButtonConfig";
    private static final String PAYPAL_MARK_BUTTON_CONFIG = "payPalMarkButtonConfig";
    private static final String B2C_CHECKOUT_CONFIRMATION_LAYOUT_PAGE = "pages/checkout/checkoutConfirmationLayoutPage";
    private static final String B2C_CHECKOUT_DELIVERY_ADDRESS_PAGE = "pages/checkout/multi/addEditDeliveryAddressPage";

    @Resource(name = "brainTreeCheckoutFacade")
    private BrainTreeCheckoutFacade brainTreeCheckoutFacade;
    @Resource(name = "brainTreePaymentFacadeImpl")
    private BrainTreePaymentFacadeImpl brainTreePaymentFacade;
    @Resource(name = "brainTreeUserFacade")
    private BrainTreeUserFacade brainTreeUserFacade;
    @Resource(name = "brainTreeConfigService")
    private BrainTreeConfigService brainTreeConfigService;
    @Resource(name = "brainTreeAccountFacade")
    private BrainTreeAccountFacade brainTreeAccountFacade;
    @Autowired
    private PayPalButtonConfigurationService payPalButtonConfigurationService;
    @Autowired
    private SessionService sessionService;

    private ConfigIntf.ConfigChangeListener configurationChangeListener;

    @Override
    public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model, final String viewName)
            throws Exception {

        if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) && Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled()) &&
                Boolean.FALSE.equals(brainTreeConfigService.getPayPalExpressEnabled())) {
            return viewName;
        }
        return handleBrainTreeCheckoutScenario(model, viewName);

    }

    private String handleBrainTreeCheckoutScenario(final ModelMap model, final String viewName) {

        if (configurationChangeListener == null) {
            registerConfigChangeLister();
        }

        if (CART_PAGE.equals(viewName)) {
            setPayPalExpressEnabled(model);
            fillPaymentMethodsInfo(model);
            model.addAttribute(PAYPAL_BUTTON_CONFIG, payPalButtonConfigurationService.getFormattedProperty(CART_BUTTON_CONFIG));
        }

        else if (ControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage.equals(viewName)) {
            if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) &&
                    Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled())) {
                return viewName;
            }
            setHostedFieldEnabled(model);
            fillPaymentMethodsInfo(model);
            final Map<String, String> paymentsImagesURL = brainTreeCheckoutFacade.getAcceptedPaymentMethodImages();
            model.addAttribute(ACCEPTED_PAYMENTS_METHODS_IMAGES_URL, paymentsImagesURL);
            model.addAttribute(PAYPAL_MARK_BUTTON_CONFIG, payPalButtonConfigurationService.getFormattedProperty(MARK_BUTTON_CONFIG));
            return SilentOrderPostPage;
        }

        else if (ControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage.equals(viewName)) {
            if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) &&
                    Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled())) {
                return viewName;
            }
            final BrainTreePaymentInfoData brainTreePaymentInfoData = brainTreePaymentFacade.getBrainTreePaymentInfoData();

            if (brainTreePaymentInfoData != null) {
                model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentInfoData);
            }
            return CheckoutSummaryPage;
        }

        else if (ControllerConstants.Views.Pages.MultiStepCheckout.CheckoutConfirmationPage.equals(viewName)) {
            if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) &&
                    Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled())) {
                return viewName;
            }
            final String orderCode = ((OrderData) model.get(ORDER_DATA)).getCode();
            if (isNotBlank(orderCode)) {
                final BrainTreePaymentInfoData brainTreePaymentInfoData = brainTreePaymentFacade.getBrainTreePaymentInfoData(orderCode);
                if (brainTreePaymentInfoData != null) {
                    model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentInfoData);
                }
            }
            return CheckoutConfirmationPage;
        }

        else if (ControllerConstants.Views.Pages.MultiStepCheckout.ReplenishmentCheckoutConfirmationPage.equals(viewName)) {
            if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) &&
                    Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled())) {
                return viewName;
            }

            final String cartCode = ((ScheduledCartData) model.get(ORDER_DATA)).getCode();
            if (isNotBlank(cartCode)) {
                model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentFacade.getBrainTreePaymentInfoDataByCart(cartCode));
            }
            return ReplenishmentCheckoutConfirmationPage;
        }

        else if (ControllerConstants.Views.Pages.MultiStepCheckout.AccountLayoutPage.equals(viewName)) {
            // add BRAIN_TREE_PAYMENT_DATA for order view page
            if (model.get(ORDER_DATA) instanceof ScheduledCartData) {
                final String cartCode = ((ScheduledCartData) model.get(ORDER_DATA)).getCode();
                if (isNotBlank(cartCode)) {
                    model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentFacade.getBrainTreePaymentInfoDataByCart(cartCode));
                }
            } else {
                final OrderData orderData = (OrderData) model.get(ORDER_DATA);
                if (orderData != null) {
                    final String orderCode = orderData.getCode();
                    if (isNotBlank(orderCode)) {
                        model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentFacade.getBrainTreePaymentInfoData(orderCode));
                        return viewName;
                    }
                }
            }

            if (Boolean.FALSE.equals(brainTreeConfigService.getPayPalStandardEnabled()) &&
                    Boolean.FALSE.equals(brainTreeConfigService.getHostedFieldEnabled())) {
                return viewName;
            }
            setHostedFieldEnabled(model);
            final PayPalConfigurationData payPalConfigurationData = brainTreeAccountFacade.getPayPalConfigurationData();
            model.addAttribute(PAY_PAL_CONFIGURATION_DATA, payPalConfigurationData);
            final Map<String, String> paymentsImagesURL = brainTreeCheckoutFacade.getAcceptedPaymentMethodImages();
            model.addAttribute(ACCEPTED_PAYMENTS_METHODS_IMAGES_URL, paymentsImagesURL);
            model.addAttribute(PAYMENT_INFOS, brainTreeUserFacade.getBrainTreeCCPaymentInfos(true));
            model.addAttribute(BILLING_AGREEMENT_DESCRIPTION, brainTreeConfigService.getBillingAgreementDescription());
            return AccountLayoutPage;
        }

        else if (B2C_CHECKOUT_CONFIRMATION_LAYOUT_PAGE.equals(viewName)) {
            if (model.get(ORDER_DATA) instanceof ScheduledCartData) {
                final String cartCode = ((ScheduledCartData) model.get(ORDER_DATA)).getCode();
                if (isNotBlank(cartCode)) {
                    model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentFacade.getBrainTreePaymentInfoDataByCart(cartCode));
                }
            } else {
                final String orderCode = ((OrderData) model.get(ORDER_DATA)).getCode();
                if (isNotBlank(orderCode)) {
                    model.addAttribute(BRAIN_TREE_PAYMENT_DATA, brainTreePaymentFacade.getBrainTreePaymentInfoData(orderCode));
                }
            }
            return viewName;
        }

        else if (B2C_ADD_TO_CART_POPUP_PAGE.equals(viewName)) {
            if (brainTreeConfigService.getPayPalExpressEnabled().booleanValue()) {
                fillPaymentMethodsInfo(model);
                model.addAttribute(PAYPAL_BUTTON_CONFIG, payPalButtonConfigurationService.getFormattedProperty(MINI_CART_BUTTON_CONFIG));
                return Braintreeb2baddonControllerConstants.Views.Fragments.Cart.AddToCartPopup;
            }
            return viewName;
        }

        else if (B2C_CART_POPUP_PAGE.equals(viewName)) {
            if (brainTreeConfigService.getPayPalExpressEnabled().booleanValue()) {
                fillPaymentMethodsInfo(model);
                model.addAttribute(PAYPAL_BUTTON_CONFIG, payPalButtonConfigurationService.getFormattedProperty(MINI_CART_BUTTON_CONFIG));
                return Braintreeb2baddonControllerConstants.Views.Fragments.Cart.CartPopup;
            }
            return viewName;
        }

        else if (B2C_CHECKOUT_DELIVERY_ADDRESS_PAGE.equals(viewName) && sessionService.getAttribute("braintree.general.error.shippingAddress") != null){
            model.addAttribute("accErrorMsgs", sessionService.getAttribute("braintree.general.error.shippingAddress"));
            sessionService.removeAttribute("braintree.general.error.shippingAddress");
        }

        return viewName;
    }

    private void setPayPalExpressEnabled(final ModelMap model) {
        model.addAttribute(PAY_PAL_EXPRESS_ENABLE, brainTreeConfigService.getPayPalExpressEnabled());
    }

    private void setHostedFieldEnabled(final ModelMap model) {
        model.addAttribute(HOSTED_FIELDS_ENABLE, brainTreeConfigService.getHostedFieldEnabled());
        model.addAttribute(PAY_PAL_STANDARD_ENABLE, brainTreeConfigService.getPayPalStandardEnabled());
        model.addAttribute(APPLE_PAY_ENABLE, brainTreeConfigService.getApplePayEnabled());
    }

    private void fillPaymentMethodsInfo(final ModelMap model) {
        String clientToken = StringUtils.EMPTY;

        try {
            clientToken = brainTreeCheckoutFacade.generateClientToken();
        } catch (final AdapterException exception) {
            LOG.error("[View Handler] Error during token generation!");
        }
        final PayPalCheckoutData payPalCheckoutData = brainTreeCheckoutFacade.getPayPalCheckoutData();
        setBraintreeLocale(model);
        model.addAttribute(CLIENT_TOKEN, clientToken);
        model.addAttribute(PAY_PAL_CHECKOUT_DATA, payPalCheckoutData);
        model.addAttribute(PAYMENT_INFOS, brainTreeUserFacade.getBrainTreeCCPaymentInfos(true));
        model.addAttribute(APPLE_PAY_ENABLE,brainTreeConfigService.getApplePayEnabled());
        model.addAttribute(BILLING_AGREEMENT_DESCRIPTION, brainTreeConfigService.getBillingAgreementDescription());
    }

    private void registerConfigChangeLister() {
        final ConfigIntf config = Registry.getMasterTenant().getConfig();
        configurationChangeListener = new BrainTreeConfigurationListener();
        config.registerConfigChangeListener(configurationChangeListener);
    }

	private void setBraintreeLocale(final ModelMap model)
	{
		final String brainTreeLocale = brainTreeConfigService.getBrainTreeLocale();
		try
		{
			if (StringUtils.isNotEmpty(brainTreeLocale) && BrainTreeSupportedLocaleConfig.supportLocale(brainTreeLocale))
			{
				model.addAttribute(BRAINTREE_LOCALE, brainTreeLocale);
			}
			else
			{
				final Locale locale = BrainTreeSupportedLocaleConfig
						.getSupportedLocaleByLanguage(getI18NService().getCurrentLocale().getLanguage());
				model.addAttribute(BRAINTREE_LOCALE, locale);
			}
		}
		catch (final Exception e)
		{
			model.addAttribute(BRAINTREE_LOCALE, BrainTreeSupportedLocaleConfig.getDefaultLocale(getI18NService().getCurrentLocale().getLanguage()));
		}
	}

	public I18NService getI18NService()
	{
		return i18NService;
	}

	public void setI18NService(I18NService i18nService)
	{
		i18NService = i18nService;
	}

    public PayPalButtonConfigurationService getPayPalButtonConfigurationService() {
        return payPalButtonConfigurationService;
    }

    public void setPayPalButtonConfigurationService(PayPalButtonConfigurationService payPalButtonConfigurationService) {
        this.payPalButtonConfigurationService = payPalButtonConfigurationService;
    }
}
