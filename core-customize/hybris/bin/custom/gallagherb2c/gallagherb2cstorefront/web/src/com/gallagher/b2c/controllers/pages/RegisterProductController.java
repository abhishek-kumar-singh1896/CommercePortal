/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Arrays;
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

import com.gallagher.b2c.form.RegisterProductForm;
import com.gallagher.b2c.response.RPFormResponseData;
import com.gallagher.b2c.response.RPFormResponseStatus;
import com.gallagher.b2c.validators.RegisterProductValidator;
import com.gallagher.facades.GallagherRegisterProductFacade;
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
	@Resource(name = "registerProduct")
	private GallagherRegisterProductFacade registerProduct;

	@Resource(name = "registerProductValidator")
	private RegisterProductValidator registerProductValidator;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "gallagheroutboundservicesService")
	private GallagheroutboundservicesService gallagheroutboundservicesService;

	protected static final Logger LOG = Logger.getLogger(RegisterProductController.class);

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
		catch (final Exception e)
		{
			LOG.debug("Issue in register product", e);
			jsonResponse.setResponseStatus(RPFormResponseStatus.FAILURE);
		}
		return jsonResponse;
	}


	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json")
	public String submitRegisterProduct(@ModelAttribute
	final RegisterProductForm registerProductForm, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		final RegisterProductRequest register = new RegisterProductRequest();
		register.setAddressLine1(registerProductForm.getAddressLine1());
		register.setAddressLine2(registerProductForm.getAddressLine2());
		register.setCountry(registerProductForm.getCountry());
		register.setDatePurchased(registerProductForm.getDatePurchased());
		register.setPhoneNumber(registerProductForm.getPhoneNumber());
		register.setPostCode(registerProductForm.getProductSku());
		register.setSerialNumber(registerProductForm.getSerialNumber());
		register.setProductSku(registerProductForm.getProductSku());
		register.setTownCity(registerProductForm.getTownCity());
		gallagheroutboundservicesService.postRegisterProduct(register);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
				"registerProduct.confirmation.message.title");
		return REDIRECT_PREFIX + "/register-product";
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
