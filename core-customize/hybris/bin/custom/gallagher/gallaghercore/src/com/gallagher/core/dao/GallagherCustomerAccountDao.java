package com.gallagher.core.dao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


/**
 * @author abhinavgupta03
 *
 */
public interface GallagherCustomerAccountDao
{
	public SearchPageData<OrderModel> findOrdersForB2BUnit(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData, final List<B2BUnitModel> b2bUnit);

	public OrderModel findOrderByCodeForAdmin(final String code, final BaseStoreModel store);

}
