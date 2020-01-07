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
package com.enterprisewide.b2badvance.core.order.dao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.SaveCartDao;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;


/**
 * Interface for dao object to handle the saved cart feature
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceSaveCartDao extends SaveCartDao
{

	/**
	 * Retrieve templates by user and basesite
	 *
	 * @param pagination
	 * @param user
	 *           mandatory parameter
	 * @param baseSite
	 *           optional parameter
	 * @return list of saved user carts
	 */
	SearchPageData<CartModel> getOrderTemplatesForSiteAndUser(PaginationData pagination, BaseSiteModel baseSite, UserModel user);

	/**
	 * Return the total number of the saved carts by user and basesite
	 *
	 * @param baseSite
	 * @param user
	 *           mandatory parameter
	 * @return the total number
	 */
	Integer getOrderTemplatesCountForSiteAndUser(BaseSiteModel baseSite, UserModel user);
}