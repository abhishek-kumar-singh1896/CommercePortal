/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallagher.b2c.controllers.ControllerConstants;
import com.gallagher.b2c.validators.RegisterProductValidator;
import com.gallagher.core.form.RegisterProductForm;
import com.gallagher.facades.GallagherRegisterProductFacade;



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

	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json")
	public String submitSampleOrder(final RegisterProductForm registerProductForm, final Model model,
			final BindingResult bindingResult, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		registerProductValidator.validate(registerProductForm, bindingResult);

		if (!bindingResult.hasErrors())
		{
			final String productCode = registerProductForm.getProductSku();
			final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
					Arrays.asList(ProductOption.BASIC));
			model.addAttribute("registerProductForm", registerProductForm);
			model.addAttribute("product", productData);
		}
		//		registerProduct.postRegisterProduct(registerProductForm);
		return ControllerConstants.Views.Fragments.RegisterProduct.RegisterProductPopup;
	}

}
