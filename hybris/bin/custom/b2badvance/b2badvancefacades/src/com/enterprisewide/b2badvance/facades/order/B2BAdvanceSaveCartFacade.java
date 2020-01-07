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
package com.enterprisewide.b2badvance.facades.order;

import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportDTO;


/**
 * Save Cart facade interface. Service is responsible for saved cart related functionality such as saving a cart,
 * retrieving saved cart(s), restoring a saved cart etc.
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceSaveCartFacade extends SaveCartFacade
{
	/**
	 * Returns all saved templates for current user
	 *
	 * @param paginationData
	 *           holds pagination information like current page, page size etc.
	 * @return list of saved templates
	 */
	SearchPageData<CartData> getOrderTemplatesForCurrentUser(final PaginationData paginationData);

	/**
	 * Returns total count of saved templates for current user
	 *
	 * @return total count of saved templates
	 */
	Integer getOrderTemplatesCountForCurrentUser();

	/**
	 * @param template
	 */
	void createOrderTemplate(OrderTemplateImportDTO template);

	/**
	 * Get all the order entries for the particular template ID
	 *
	 * @param templateId
	 *                      holds the template id
	 * @return all the order entries
	 * @throws CommerceSaveCartException
	 */
	List<OrderEntryData> getTemplateOrderEntries(String templateId) throws CommerceSaveCartException;

}
