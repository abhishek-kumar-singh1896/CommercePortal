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
package com.enterprisewide.b2badvance.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportDTO;
import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportEntryDTO;
import com.enterprisewide.b2badvance.facades.order.B2BAdvanceSaveCartFacade;
import com.enterprisewide.b2badvance.storefront.controllers.ControllerConstants;
import com.enterprisewide.b2badvance.storefront.forms.CreateTemplateForm;
import com.enterprisewide.b2badvance.storefront.forms.RestoreTemplateForm;
import com.enterprisewide.b2badvance.storefront.forms.validation.CreateTemplateFormValidator;


/**
 * Controller for order templates page
 *
 * @author Enterprise Wide
 */
@Controller
@RequestMapping("/my-account/quick-order-templates")
public class QuickOrderTemplatePageController extends B2BAdvanceAbstractSearchPageController
{
	private static final String QUICK_ORDER_TEMPLATES_CMS_PAGE = "order-templates";

	private static final String REDIRECT_TO_QUICK_ORDER = REDIRECT_PREFIX + "/quickOrder";

	private static final Logger LOG = Logger.getLogger(QuickOrderTemplatePageController.class);

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "saveCartFacade")
	private B2BAdvanceSaveCartFacade saveCartFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "createTemplateFormValidator")
	private CreateTemplateFormValidator createTemplateFormValidator;

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String orderTemplates(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model) throws CMSItemNotFoundException
	{
		// Handle paged search results
		final PaginationData paginationData = createPaginationData(page, 5, sortCode, showMode);
		final SearchPageData<CartData> searchPageData = saveCartFacade.getOrderTemplatesForCurrentUser(paginationData);
		populateModel(model, searchPageData, showMode);

		storeCmsPageInModel(model, getContentPageForLabelOrId(QUICK_ORDER_TEMPLATES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(QUICK_ORDER_TEMPLATES_CMS_PAGE));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.quickOrderTemplates"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/{templateId}/restore", method = RequestMethod.GET)
	@RequireHardLogIn
	public String restoreTemplateForId(@PathVariable(value = "templateId") final String templateId, final Model model)
			throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(templateId);
		final CommerceSaveCartResultData commerceSaveCartResultData = saveCartFacade.getCartForCodeAndCurrentUser(parameters);
		model.addAttribute(templateId);
		model.addAttribute(commerceSaveCartResultData);
		return ControllerConstants.Views.Fragments.QuickOrder.TemplateRestorePopup;
	}

	@RequireHardLogIn
	@RequestMapping(value = "/{templateId}/restore", method = RequestMethod.POST)
	public String postRestoreOrderTemplateForId(@PathVariable(value = "templateId") final String templateId,
			final RestoreTemplateForm form, final BindingResult bindingResult, final RedirectAttributes redirectModel)
			throws CommerceSaveCartException
	{
		final List<OrderEntryData> quickOrderEntriesData = saveCartFacade.getTemplateOrderEntries(form.getTemplateId());
		redirectModel.addFlashAttribute("quickOrderEntries", quickOrderEntriesData);
		return REDIRECT_TO_QUICK_ORDER;
	}

	@RequestMapping(value = "/{templateId}/delete", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@RequireHardLogIn
	public @ResponseBody String deleteOrderTemplateForId(@PathVariable(value = "templateId") final String templateId)
			throws CommerceSaveCartException
	{
		try
		{
			final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
			parameters.setCartId(templateId);
			saveCartFacade.flagForDeletion(templateId);
		}
		catch (final CommerceSaveCartException ex)
		{
			LOG.error("Error while deleting the template with templateId " + templateId + " because of " + ex);
			return getMessageSource().getMessage("text.delete.quickOrderTemplate.error", null, getI18nService().getCurrentLocale());
		}
		return String.valueOf(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/createTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireHardLogIn
	public final Map<String, Object> createTemplate(@RequestBody final CreateTemplateForm form, final Model model,
			final BindingResult bindingResult) throws CommerceSaveCartException
	{
		final Map<String, Object> result = new HashMap<>();
		createTemplateFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors() && form.getCartEntries().size() > 0)
		{
			for (final FieldError error : bindingResult.getFieldErrors())
			{
				result.put(error.getField(),
						getMessageSource().getMessage(error.getCode(), null, getI18nService().getCurrentLocale()));
			}
		}
		else
		{
			final OrderTemplateImportDTO template = new OrderTemplateImportDTO();
			template.setName(form.getTemplateName());
			template.setDescription(form.getTemplateDesc());
			if (CollectionUtils.isNotEmpty(form.getCartEntries()))
			{
				final List<OrderTemplateImportEntryDTO> entryList = new ArrayList<>(form.getCartEntries().size());
				template.setEntries(entryList);
				for (final OrderEntryData orderEntry : form.getCartEntries())
				{
					final OrderTemplateImportEntryDTO entry = new OrderTemplateImportEntryDTO();
					entry.setProductCode(orderEntry.getProduct().getCode());
					entry.setQuantity(orderEntry.getQuantity());
					entryList.add(entry);
				}

			}
			saveCartFacade.createOrderTemplate(template);
			result.put("success", getMessageSource().getMessage("create.template.on.success", new Object[]
			{ template.getName() }, getI18nService().getCurrentLocale()));
		}
		return result;
	}
}
