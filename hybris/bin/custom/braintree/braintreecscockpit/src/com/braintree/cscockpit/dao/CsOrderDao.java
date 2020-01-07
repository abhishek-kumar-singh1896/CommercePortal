package com.braintree.cscockpit.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.OrderDao;


public interface CsOrderDao extends OrderDao
{

	/**
	 * Return order with the given braintree Transaction id
	 *
	 * @param braintreeTransactionID
	 *           - braintree Transaction id
	 *
	 * @return {@link AbstractOrderModel} - matched orders
	 * @throws IllegalArgumentException
	 *            if paymentMode is null
	 * @throws IllegalStateException
	 *            if paymentMode is not persisted.
	 */
	OrderModel findOrderByBraintreeTransactionID(String braintreeTransactionID);
}
