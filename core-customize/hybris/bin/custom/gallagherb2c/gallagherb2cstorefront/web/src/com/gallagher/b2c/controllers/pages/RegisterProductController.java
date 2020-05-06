/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
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
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
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
import com.gallagher.b2c.response.RPFormResponseData;
import com.gallagher.b2c.response.RPFormResponseStatus;
import com.gallagher.b2c.util.GallagherProductRegistrationUtil;
import com.gallagher.b2c.validators.RegisterProductValidator;
import com.gallagher.facades.GallagherRegisteredProductsFacade;
import com.gallagher.outboundservices.request.dto.RegisterProductRequest;
import com.hybris.charon.exp.InternalServerException;


/**
 * @author shilpiverma
 *
 */

@Controller
@RequestMapping(value = "/register-product")
public class RegisterProductController extends AbstractPageController
{
	private static final String REDIRECT_TO_REGISTER_PRODUCT = REDIRECT_PREFIX + "/register-product";

	private static final String REDIRECT_TO_REGISTERED_PRODUCT = REDIRECT_PREFIX + "/register-product/products";

	private static final Logger LOG = Logger.getLogger(RegisterProductController.class);

	private static final String REG_PRODUCTS_PAGE = "regProducts";

	private static final String IMPORT_FILE_MAX_SIZE_BYTES_KEY = "import.product.receipt.max.file.size.bytes";

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "registerProductValidator")
	private RegisterProductValidator registerProductValidator;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "registeredProductsFacade")
	private GallagherRegisteredProductsFacade gallagherRegisteredProductsFacade;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;

	@Resource(name = "productService")
	private ProductService productService;

	String imageUrl;

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@RequireHardLogIn
	public String registeredProductsByUser(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel regProductsPage = getContentPageForLabelOrId(REG_PRODUCTS_PAGE);
		storeCmsPageInModel(model, regProductsPage);
		setUpMetaDataForContentPage(model, regProductsPage);
		model.addAttribute("registeredProducts", gallagherRegisteredProductsFacade.getRegisteredProducts());
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(regProductsPage));

		return getViewForPage(model);
	}

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String doRegisterProduct(final Model model) throws CMSItemNotFoundException
	{
		return getProductRegistrationPage(model);
	}

	protected String getProductRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		final Breadcrumb registerBreadcrumbEntry = new Breadcrumb("#",
				getMessageSource().getMessage("header.register.product", null, getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(registerBreadcrumbEntry));

		final List<CountryModel> countries = getI18nService().getAllCountries().stream()
				.sorted((e1, e2) -> e1.getName().compareTo(e2.getName())).collect(Collectors.toList());

		model.addAttribute("Countries", countries);
		final long fileSie = getSiteConfigService().getLong(IMPORT_FILE_MAX_SIZE_BYTES_KEY, 0);
		model.addAttribute("fileMaxSize", fileSie == 0 ? 0 : (fileSie / 1024) / 1024);

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
				final Collection<ImageData> images = productData.getImages();

				if (CollectionUtils.isNotEmpty(images))
				{
					for (final ImageData data : productData.getImages())
					{
						if (data.getFormat().equals("thumbnail"))
						{
							jsonResponse.setProductImage(data.getUrl());
							jsonResponse.setProductaltText(data.getAltText());
							break;
						}
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
			jsonResponse.setResponseStatus(RPFormResponseStatus.PRODUCTNOTFOUND);
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
	@RequestMapping(method = RequestMethod.POST, value = "/submit")
	@RequireHardLogIn
	public String submitRegisterProduct(@ModelAttribute
	final RegisterProductForm registerProductForm1, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		final RegisterProductRequest request = new RegisterProductRequest();
		GallagherProductRegistrationUtil.convert(registerProductForm1, request, userService.getCurrentUser());
		final RegisterProductForm rg = new RegisterProductForm();
		try
		{
			final HttpStatus status = gallagherRegisteredProductsFacade.registerProduct(request);

			if (HttpStatus.CREATED.equals(status))
			{
				GlobalMessages.addConfMessage(model, "registerProduct.confirmation.message.title");
			}
			else
			{
				populateForm(registerProductForm1, rg);
				GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "registerProduct.error.message.title", null);
			}
		}
		catch (final UnknownIdentifierException | InternalServerException exception)
		{
			populateForm(registerProductForm1, rg);
			GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "registerProduct.error.message.title", null);
		}
		model.addAttribute(rg);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
				"registerProduct.confirmation.message.title", null);
		return REDIRECT_TO_REGISTERED_PRODUCT;
	}


	/**
	 * @param registerProductForm1
	 * @param rg
	 */
	private void populateForm(final RegisterProductForm registerProductForm1, final RegisterProductForm rg)
	{
		rg.setAddressLine1(registerProductForm1.getAddressLine1());
		rg.setAddressLine2(registerProductForm1.getAddressLine2());
		rg.setCountry(registerProductForm1.getCountry());
		rg.setDatePurchased(registerProductForm1.getDatePurchased());
		rg.setPhoneNumber(registerProductForm1.getPhoneNumber());
		rg.setPostCode(registerProductForm1.getProductSku());
		rg.setSerialNumber(registerProductForm1.getSerialNumber());
		rg.setProductSku(registerProductForm1.getProductSku());
		rg.setTownCity(registerProductForm1.getTownCity());
		rg.setRegion(registerProductForm1.getRegion());
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
