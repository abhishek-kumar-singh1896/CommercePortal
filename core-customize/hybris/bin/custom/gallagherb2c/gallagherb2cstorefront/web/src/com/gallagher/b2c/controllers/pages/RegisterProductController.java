/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2c.controllers.ControllerConstants;
import com.gallagher.b2c.form.RegisterProductForm;
import com.gallagher.b2c.form.RegisterProductPopupForm;
import com.gallagher.b2c.response.RPFormResponseData;
import com.gallagher.b2c.response.RPFormResponseStatus;
import com.gallagher.b2c.validators.RegisterProductValidator;
import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.gallagher.outboundservices.service.GallagheroutboundservicesService;




/**
 * @author shilpiverma
 *
 */

@Controller
@RequestMapping(value = "/register-product")
public class RegisterProductController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(RegisterProductController.class);

	private static final String REG_PRODUCTS_PAGE = "regProducts";

	@Resource(name = "registerProductValidator")
	private RegisterProductValidator registerProductValidator;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "gallagheroutboundservicesService")
	private GallagheroutboundservicesService gallagheroutboundservicesService;

	@Resource(name = "registeredProductsFacade")
	private GallagherRegisteredProductsFacade gallagherRegisteredProductsFacade;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;

	@RequestMapping(method = RequestMethod.GET)
	public String doRegisterProduct(final Model model) throws CMSItemNotFoundException
	{
		return getProductRegistrationPage(model);
	}

	@RequestMapping(value = "/registeredProducts", method = RequestMethod.GET)
	public String registeredProductsByUser(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel regProductsPage = getContentPageForLabelOrId(REG_PRODUCTS_PAGE);
		storeCmsPageInModel(model, regProductsPage);
		setUpMetaDataForContentPage(model, regProductsPage);
		model.addAttribute("registeredProducts", gallagherRegisteredProductsFacade.getRegisteredProducts());
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(regProductsPage));
		return getViewForPage(model);
	}

	protected String getProductRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		final Breadcrumb registerBreadcrumbEntry = new Breadcrumb("#",
				getMessageSource().getMessage("header.register.product", null, getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(registerBreadcrumbEntry));
		model.addAttribute("Countries", getI18nService().getAllCountries());
		model.addAttribute(new RegisterProductForm());
		return getView();
	}

	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("registerProduct");
	}

	protected String getView()
	{
		return ControllerConstants.Views.Pages.Product.RegisterProduct;
	}


	@ResponseBody
	@RequestMapping(value = "/verify", method = RequestMethod.POST, produces = "application/json")
	public RPFormResponseData checkRegProduct(@RequestBody
	final RegisterProductForm registerProductForm, final Model model, final BindingResult bindingResult,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		registerProductValidator.validate(registerProductForm, bindingResult);
		final RPFormResponseData jsonResponse = new RPFormResponseData();
		try
		{
			if (bindingResult.hasErrors())
			{
				jsonResponse.setResponseStatus(RPFormResponseStatus.FAILURE);
				populateFieldErrors(jsonResponse, bindingResult.getFieldErrors());
			}
			else
			{
				final String productCode = registerProductForm.getProductSku();
				final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC));
				for (final ImageData data : productData.getImages())
				{
					if (data.getFormat().equals("thumbnail"))
					{
						jsonResponse.setProductImage(data.getUrl());
						jsonResponse.setProductaltText(data.getAltText());
						break;
					}
				}
				jsonResponse.setRegisterProductForm(registerProductForm);
				jsonResponse.setProductCode(productCode);
				jsonResponse.setProductName(productData.getName());
				jsonResponse.setResponseStatus(RPFormResponseStatus.SUCCESS);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.debug("Issue in register product", e);
			jsonResponse.setResponseStatus(RPFormResponseStatus.FAILURE);
		}
		return jsonResponse;
	}


	/**
	 * @param registerProductForm1
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submitRegisterProduct(@ModelAttribute
	final RegisterProductPopupForm registerProductForm1, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		final RegisterProductRequest register = new RegisterProductRequest();
		register.setAddressLine1(registerProductForm1.getAddressLine11());
		register.setAddressLine2(registerProductForm1.getAddressLine21());
		register.setCountry(registerProductForm1.getCountry1());
		register.setDatePurchased(registerProductForm1.getDatePurchased1());
		register.setPhoneNumber(registerProductForm1.getPhoneNumber1());
		register.setPostCode(registerProductForm1.getProductSku1());
		register.setSerialNumber(registerProductForm1.getSerialNumber1());
		register.setProductSku(registerProductForm1.getProductSku1());
		register.setTownCity(registerProductForm1.getTownCity1());
		final String page = getProductRegistrationPage(model);
		final RegisterProductForm rg = new RegisterProductForm();
		try
		{
			gallagheroutboundservicesService.postRegisterProduct(register);
		}
		catch (final UnknownIdentifierException e)
		{
			rg.setAddressLine1(registerProductForm1.getAddressLine11());
			rg.setAddressLine2(registerProductForm1.getAddressLine21());
			rg.setCountry(registerProductForm1.getCountry1());
			rg.setDatePurchased(registerProductForm1.getDatePurchased1());
			rg.setPhoneNumber(registerProductForm1.getPhoneNumber1());
			rg.setPostCode(registerProductForm1.getProductSku1());
			rg.setSerialNumber(registerProductForm1.getSerialNumber1());
			rg.setProductSku(registerProductForm1.getProductSku1());
			rg.setTownCity(registerProductForm1.getTownCity1());
			model.addAttribute(rg);
			GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "registerProduct.error.message.title", null);
			return page;
		}
		model.addAttribute(rg);
		GlobalMessages.addConfMessage(model, "registerProduct.confirmation.message.title");
		return page;
	}


	protected void populateFieldErrors(final RPFormResponseData jsonResponse, final List<FieldError> fieldErrors)
	{
		final Map<String, String> errors = new HashMap<>();
		for (final FieldError error : fieldErrors)
		{
			final Locale currentLocale = getI18nService().getCurrentLocale();
			final String message = getMessageSource().getMessage(error.getCode(), error.getArguments(), currentLocale);
			errors.put(error.getField(), message);
		}
		jsonResponse.setErrorsMap(errors);
	}
}
