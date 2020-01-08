package com.braintree.controllers.pages;

import com.braintree.configuration.service.BrainTreeConfigService;
import com.braintree.constants.ControllerConstants;
import com.braintree.controllers.form.BraintreePlaceOrderForm;
import com.braintree.customfield.service.CustomFieldsService;
import com.braintree.facade.BrainTreeB2BCheckoutFacade;
import com.braintree.facade.impl.BrainTreePaymentFacadeImpl;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import com.enterprisewide.b2badvanceacceleratoraddon.controllers.pages.checkout.steps.SummaryCheckoutStepController;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.util.localization.Localization;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.braintree.constants.Braintreeb2baddonWebConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/checkout/multi/summary")
public class BrainTreeSummaryCheckoutStepController extends SummaryCheckoutStepController
{

	private static final Logger LOG = Logger.getLogger(BrainTreeSummaryCheckoutStepController.class);

	private static final String SUMMARY = "summary";

	@Resource(name = "brainTreeB2BCheckoutFacade")
	private BrainTreeB2BCheckoutFacade brainTreeB2BCheckoutFacade;

	@Resource(name = "customFieldsService")
	private CustomFieldsService customFieldsService;

	@Resource(name = "brainTreeConfigService")
	private BrainTreeConfigService brainTreeConfigService;

	@Resource(name = "brainTreePaymentFacadeImpl")
	private BrainTreePaymentFacadeImpl brainTreePaymentFacade;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = SUMMARY)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final String productCode = entry.getProduct().getCode();
				final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.VARIANT_MATRIX_BASE, ProductOption.PRICE_RANGE));
				entry.setProduct(product);
			}
		}

		model.addAttribute("cartData", cartData);
		model.addAttribute("allItems", cartData.getEntries());
		model.addAttribute("deliveryAddress", cartData.getDeliveryAddress());
		model.addAttribute("deliveryMode", cartData.getDeliveryMode());
		model.addAttribute("paymentInfo", cartData.getPaymentInfo());
		// TODO:Make configuration hmc driven than hardcoding in controllers
		model.addAttribute("nDays", getNumberRange(1, 30));
		model.addAttribute("nthDayOfMonth", getNumberRange(1, 31));
		model.addAttribute("nthWeek", getNumberRange(1, 12));
		model.addAttribute("daysOfWeek", getB2BCheckoutFacade().getDaysOfWeekForReplenishmentCheckoutSummary());

		// Only request the security code if the SubscriptionPciOption is set to Default.
		final boolean requestSecurityCode = (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade()
				.getSubscriptionPciOption()));
		model.addAttribute("requestSecurityCode", Boolean.valueOf(requestSecurityCode));

		if (!model.containsAttribute("placeOrderForm"))
		{
			final BraintreePlaceOrderForm placeOrderForm = new BraintreePlaceOrderForm();
			// TODO: Make setting of default recurrence enum value hmc driven rather hard coding in controller
			placeOrderForm.setReplenishmentRecurrence(B2BReplenishmentRecurrenceEnum.MONTHLY);
			placeOrderForm.setnDays("14");
			final List<DayOfWeek> daysOfWeek = new ArrayList<>();
			daysOfWeek.add(DayOfWeek.MONDAY);
			placeOrderForm.setnDaysOfWeek(daysOfWeek);
			model.addAttribute("placeOrderForm", placeOrderForm);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.summary.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		return ControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage;
	}

	@RequestMapping(value = "/braintree/placeOrder")
	@RequireHardLogIn
	public String placeBraintreeOrder(@ModelAttribute("braintreePlaceOrderForm") final BraintreePlaceOrderForm braintreePlaceOrderForm, final Model model,
									  final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, InvalidCartException, CommerceCartModificationException
	{
		if (validateOrderForm(braintreePlaceOrderForm, model))
		{
			return enterStep(model, redirectModel);
		}

		boolean isPaymentAuthorized = false;
		try
		{
			if (braintreePlaceOrderForm.isReplenishmentOrder())
			{
				isPaymentAuthorized = brainTreeB2BCheckoutFacade.authorizePaymentForReplenishment();
			}
			else
			{
				isPaymentAuthorized = brainTreeB2BCheckoutFacade.authorizePayment(braintreePlaceOrderForm.getSecurityCode());
			}
		}
		catch (final AdapterException exception)
		{
			// handle a case braintree errors and display reason of error
			LOG.error(exception.getMessage(), exception);
			GlobalMessages.addErrorMessage(model,
					Localization.getLocalizedString(Braintreeb2baddonWebConstants.ERROR_MESSAGE_WITH_REASON, new Object[]
			{ exception.getMessage() }));
			return enterStep(model, redirectModel);
		}
		if (!isPaymentAuthorized)
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.authorization.failed");
			return enterStep(model, redirectModel);
		}

		final PlaceOrderData placeOrderData = new PlaceOrderData();
		placeOrderData.setNDays(braintreePlaceOrderForm.getnDays());
		placeOrderData.setNDaysOfWeek(braintreePlaceOrderForm.getnDaysOfWeek());
		placeOrderData.setNthDayOfMonth(braintreePlaceOrderForm.getNthDayOfMonth());
		placeOrderData.setNWeeks(braintreePlaceOrderForm.getnWeeks());
		placeOrderData.setReplenishmentOrder(Boolean.valueOf(braintreePlaceOrderForm.isReplenishmentOrder()));
		placeOrderData.setReplenishmentRecurrence(braintreePlaceOrderForm.getReplenishmentRecurrence());
		placeOrderData.setReplenishmentStartDate(braintreePlaceOrderForm.getReplenishmentStartDate());
		placeOrderData.setSecurityCode(braintreePlaceOrderForm.getSecurityCode());
		placeOrderData.setTermsCheck(Boolean.valueOf(braintreePlaceOrderForm.isTermsCheck()));

		final AbstractOrderData orderData;
		try
		{
			orderData = getB2BCheckoutFacade().placeOrder(placeOrderData);
		}
		catch (final EntityValidationException e)
		{
			GlobalMessages.addErrorMessage(model, e.getLocalizedMessage());

			braintreePlaceOrderForm.setTermsCheck(false);
			model.addAttribute(braintreePlaceOrderForm);

			return enterStep(model, redirectModel);
		}
		catch (final Exception e)
		{
			LOG.error("Failed to place Order", e);
			GlobalMessages.addErrorMessage(model, "checkout.placeOrder.failed");
			return enterStep(model, redirectModel);
		}

		return redirectToOrderConfirmationPage(placeOrderData, orderData);
	}

	private Map<String, String> getMergedCustomFields (Map<String, String> customFieldsFromUI)
	{
		Map<String, String> customFields = customFieldsService.getDefaultCustomFieldsMap();

		for (String key: customFieldsFromUI.keySet()) {
			customFields.put(key, customFieldsFromUI.get(key));
		}

		return customFields;
	}

	public CustomFieldsService getCustomFieldsService() {
		return customFieldsService;
	}

	public void setCustomFieldsService(CustomFieldsService customFieldsService) {
		this.customFieldsService = customFieldsService;
	}

	public BrainTreeConfigService getBrainTreeConfigService() {
		return brainTreeConfigService;
	}

	public void setBrainTreeConfigService(BrainTreeConfigService brainTreeConfigService) {
		this.brainTreeConfigService = brainTreeConfigService;
	}

	public BrainTreePaymentFacadeImpl getBrainTreePaymentFacade() {
		return brainTreePaymentFacade;
	}

	public void setBrainTreePaymentFacade(BrainTreePaymentFacadeImpl brainTreePaymentFacade) {
		this.brainTreePaymentFacade = brainTreePaymentFacade;
	}
}
