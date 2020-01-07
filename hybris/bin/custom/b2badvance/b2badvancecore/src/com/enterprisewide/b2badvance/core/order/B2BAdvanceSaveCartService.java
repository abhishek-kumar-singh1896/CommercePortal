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
package com.enterprisewide.b2badvance.core.order;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import com.enterprisewide.b2badvance.core.dto.OrderTemplateImportDTO;


/**
 * B2B Advance Interface that extends the interface {@link CommerceCartService} to expose methods to deal with
 * operations for saved carts (saving, retrieving, restoring, etc).
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceSaveCartService extends CommerceSaveCartService
{
	/**
	 * Returns all the order templates for the user
	 *
	 * @param pagination
	 *           holds the pagination information
	 * @param baseSite
	 *           for which templates needs to be fetched
	 * @param user
	 *           for whom templates needs to be fetched
	 * @return all the saved templates (basically carts with template attribute set as true)
	 */
	SearchPageData<CartModel> getOrderTemplatesForSiteAndUser(final PaginationData pagination, final BaseSiteModel baseSite,
			final UserModel user);

	/**
	 * Returns the total number of templates available for specific user and site
	 *
	 * @param baseSite
	 *           for which template count needs to be fetched
	 * @param user
	 *           for whom template count needs to be fetched
	 * @return total number of templates
	 */
	Integer getOrderTemplatesCountForSiteAndUser(final BaseSiteModel baseSite, final UserModel user);

	void createOrderTemplate(final OrderTemplateImportDTO template);

}
