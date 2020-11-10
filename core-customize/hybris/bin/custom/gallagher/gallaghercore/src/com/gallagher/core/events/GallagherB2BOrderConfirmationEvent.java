/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 *
 */
public class GallagherB2BOrderConfirmationEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

	private OrderModel orderEntryModel;

	public GallagherB2BOrderConfirmationEvent(final OrderModel orderEntryModel, final BaseStoreModel baseStore,
			final BaseSiteModel site, final CurrencyModel currency)
	{
		super();

		setOrderEntryModel(orderEntryModel);
		setBaseStore(baseStore);
		setSite(site);
		setCurrency(currency);
	}

	public OrderModel getOrderEntryModel()
	{
		return orderEntryModel;
	}

	public void setOrderEntryModel(final OrderModel orderEntryModel)
	{
		this.orderEntryModel = orderEntryModel;
	}
}



