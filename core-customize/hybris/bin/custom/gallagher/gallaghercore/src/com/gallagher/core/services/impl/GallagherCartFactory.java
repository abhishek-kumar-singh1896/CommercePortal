/**
 *
 */
package com.gallagher.core.services.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.order.impl.CommerceCartFactory;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * @author ankituniyal
 *
 */
public class GallagherCartFactory extends CommerceCartFactory
{

	@Override
	protected CartModel createCartInternal()
	{
		final CartModel cart = super.createCartInternal();

		final UserModel user = cart.getUser();
		if (user instanceof B2BCustomerModel)
		{
			final B2BCustomerModel b2bCustomer = (B2BCustomerModel) user;
			final String salesArea = b2bCustomer.getDefaultB2BUnit().getSalesArea();
			cart.setSalesArea(salesArea);
		}
		return cart;
	}
}
