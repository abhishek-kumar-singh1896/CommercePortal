/**
 *
 */
package com.enterprisewide.b2badvance.facades.checkout.flow;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;


/**
 *
 */
public interface B2badvanceCheckoutFacade extends CheckoutFlowFacade
{
	void setDeliveryInstructions(String deliveryInstructions);
}
