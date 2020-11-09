/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvanceacceleratoraddon.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enterprisewide.b2badvance.facades.checkout.flow.B2badvanceCheckoutFacade;
import com.enterprisewide.b2badvance.facades.order.B2BAdvanceOrderDetailsFacade;
import com.enterprisewide.b2badvanceacceleratoraddon.controllers.B2badvanceacceleratoraddonControllerConstants;
import com.enterprisewide.b2badvanceacceleratoraddon.forms.PlaceOrderForm;
import com.gallagher.core.events.GallagherB2BOrderConfirmationEvent;


@Controller
@RequestMapping(value = "/checkout/multi/summary")
public class SummaryCheckoutStepController extends AbstractCheckoutStepController
{
	private static final Logger LOG = Logger.getLogger(SummaryCheckoutStepController.class);
	private static final String SUMMARY = "summary";
	private static final String REDIRECT_URL_QUOTE_ORDER_CONFIRMATION = REDIRECT_PREFIX + "/checkout/quote/orderConfirmation/";
	private static final String REDIRECT_URL_REPLENISHMENT_CONFIRMATION = REDIRECT_PREFIX
			+ "/checkout/replenishment/confirmation/";
	private static final String TEXT_STORE_DATEFORMAT_KEY = "text.store.dateformat";
	private static final String DEFAULT_DATEFORMAT = "MM/dd/yyyy";

	@Resource(name = "checkoutFlowFacade")
	private B2badvanceCheckoutFacade checkoutFlowFacade;


	@Resource(name = "b2badvanceOrderDetailsFacade")
	private B2BAdvanceOrderDetailsFacade b2badvanceOrderDetailsFacade;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "userService")
	private UserService userService;

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = SUMMARY)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final String productCode = entry.getProduct().getCode();
				final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode, Arrays.asList(
						ProductOption.BASIC, ProductOption.PRICE, ProductOption.VARIANT_MATRIX_BASE, ProductOption.PRICE_RANGE,
						ProductOption.IMAGES));
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
		final boolean requestSecurityCode = (CheckoutPciOptionEnum.DEFAULT
				.equals(getCheckoutFlowFacade().getSubscriptionPciOption()));
		model.addAttribute("requestSecurityCode", Boolean.valueOf(requestSecurityCode));

		if (!model.containsAttribute("placeOrderForm"))
		{
			final PlaceOrderForm placeOrderForm = new PlaceOrderForm();
			// TODO: Make setting of default recurrence enum value hmc driven rather hard coding in controller
			placeOrderForm.setReplenishmentRecurrence(B2BReplenishmentRecurrenceEnum.MONTHLY);
			placeOrderForm.setnDays("14");
			final List<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();
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
		return B2badvanceacceleratoraddonControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage;
	}

	/**
	 * Need to move out of controller utility method for Replenishment
	 *
	 */
	protected List<String> getNumberRange(final int startNumber, final int endNumber)
	{
		final List<String> numbers = new ArrayList<String>();
		for (int number = startNumber; number <= endNumber; number++)
		{
			numbers.add(String.valueOf(number));
		}
		return numbers;
	}

	@RequestMapping(value = "/placeOrder")
	@RequireHardLogIn
	public String placeOrder(@ModelAttribute("placeOrderForm") final PlaceOrderForm placeOrderForm, final Model model,
			final RedirectAttributes redirectModel)
					throws CMSItemNotFoundException, InvalidCartException, CommerceCartModificationException
	{
		if (validateOrderForm(placeOrderForm, model))
		{
			return enterStep(model, redirectModel);
		}

		// authorize, if failure occurs don't allow to place the order
		boolean isPaymentAuthorized = false;
		try
		{
			isPaymentAuthorized = getCheckoutFacade().authorizePayment(placeOrderForm.getSecurityCode());
		}
		catch (final AdapterException ae)
		{
			// handle a case where a wrong paymentProvider configurations on the store see getCommerceCheckoutService().getPaymentProvider()
			LOG.error(ae.getMessage(), ae);
		}
		if (!isPaymentAuthorized)
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.authorization.failed");
			return enterStep(model, redirectModel);
		}

		final PlaceOrderData placeOrderData = new PlaceOrderData();
		placeOrderData.setNDays(placeOrderForm.getnDays());
		placeOrderData.setNDaysOfWeek(placeOrderForm.getnDaysOfWeek());
		placeOrderData.setNegotiateQuote(placeOrderForm.isNegotiateQuote());
		placeOrderData.setNthDayOfMonth(placeOrderForm.getNthDayOfMonth());
		placeOrderData.setNWeeks(placeOrderForm.getnWeeks());
		placeOrderData.setQuoteRequestDescription(placeOrderForm.getQuoteRequestDescription());
		placeOrderData.setReplenishmentOrder(placeOrderForm.isReplenishmentOrder());
		placeOrderData.setReplenishmentRecurrence(placeOrderForm.getReplenishmentRecurrence());
		placeOrderData.setReplenishmentStartDate(placeOrderForm.getReplenishmentStartDate());
		placeOrderData.setSecurityCode(placeOrderForm.getSecurityCode());
		placeOrderData.setTermsCheck(placeOrderForm.isTermsCheck());

		//getCheckoutFlowFacade().setDeliveryInstructions(placeOrderForm.getDeliveryInstructions());


		final AbstractOrderData orderData;
		try
		{
			orderData = getB2BCheckoutFacade().placeOrder(placeOrderData);
		}
		catch (final EntityValidationException e)
		{
			LOG.error("Failed to place Order", e);
			GlobalMessages.addErrorMessage(model, e.getLocalizedMessage());

			placeOrderForm.setTermsCheck(false);
			placeOrderForm.setNegotiateQuote(false);
			model.addAttribute(placeOrderForm);

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

	/**
	 * Validates the order form before to filter out invalid order states
	 *
	 * @param placeOrderForm
	 *           The spring form of the order being submitted
	 * @param model
	 *           A spring Model
	 * @return True if the order form is invalid and false if everything is valid.
	 */
	protected boolean validateOrderForm(final PlaceOrderForm placeOrderForm, final Model model)
	{
		final String securityCode = placeOrderForm.getSecurityCode();
		boolean invalid = false;

		if (getCheckoutFlowFacade().hasNoDeliveryAddress())
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryAddress.notSelected");
			invalid = true;
		}

		if (getCheckoutFlowFacade().hasNoDeliveryMode())
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryMethod.notSelected");
			invalid = true;
		}

		if (getCheckoutFlowFacade().hasNoPaymentInfo())
		{
			GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.notSelected");
			invalid = true;
		}
		else
		{
			// Only require the Security Code to be entered on the summary page if the SubscriptionPciOption is set to Default.
			if (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade().getSubscriptionPciOption())
					&& StringUtils.isBlank(securityCode))
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.noSecurityCode");
				invalid = true;
			}
		}

		if (!placeOrderForm.isTermsCheck())
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.terms.not.accepted");
			invalid = true;
			return invalid;
		}
		final CartData cartData = getCheckoutFacade().getCheckoutCart();

		/*
		 * if (!getCheckoutFacade().containsTaxValues()) { LOG.error(String.format(
		 * "Cart %s does not have any tax values, which means the tax cacluation was not properly done, placement of order can't continue"
		 * , cartData.getCode())); GlobalMessages.addErrorMessage(model, "checkout.error.tax.missing"); invalid = true; }
		 */

		if (!cartData.isCalculated())
		{
			LOG.error(
					String.format("Cart %s has a calculated flag of FALSE, placement of order can't continue", cartData.getCode()));
			GlobalMessages.addErrorMessage(model, "checkout.error.cart.notcalculated");
			invalid = true;
		}

		if (placeOrderForm.isNegotiateQuote() && !cartData.getQuoteAllowed())
		{
			LOG.error(String.format("Cart %s has the quote allowed flag set to FALSE, placement of quote can't continue",
					cartData.getCode()));
			GlobalMessages.addErrorMessage(model, "checkout.error.quote.requirements");
			invalid = true;
			// Reset negotiate quote flag, this will allow the user to place an order if needed
			placeOrderForm.setNegotiateQuote(false);
		}

		return invalid;
	}

	public B2BAdvanceOrderDetailsFacade getB2badvanceOrderDetailsFacade()
	{
		return b2badvanceOrderDetailsFacade;
	}

	public void setB2badvanceOrderDetailsFacade(final B2BAdvanceOrderDetailsFacade b2badvanceOrderDetailsFacade)
	{
		this.b2badvanceOrderDetailsFacade = b2badvanceOrderDetailsFacade;
	}

	protected CheckoutFacade getB2BCheckoutFacade()
	{
		return (CheckoutFacade) this.getCheckoutFacade();
	}

	protected String redirectToOrderConfirmationPage(final PlaceOrderData placeOrderData, final AbstractOrderData orderData)
	{
		final OrderModel model = getB2badvanceOrderDetailsFacade().getB2bOrderService().getOrderByCode(orderData.getCode());


		if (Boolean.TRUE.equals(placeOrderData.getNegotiateQuote()))
		{
			return REDIRECT_URL_QUOTE_ORDER_CONFIRMATION + orderData.getCode();
		}
		else if (Boolean.TRUE.equals(placeOrderData.getReplenishmentOrder()) && (orderData instanceof ScheduledCartData))
		{
			return REDIRECT_URL_REPLENISHMENT_CONFIRMATION + ((ScheduledCartData) orderData).getJobCode();
		}

		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();

		if (model != null)
		{
			eventService.publishEvent(initializeEvent(
					new GallagherB2BOrderConfirmationEvent(model, model.getStore(), model.getSite(), model.getCurrency()),
					currentUser));
		}


		return REDIRECT_URL_ORDER_CONFIRMATION + orderData.getCode();
	}



	/**
	 * @param orderPlacedEvent
	 * @param currentUser
	 * @return
	 */
	private AbstractCommerceUserEvent initializeEvent(final GallagherB2BOrderConfirmationEvent orderEvent,
			final CustomerModel currentUser)
	{
		orderEvent.setCustomer(currentUser);
		return orderEvent;
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

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(SUMMARY);
	}

	@Override
	protected B2badvanceCheckoutFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	@InitBinder
	protected void initBinder(final ServletRequestDataBinder binder)//preprocess the web request
	{
		final Locale currentLocale = getI18nService().getCurrentLocale();
		final String formatString = getMessageSource().getMessage(TEXT_STORE_DATEFORMAT_KEY, null, DEFAULT_DATEFORMAT,
				currentLocale);
		final DateFormat dateFormat = new SimpleDateFormat(formatString, currentLocale);
		final CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, editor);
	}




}
