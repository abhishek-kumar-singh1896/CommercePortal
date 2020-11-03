/**
 *
 */
package com.gallagher.facades.order;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;


/**
 * @author abhinavgupta03
 *
 */
public interface GallagherOrderFacade
{
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryBUnit(final PageableData pageableData,
			final OrderStatus... statuses);

	public OrderData getOrderDetailsForCodeForAdmin(final String code);

}
