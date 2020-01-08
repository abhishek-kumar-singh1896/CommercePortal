/**
 *
 */
package com.enterprisewide.b2badvance.core.order.hook.impl;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;


/**
 * Hook to send order to third party
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceOrderPostMethodHook implements CommercePlaceOrderMethodHook
{
	private static final Logger LOG = Logger.getLogger(B2BAdvanceOrderPostMethodHook.class);

	private final ConfigurationService configurationService;

	public B2BAdvanceOrderPostMethodHook(final ConfigurationService configurationService)
	{
		super();
		this.configurationService = configurationService;
	}

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter commerceCheckoutParameter,
			final CommerceOrderResult commerceOrderResult)
	{
		// not implemented
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter commerceCheckoutParameter)
	{
		// not implemented
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter commerceCheckoutParameter,
			final CommerceOrderResult commerceOrderResult)
	{
		final OrderModel order = commerceOrderResult.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		final boolean sendEnabled = configurationService.getConfiguration().getBoolean("order.send.enabled", Boolean.FALSE);
		if (sendEnabled)
		{
			//TODO send Order
		}

	}
}

