package com.braintree.controllers.pages;

import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.constants.Braintreeb2baddonWebConstants;
import com.braintree.constants.BraintreeConstants;
import com.braintree.controllers.Braintreeb2baddonControllerConstants;
import com.braintree.facade.BrainTreeUserFacade;
import com.braintree.facade.impl.BrainTreeCheckoutFacade;
import com.braintree.facade.impl.BrainTreePaymentFacadeImpl;
import com.braintree.hybris.data.BrainTreeSubscriptionInfoData;
import com.braintree.hybris.data.PayPalCheckoutData;
import com.braintree.payment.validators.BrainTreePaymentDetailsValidator;
import com.google.common.collect.Lists;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.SopPaymentDetailsForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.payment.AdapterException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.braintree.constants.Braintreeb2baddonWebConstants.*;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.*;
import static com.braintree.controllers.Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.CheckoutOrderPageErrorPage;
import static de.hybris.platform.util.localization.Localization.getLocalizedString;


@Controller
@RequestMapping(value = "/braintree/checkout/hop")
public class BrainTreePaymentController extends AbstractCheckoutStepController
{

	private static final Logger LOG = Logger.getLogger(BrainTreePaymentController.class);
	private final static String PAYMENT_METHOD = "payment-method";

	@Resource(name = "brainTreePaymentFacadeImpl")
	private BrainTreePaymentFacadeImpl brainTreePaymentFacade;
	@Resource(name = "brainTreeCheckoutFacade")
	private BrainTreeCheckoutFacade brainTreeCheckoutFacade;
	@Resource(name = "brainTreePaymentDetailsValidator")
	private BrainTreePaymentDetailsValidator brainTreePaymentDetailsValidator;
	@Resource(name = "brainTreeUserFacade")
	private BrainTreeUserFacade brainTreeUserFacade;
	@Resource(name = "brainTreeConfigService")
	private BrainTreeConfigService brainTreeConfigService;

	@RequestMapping(value = "/response", method = RequestMethod.POST)
	@RequireHardLogIn
	public String enterStep(@RequestParam(value = "bt_payment_method_nonce") final String nonce,
							@RequestParam(value = "use_delivery_address") final String useBillingAddress,
							@RequestParam(value = "payment_type") final String paymentProvider,
							@RequestParam(value = "paypal_email") final String payPalEmail,
							@RequestParam(value = "card_type") final String cardType,
							@RequestParam(value = "card_details") final String cardDetails,
							@RequestParam(value = "device_data") final String deviceData,
							@RequestParam(value = "liability_shifted") final String liabilityShifted,
							@RequestParam(value = "cardholder", required = false) final String cardholder,
							@Valid final SopPaymentDetailsForm sopPaymentDetailsForm, final BindingResult bindingResult, final Model model,
							final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, CommerceCartModificationException
	{
		if (StringUtils.isEmpty(nonce))
		{
			handleErrors(GENERAL_HEAD_ERROR_MESSAGE, model);
			return CheckoutOrderPageErrorPage;
		}

		if (sopPaymentDetailsForm.isSavePaymentInfo() && BraintreeConstants.APPLE_PAY_PAYMENT.equals(paymentProvider))
		{
			sopPaymentDetailsForm.setSavePaymentInfo(false);
		}

		setupAddPaymentPage(model);
		setupParametersSilentOrderPostPage(sopPaymentDetailsForm, model, paymentProvider,
				String.valueOf(sopPaymentDetailsForm.isSavePaymentInfo()));

		final BrainTreeSubscriptionInfoData subscriptionInfo = buildSubscriptionInfo(nonce, paymentProvider, cardDetails, cardType,
				payPalEmail, deviceData, liabilityShifted, sopPaymentDetailsForm.isSavePaymentInfo(), cardholder);

		try
		{
			setupSilentOrderPostPage(sopPaymentDetailsForm, model);
		}
		catch (final Exception e)
		{
			LOG.error("Failed to build beginCreateSubscription request", e);
			GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
			return enterStep(model, redirectAttributes);
		}

		if (!checkSavePaymentForCurrentConfig(sopPaymentDetailsForm.isSavePaymentInfo()))
		{
			LOG.error("It is impossible to save payment method when braintree.store.in.vault property is false");
			GlobalMessages.addErrorMessage(model, getLocalizedString("text.account.profile.paymentCart.addPaymentMethod.forbidden"));
			return Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
		}

		if (sopPaymentDetailsForm.isSavePaymentInfo() && BraintreeConstants.PAYPAL_INTENT_ORDER.equalsIgnoreCase(brainTreeConfigService.getIntent()))
		{
			LOG.error("It is impossible to save payment method when braintree.intent property is set to 'order'");
			GlobalMessages.addErrorMessage(model, getLocalizedString("text.account.profile.paymentCart.addPaymentMethod.forbidden"));
			return Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
		}

		List<PaymentErrorField> errorFields = Lists.newArrayList();

		if (Boolean.TRUE.toString().equals(useBillingAddress))
		{
			try
			{
				brainTreePaymentFacade.completeCreateSubscription(subscriptionInfo);
			}
			catch (final Exception exception)
			{
				GlobalMessages.addErrorMessage(model, getLocalizedString("braintree.billing.general.error"));
				return Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
			}
		}
		else
		{
			final AddressData addressData = interpretResponseAddressData(sopPaymentDetailsForm);
			subscriptionInfo.setAddressData(addressData);

			errorFields = brainTreePaymentDetailsValidator.validatePaymentDetails(addressData, bindingResult);

			if (errorFields.isEmpty())
			{
				try
				{
					brainTreePaymentFacade.completeCreateSubscription(subscriptionInfo);
				}
				catch (final Exception exception)
				{
					GlobalMessages.addErrorMessage(model, getLocalizedString("braintree.billing.general.error"));
					return Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
				}
			}
			else
			{
				GlobalMessages.addErrorMessage(model, getLocalizedString("braintree.billing.address.error"));
				return Braintreeb2baddonControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
			}

		}

		return getCheckoutStep().nextStep();
	}

	private boolean checkSavePaymentForCurrentConfig(boolean savePayment)
	{
		return isAvailableSavePayment(savePayment);
	}

	private boolean isAvailableSavePayment (boolean savePayment)
	{
		String storeInVault = brainTreeConfigService.getStoreInVaultForCurrentUser();
		boolean config = !Boolean.FALSE.toString().equals(storeInVault);
		return !(!config && savePayment);
	}

	protected void setupSilentOrderPostPage(final SopPaymentDetailsForm sopPaymentDetailsForm, final Model model)
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddress", cartData.getDeliveryAddress());
		if (StringUtils.isNotBlank(sopPaymentDetailsForm.getBillTo_country()))
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(sopPaymentDetailsForm.getBillTo_country()));
			model.addAttribute("country", sopPaymentDetailsForm.getBillTo_country());
		}
	}

	protected CardTypeData createCardTypeData(final String code, final String name)
	{
		final CardTypeData cardTypeData = new CardTypeData();
		cardTypeData.setCode(code);
		cardTypeData.setName(name);
		return cardTypeData;
	}

	private void setupParametersSilentOrderPostPage(final SopPaymentDetailsForm sopPaymentDetailsForm, final Model model,
													final String paymentProvider, final String isSingleUseSelected)
	{
		setupSilentOrderPostPage(sopPaymentDetailsForm, model);
		final PayPalCheckoutData payPalCheckoutData = brainTreeCheckoutFacade.getPayPalCheckoutData();

		String clientToken = StringUtils.EMPTY;

		try
		{
			clientToken = brainTreeCheckoutFacade.generateClientToken();
		}
		catch (final AdapterException exception)
		{
			LOG.error("[Brain Tree Controller] Error during token generation!");
		}

		model.addAttribute(CLIENT_TOKEN, clientToken);
		model.addAttribute(PAY_PAL_CHECKOUT_DATA, payPalCheckoutData);
		model.addAttribute(HOSTED_FIELDS_ENABLE, brainTreeConfigService.getHostedFieldEnabled());
		model.addAttribute(PAY_PAL_STANDARD_ENABLE, brainTreeConfigService.getHostedFieldEnabled());
		model.addAttribute(PAYMENT_INFOS, brainTreeUserFacade.getBrainTreeCCPaymentInfos(true));
		final Map<String, String> paymentsImagesURL = brainTreeCheckoutFacade.getAcceptedPaymentMethodImages();
		model.addAttribute(ACCEPTED_PAYMENTS_METHODS_IMAGES_URL, paymentsImagesURL);

		if (BraintreeConstants.BRAINTREE_PAYMENT.equals(paymentProvider))
		{
			model.addAttribute(IS_ADDRESS_OPEN, Boolean.TRUE);
		}
	}

	protected void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "noindex,nofollow");
		model.addAttribute("hasNoPaymentInfo", Boolean.valueOf(getCheckoutFlowFacade().hasNoPaymentInfo()));
		prepareDataForPage(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		final ContentPageModel contentPage = getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
	}

	private AddressData interpretResponseAddressData(final SopPaymentDetailsForm sopPaymentDetailsForm)
	{
		final AddressData address = new AddressData();
		final CountryData country = new CountryData();
		country.setIsocode(sopPaymentDetailsForm.getBillTo_country());
		address.setCountry(country);
		final RegionData region = new RegionData();
		region.setIsocode(sopPaymentDetailsForm.getBillTo_state());
		address.setTitleCode(sopPaymentDetailsForm.getBillTo_titleCode());
		address.setFirstName(sopPaymentDetailsForm.getBillTo_firstName());
		address.setLastName(sopPaymentDetailsForm.getBillTo_lastName());
		address.setTown(sopPaymentDetailsForm.getBillTo_city());
		address.setLine1(sopPaymentDetailsForm.getBillTo_street1());
		address.setLine2(sopPaymentDetailsForm.getBillTo_street2());
		address.setPostalCode(sopPaymentDetailsForm.getBillTo_postalCode());
		return address;
	}

	private BrainTreeSubscriptionInfoData buildSubscriptionInfo(final String nonce, final String paymentProvider,
																final String cardDetails, final String cardType, final String email, final String deviceData,
																final String liabilityShifted, final boolean isPaymentInfoSaved, final String cardholder)
	{
		final BrainTreeSubscriptionInfoData subscriptionInfo = new BrainTreeSubscriptionInfoData();
		subscriptionInfo.setPaymentProvider(paymentProvider);
		subscriptionInfo.setCardNumber(cardDetails);
		subscriptionInfo.setDeviceData(deviceData);
		subscriptionInfo.setCardType(cardType);
		subscriptionInfo.setNonce(nonce);
		subscriptionInfo.setEmail(email);
		subscriptionInfo.setSavePaymentInfo(isPaymentInfoSaved);
		subscriptionInfo.setCardholder(cardholder);
		if (StringUtils.isNotBlank(liabilityShifted))
		{
			subscriptionInfo.setLiabilityShifted(Boolean.valueOf(liabilityShifted));
		}
		return subscriptionInfo;
	}

	private void handleErrors(final String errorsDetail, final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("errorsDetail", getLocalizedString(errorsDetail));
		final String redirectUrl = REDIRECT_URL_CART;
		model.addAttribute("redirectUrl", redirectUrl.replace(REDIRECT_PREFIX, ""));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.hostedOrderPageError.breadcrumb"));
		storeCmsPageInModel(model, getContentPageForLabelOrId(Braintreeb2baddonWebConstants.MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model,
				getContentPageForLabelOrId(Braintreeb2baddonWebConstants.MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));

		GlobalMessages.addErrorMessage(model, getLocalizedString(GENERAL_HEAD_ERROR));
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(PAYMENT_METHOD);
	}

	//replaced by customized method with multi args
	@Override
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		return StringUtils.EMPTY;
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

}
