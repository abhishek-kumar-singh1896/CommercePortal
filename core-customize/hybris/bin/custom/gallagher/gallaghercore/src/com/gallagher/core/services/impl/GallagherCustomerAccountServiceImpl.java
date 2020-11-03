/**
 *
 */
package com.gallagher.core.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import javax.annotation.Resource;

import com.gallagher.core.dao.GallagherCustomerAccountDao;
import com.gallagher.core.services.GallagherCustomerAccountService;


/**
 * @author abhinavgupta03
 *
 */
public class GallagherCustomerAccountServiceImpl extends DefaultCustomerAccountService implements GallagherCustomerAccountService
{
	@Resource(name = "gallagherCustomerAccountDao")
	private GallagherCustomerAccountDao gallagherCustomerAccountDao;

	@Override
	public SearchPageData<OrderModel> getOrderListForB2BUnit(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData, final B2BUnitModel b2bUnit)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		validateParameterNotNull(pageableData, "PageableData must not be null");
		validateParameterNotNull(b2bUnit, "B2B Unit must not be null");
		return gallagherCustomerAccountDao.findOrdersForB2BUnit(customerModel, store, status, pageableData, b2bUnit);
	}

	/**
	 * @deprecated Since 5.0.
	 */
	@Override
	@Deprecated(since = "5.0")
	public OrderModel getOrderForCodeForAdmin(final String code, final BaseStoreModel store)
	{
		validateParameterNotNull(code, "Order code cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		return gallagherCustomerAccountDao.findOrderByCodeForAdmin(code, store);
	}

}
