/**
 *
 */
package com.enterprisewide.b2badvance.facades.order;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.List;
import java.util.Map;

import com.enterprisewide.b2badvance.facades.order.data.B2BAdvanceOrderData;


/**
 * Order facade for B2BAdvance.
 *
 * @author Enterprise Wide
 *
 */
public interface B2BAdvanceOrderFacade extends OrderFacade
{

	SearchPageData<OrderData> getPagedOrderDetailsForStatuses(final PageableData pageableData,
         final OrderStatus... statuses);


	/**
	 * Saves list of orders
	 *
	 * @param orders
	 * @return map of errors. Key is Hybris Order Number and value is the error associated with that order
	 */
	Map<String, String> saveOrders(final List<B2BAdvanceOrderData> orders);
}
