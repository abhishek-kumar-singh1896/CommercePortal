/**
 *
 */
package com.gallagher.facades.order.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;

import javax.annotation.Resource;

import com.gallagher.core.services.GallagherCustomerAccountService;
import com.gallagher.facades.order.GallagherOrderFacade;


/**
 * @author abhinavgupta03
 *
 */
public class GallagherOrderFacadeImpl extends DefaultOrderFacade implements GallagherOrderFacade
{
	private static final String ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE = "Order with guid %s not found for current user in current BaseStore";

	@Resource(name = "gallagherCustomerAccountService")
	private GallagherCustomerAccountService gallagherCustomerAccountService;

	@Override
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryBUnit(final PageableData pageableData,
			final OrderStatus... statuses)
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final SearchPageData<OrderModel> orderResults = gallagherCustomerAccountService.getOrderListForB2BUnit(currentCustomer,
				currentBaseStore,
				statuses, pageableData, ((B2BCustomerModel) currentCustomer).getDefaultB2BUnit());

		return convertPageData(orderResults, getOrderHistoryConverter());
	}

	@Override
	public OrderData getOrderDetailsForCodeForAdmin(final String code)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

		OrderModel orderModel = null;
		if (getCheckoutCustomerStrategy().isAnonymousCheckout())
		{
			orderModel = getCustomerAccountService().getOrderDetailsForGUID(code, baseStoreModel);
		}
		else
		{
			try
			{
				orderModel = gallagherCustomerAccountService
						.getOrderForCodeForAdmin(code,
						baseStoreModel);
			}
			catch (final ModelNotFoundException e)
			{
				throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
			}
		}

		if (orderModel == null)
		{
			throw new UnknownIdentifierException(String.format(ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE, code));
		}
		return getOrderConverter().convert(orderModel);
	}

}
