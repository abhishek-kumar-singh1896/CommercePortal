package com.enterprisewide.b2badvance.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enterprisewide.b2badvance.facades.invoice.B2BAdvanceInvoiceFacade;
import com.enterprisewide.b2badvance.facades.invoice.data.InvoiceData;


/**
 * Controller to provide all invoice related operations
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping("/my-account/invoices")
public class B2BAdvanceInvoiceController extends B2BAdvanceAbstractSearchPageController
{
	private static final String INVOICES_CMS_PAGE = "invoices";

	private static final Logger LOG = Logger.getLogger(B2BAdvanceInvoiceController.class);

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "invoiceFacade")
	private B2BAdvanceInvoiceFacade invoiceFacade;

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getInvoices(@RequestParam(value = "page", defaultValue = "0")
	final int page, @RequestParam(value = "show", defaultValue = "Page")
	final ShowMode showMode, @RequestParam(value = "sort", required = false)
	final String sortCode, final Model model) throws CMSItemNotFoundException
	{
		// Handle paged search results
		final PaginationData paginationData = createPaginationData(page, 5, sortCode, showMode);
		final SearchPageData<InvoiceData> searchPageData = invoiceFacade.getInvoicesForCurrentUser(paginationData);
		populateModel(model, searchPageData, showMode);
		storeCmsPageInModel(model, getContentPageForLabelOrId(INVOICES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(INVOICES_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, accountBreadcrumbBuilder.getBreadcrumbs("text.account.invoices"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}
}
