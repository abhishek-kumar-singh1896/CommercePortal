/**
 *
 */
package com.gallagher.b2c.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallagher.facades.GallagherRegisteredProductsFacade;


/**
 *
 */
@Controller
@RequestMapping(value = "/products")
public class RegisteredProductsController extends AbstractPageController
{

	@Resource(name = "registeredProductsFacade")
	private GallagherRegisteredProductsFacade gallagherRegisteredProductsFacade;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;

	private static final String REG_PRODUCTS_PAGE = "regProducts";

	@RequestMapping(value = "/{email}", method = RequestMethod.GET)
	public String registeredProductsByUser(@PathVariable("email")
	final String email, final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel regProductsPage = getContentPageForLabelOrId(REG_PRODUCTS_PAGE);
		storeCmsPageInModel(model, regProductsPage);
		setUpMetaDataForContentPage(model, regProductsPage);
		model.addAttribute("registeredProducts", getGallagherRegisteredProductsFacade().getRegisteredProducts(email));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getContentPageBreadcrumbBuilder().getBreadcrumbs(regProductsPage));
		return getViewForPage(model);
	}

	public GallagherRegisteredProductsFacade getGallagherRegisteredProductsFacade()
	{
		return gallagherRegisteredProductsFacade;
	}

	public ContentPageBreadcrumbBuilder getContentPageBreadcrumbBuilder()
	{
		return contentPageBreadcrumbBuilder;
	}

}
