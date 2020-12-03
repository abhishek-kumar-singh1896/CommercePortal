/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.enterprisewide.b2badvanceacceleratoraddon.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateQuoteCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.constants.GeneratedB2BAcceleratorServicesConstants.Enumerations.CheckoutPaymentType;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enterprisewide.b2badvance.facades.checkout.flow.B2badvanceCheckoutFacade;
import com.enterprisewide.b2badvanceacceleratoraddon.controllers.B2badvanceacceleratoraddonControllerConstants;
import com.enterprisewide.b2badvanceacceleratoraddon.forms.PaymentTypeForm;
import com.enterprisewide.b2badvanceacceleratoraddon.forms.validation.PaymentTypeFormValidator;
import com.gallagher.facades.GallagherB2BUnitFacade;;
@Controller
@RequestMapping(value = "/checkout/multi/payment-type")
public class PaymentTypeCheckoutStepController extends AbstractCheckoutStepController
{
	private final static String PAYMENT_TYPE = "payment-type";

	@Resource(name = "checkoutFlowFacade")
	private B2badvanceCheckoutFacade checkoutFlowFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "gallagherDefaultB2BCheckoutFacade")
	private CheckoutFacade b2bCheckoutFacade;

	@Resource(name = "paymentTypeFormValidator")
	private PaymentTypeFormValidator paymentTypeFormValidator;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@ModelAttribute("paymentTypes")
	public Collection<B2BPaymentTypeData> getAllB2BPaymentTypes()
	{
		return b2bCheckoutFacade.getPaymentTypes();
	}

	@Resource(name = "b2bUnitFacade")
	private GallagherB2BUnitFacade b2bUnitFacade;




	@ModelAttribute("b2bUnits")
	public List<B2BUnitData> getAllB2BUnitData()
	{

		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final List<B2BUnitData> b2bUnitData = b2bUnitFacade.getAllB2BData(currentCustomer);
		return b2bUnitData;
	}

	@Override
	@RequestMapping(value = "/choose", method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateQuoteCheckoutStep
	@PreValidateCheckoutStep(checkoutStep = PAYMENT_TYPE)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		setCommentQuestionOnCheckout(model);
		model.addAttribute("cartData", cartData);
		model.addAttribute("paymentTypeForm", preparePaymentTypeForm(cartData));
		prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentType.breadcrumb"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		setCheckoutStepLinksForModel(model, getCheckoutStep());

		return B2badvanceacceleratoraddonControllerConstants.Views.Pages.MultiStepCheckout.ChoosePaymentTypePage;
	}

	@RequestMapping(value = "/choose", method = RequestMethod.POST)
	@RequireHardLogIn
	public String choose(@ModelAttribute final PaymentTypeForm paymentTypeForm, final BindingResult bindingResult,
			final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		paymentTypeFormValidator.validate(paymentTypeForm, bindingResult);
		
		setCommentQuestionOnCheckout(model);

		if (bindingResult.hasErrors())
		{
			if (paymentTypeForm.isIndicator() == true)
			{
				GlobalMessages.addErrorMessage(model, "checkout.error.requiredDelivery.Date");
			}
			else
			{
			GlobalMessages.addErrorMessage(model, "checkout.error.paymenttype.formentry.invalid");
			}
			model.addAttribute("paymentTypeForm", paymentTypeForm);
			prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentType.breadcrumb"));
			model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			return B2badvanceacceleratoraddonControllerConstants.Views.Pages.MultiStepCheckout.ChoosePaymentTypePage;
		}

		updateCheckoutCart(paymentTypeForm);

		//checkAndSelectDeliveryAddress(paymentTypeForm);

		return getCheckoutStep().nextStep();
	}

	protected void updateCheckoutCart(final PaymentTypeForm paymentTypeForm)
	{
		final CartData cartData = new CartData();

		// set payment type
		final B2BPaymentTypeData paymentTypeData = new B2BPaymentTypeData();
		paymentTypeData.setCode(paymentTypeForm.getPaymentType());

		cartData.setPaymentType(paymentTypeData);

		// set B2B Unit
		if (CheckoutPaymentType.ACCOUNT.equals(cartData.getPaymentType().getCode()))
		{
			final B2BUnitData unitData = new B2BUnitData();
			unitData.setCode(paymentTypeForm.getB2bUnit());

			cartData.setB2bUnit(unitData);

		}

		// set purchase order number
		if (paymentTypeForm.getPurchaseOrderNumber() == null || paymentTypeForm.getPurchaseOrderNumber().length() == 0)
		{
			cartData.setPurchaseOrderNumber(null);
		}
		else
		{
			cartData.setPurchaseOrderNumber(paymentTypeForm.getPurchaseOrderNumber());
		}

		//set Date in Model
		cartData.setRequiredDeliveryDate(paymentTypeForm.getRequiredDeliveryDate());

		if (paymentTypeForm.getDeliveryInstructions() != null || paymentTypeForm.getDeliveryInstructions().length() != 0)
		{
			cartData.setDeliveryInstructions(paymentTypeForm.getDeliveryInstructions());
		}

		//set Comment in CartModel if Entered
		getCheckoutFlowFacade().setDeliveryInstructions(paymentTypeForm.getDeliveryInstructions());


		b2bCheckoutFacade.updateCheckoutCart(cartData);
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	protected PaymentTypeForm preparePaymentTypeForm(final CartData cartData)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final Date date = new Date();

		final String dateToString = formatter.format(date);
		final PaymentTypeForm paymentTypeForm = new PaymentTypeForm();

		if (cartData.getPaymentType() != null)
		{
			paymentTypeForm.setPaymentType(CheckoutPaymentType.ACCOUNT);
		}


		if (cartData.getB2bUnit() != null && StringUtils.isNotBlank(cartData.getB2bUnit().getCode()))
		{
			paymentTypeForm.setB2bUnit(cartData.getB2bUnit().getCode());
		}
		else if (getAllB2BUnitData() != null)
		{
			paymentTypeForm.setB2bUnit(getAllB2BUnitData().get(0).getCode());
		}

		//set Current Date if availaible in CartData
		if (cartData.getRequiredDeliveryDate() != null && cartData.getRequiredDeliveryDate().length() != 0)
		{
			paymentTypeForm.setRequiredDeliveryDate(cartData.getRequiredDeliveryDate());
		}
		else
		{
			paymentTypeForm.setRequiredDeliveryDate(dateToString);
		}

		// set purchase order number
		/*
		 * paymentTypeForm.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
		 */

		if (cartData.getPurchaseOrderNumber() != null && cartData.getPurchaseOrderNumber().length() != 0)
		{
			paymentTypeForm.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
		}

		if (cartData.getDeliveryInstructions() != null && cartData.getDeliveryInstructions().length() != 0)
		{
			paymentTypeForm.setDeliveryInstructions(cartData.getDeliveryInstructions());
		}

		return paymentTypeForm;
	}



	protected void checkAndSelectDeliveryAddress(final PaymentTypeForm paymentTypeForm)
	{
		if (CheckoutPaymentType.ACCOUNT.equals(paymentTypeForm.getPaymentType()))
		{
			final List<? extends AddressData> deliveryAddresses = getCheckoutFacade().getSupportedDeliveryAddresses(true);
			if (deliveryAddresses.size() == 1)
			{
				getCheckoutFacade().setDeliveryAddress(deliveryAddresses.get(0));
			}
		}
	}
	
	protected void setCommentQuestionOnCheckout(final Model model)
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		for (final OrderEntryData cartEntry : cartData.getEntries()) {
			for (final CategoryData categoryData : cartEntry.getProduct().getCategories()) {
				if (categoryData.getCode().equalsIgnoreCase("Software") && StringUtils.isNotEmpty(categoryData.getCommentsQuestion())) {
					model.addAttribute("commentsQuestion", categoryData.getCommentsQuestion());
					break;
				}
			}
			if(cartEntry.getProduct().getBaseProduct()!=null) {
				final ProductData baseProductData = productFacade.getProductForCodeAndOptions(cartEntry.getProduct().getBaseProduct(),
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
								ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.VARIANT_FULL, ProductOption.DELIVERY_MODE_AVAILABILITY));
				for (final CategoryData categoryData : baseProductData.getCategories()) {
					if (categoryData.getCode().equalsIgnoreCase("Software") && StringUtils.isNotEmpty(categoryData.getCommentsQuestion())) {
						model.addAttribute("commentsQuestion", categoryData.getCommentsQuestion());
						break;
					}
				}
			}
		}
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(PAYMENT_TYPE);
	}

	@Override
	protected B2badvanceCheckoutFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

}
