/**
 *
 */
package com.gallagher.core.services;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author abhinavgupta03
 *
 */
public interface GallagherCustomerAccountService
{
	public SearchPageData<OrderModel> getOrderListForB2BUnit(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData, B2BUnitModel b2bUnit);

	public OrderModel getOrderForCodeForAdmin(final String code, final BaseStoreModel store);
}
