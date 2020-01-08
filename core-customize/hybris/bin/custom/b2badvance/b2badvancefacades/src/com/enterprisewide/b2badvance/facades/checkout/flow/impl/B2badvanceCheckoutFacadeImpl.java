/**
 *
 */
package com.enterprisewide.b2badvance.facades.checkout.flow.impl;

import com.enterprisewide.b2badvance.facades.checkout.flow.B2badvanceCheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.order.impl.B2BMultiStepCheckoutFlowFacade;
import de.hybris.platform.core.model.order.CartModel;


/**
 *
 */
public class B2badvanceCheckoutFacadeImpl extends B2BMultiStepCheckoutFlowFacade implements B2badvanceCheckoutFacade
{

	@Override
	public void setDeliveryInstructions(final String deliveryInstructions)
	{
		final CartModel cartModel = getCart();
		cartModel.setDeliveryInstructions(deliveryInstructions);
		getModelService().save(cartModel);

	}

}
