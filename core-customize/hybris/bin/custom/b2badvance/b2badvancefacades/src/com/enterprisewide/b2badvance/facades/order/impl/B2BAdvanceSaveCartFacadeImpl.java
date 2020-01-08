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
package com.enterprisewide.b2badvance.facades.order.impl;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultSaveCartFacade;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportDTO;
import com.enterprisewide.b2badvance.core.order.B2BAdvanceSaveCartService;
import com.enterprisewide.b2badvance.facades.order.B2BAdvanceSaveCartFacade;


/**
 * Service is responsible for saved cart related functionality such as saving a cart, retrieving saved cart(s),
 * restoring a saved cart etc.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceSaveCartFacadeImpl extends DefaultSaveCartFacade implements B2BAdvanceSaveCartFacade
{

	private Converter<CartModel, CartData> templateConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchPageData<CartData> getOrderTemplatesForCurrentUser(final PaginationData paginationData)
	{
		final SearchPageData<CartData> result = new SearchPageData<>();
		final SearchPageData<CartModel> savedCartModels = ((B2BAdvanceSaveCartService) getCommerceSaveCartService())
				.getOrderTemplatesForSiteAndUser(paginationData, getBaseSiteService().getCurrentBaseSite(),
						getUserService().getCurrentUser());
		result.setPagination(savedCartModels.getPagination());
		result.setSorts(savedCartModels.getSorts());

		final List<CartData> savedCartDatas = Converters.convertAll(savedCartModels.getResults(), getCartConverter());

		result.setResults(savedCartDatas);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOrderTemplatesCountForCurrentUser()
	{
		return ((B2BAdvanceSaveCartService) getCommerceSaveCartService())
				.getOrderTemplatesCountForSiteAndUser(getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createOrderTemplate(final OrderTemplateImportDTO template)
	{
		((B2BAdvanceSaveCartService) getCommerceSaveCartService()).createOrderTemplate(template);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws CommerceSaveCartException
	 */
	@Override
	public List<OrderEntryData> getTemplateOrderEntries(final String templateId) throws CommerceSaveCartException
	{
		CartModel templateToRetrieve = null;
		if (StringUtils.isNotEmpty(templateId))
		{
			templateToRetrieve = getCommerceCartService().getCartForCodeAndUser(templateId, getUserService().getCurrentUser());
		}
		else
		{
			throw new CommerceSaveCartException("template Id cannot be empty");
		}

		if (null == templateToRetrieve)
		{
			throw new CommerceSaveCartException("Cannot find a template for Id [" + templateId + "]");
		}

		return getTemplateConverter().convert(templateToRetrieve).getEntries();
	}

	@Required
	public void setTemplateConverter(final Converter<CartModel, CartData> templateConverter)
	{
		this.templateConverter = templateConverter;
	}

	protected Converter<CartModel, CartData> getTemplateConverter()
	{
		return templateConverter;
	}
}
