package com.gallagher.b2b.storefront.controllers.pages;


import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.enterprisewide.b2badvance.bulkorder.data.BulkOrderCartItemData;
import com.enterprisewide.b2badvance.facades.bulkorder.BulkOrderFacade;
import com.enterprisewide.b2badvance.facades.bulkorder.data.BulkOrderImportResultData;


@Controller
@Scope("tenant")
@RequestMapping("/bulkorder")
public class BulkOrderController
{

	@Autowired
	BulkOrderFacade bulkOrderFacade;

	//@Resource(name = "bulkOrderProductOptionsList")
	private final List<ProductOption> bulkOrderProductOptionsList = new ArrayList<>(Arrays.asList(ProductOption.BASIC,
			ProductOption.SUMMARY, ProductOption.PRICE, ProductOption.STOCK, ProductOption.VARIANT_FULL));


	private static final String CART_POPUP = "fragments/bulkorder/addBulkOrderToCartPopup";

	@RequestMapping(value = "/addToCart", method = RequestMethod.GET)
	public String addProductsToCart(final HttpServletRequest request, final Model model)
	{
		final List<CartModificationData> modifications = bulkOrderFacade.addBulkOrderToCart(bulkOrderProductOptionsList);
		final List<BulkOrderCartItemData> entries = bulkOrderFacade.getBulkOrderCartItemEntries(modifications);

		model.addAttribute("entries", entries);
		bulkOrderFacade.clear();

		return CART_POPUP;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ProductData> search(@RequestParam("query")
	final String query, @RequestParam("resultSize")
	final Integer resultSize, final HttpServletRequest request, final Model model)
	{

		return bulkOrderFacade.getProductsForQuery(query, resultSize);
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void select(@RequestParam("qty")
	final Integer qty, @RequestParam("order")
	final Integer order, @RequestParam("code")
	final String code, final HttpServletRequest request, final Model model)
	{

		bulkOrderFacade.add(order, qty, code);
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void select(@RequestParam("order")
	final Integer order, final HttpServletRequest request, final Model model)
	{

		bulkOrderFacade.remove(order);
	}

	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public BulkOrderImportResultData upload2(final DefaultMultipartHttpServletRequest request, final Model model)
	{

		final MultipartFile file = request.getFile("files");
		final BulkOrderImportResultData result = bulkOrderFacade.importBulkOrderFromFile(file);
		return result;
	}

}
