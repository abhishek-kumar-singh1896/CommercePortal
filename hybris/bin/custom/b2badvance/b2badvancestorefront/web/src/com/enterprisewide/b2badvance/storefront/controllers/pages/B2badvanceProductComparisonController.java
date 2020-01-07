/**
 *
 */
package com.enterprisewide.b2badvance.storefront.controllers.pages;

import com.enterprisewide.b2badvance.facades.product.comparison.B2badvanceProductComparisonFacade;
import com.enterprisewide.b2badvance.facades.product.comparison.data.WrapperMapVariantAttributes;
import com.enterprisewide.b2badvance.storefront.controllers.ControllerConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * @author Enterprise Wide
 *
 */
@Controller
@RequestMapping("/compare")
public class B2badvanceProductComparisonController
{
	@Autowired
	B2badvanceProductComparisonFacade productComparisonFacade;
	
	@Autowired
	private ProductFacade productFacade;

	private static final String COMPARE_LIST = "/fragments/product/compareList";
	
	@RequestMapping(method = RequestMethod.GET)
    public String getCompareResults(@RequestParam(value = "code", required = true) final String productCode,
                                    @RequestParam(value = "action", required = true) final String action,
                                    final HttpServletRequest request, final Model model) throws UnsupportedEncodingException, CMSItemNotFoundException {
   	 	if(action.equals("removeAll")){
			productComparisonFacade.clear();
		}
		else if(action.equals("add")){
			if(checkProductExists(productCode)){
				productComparisonFacade.add(productCode);
			}
			else{
				model.addAttribute("error", "Product could not be found");
			}
   		}
   		else if(action.equals("remove")){
   			productComparisonFacade.remove(productCode);
		}
		
		model.addAttribute("compareProducts", productComparisonFacade.getProductComparisonList());
		return ControllerConstants.Views.Fragments.Product.CompareDiv;
    }

	private boolean checkProductExists(String productCode) {
		try{
		productFacade.getProductForCodeAndOptions(productCode, null);
		return true;
		}catch(Exception e){
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Integer add(@RequestParam("code") final String productCode, final HttpServletRequest request, final Model model)
	{
		productComparisonFacade.add(productCode);
		return productComparisonFacade.size();
	}

	@ResponseBody
	@RequestMapping(value = "/cbremove", method = RequestMethod.POST)
	public Integer remove(@RequestParam("code") final String productCode, final HttpServletRequest request, final Model model)
	{
		productComparisonFacade.remove(productCode);
		return productComparisonFacade.size();
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String removetest(@RequestParam("code") final String productCode, final HttpServletRequest request, final Model model)
	{
		productComparisonFacade.remove(productCode);

		return "redirect:/comparison";
	}

	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public void clear(final HttpServletRequest request, final Model model)
	{
		productComparisonFacade.clear();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(final HttpServletRequest request, final Model model)
	{
		final String popup = request.getParameter("popup");
		if (popup != null)
		{
			model.addAttribute("pcPopup", true);
		}
		final String close = request.getParameter("close");
		if (close != null)
		{
			model.addAttribute("pcPopupClose", true);
		}
		final List<ProductData> list = productComparisonFacade.getProductComparisonList();
		final List<WrapperMapVariantAttributes> variantMap = productComparisonFacade.getProductVariantAttributes(list);
		model.addAttribute("productList", list);
		model.addAttribute("variantMap", variantMap);

		return COMPARE_LIST;
	}
}
