package com.enterprisewide.b2badvance.fulfilmentprocess.actions.order;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.outboundintegration.exceptions.B2BAdvanceWebServiceFailureException;
import com.enterprisewide.b2badvance.outboundintegration.services.B2BAdvanceOrderOutboundIntegrationService;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

/**
 * Action sends a webservice Request to ERP System for Order Creation.
 *
 * @author Enterprise WideF
 */
public class SendOrderToERPAction extends AbstractSimpleDecisionAction<OrderProcessModel> {
	private static final Logger LOG = Logger.getLogger(SendOrderToERPAction.class);

	private B2BAdvanceOrderOutboundIntegrationService orderOutboundIntegrationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transition executeAction(final OrderProcessModel process) {
		final OrderModel order = process.getOrder();

		if (order == null) {
			LOG.error("Missing the order, exiting the process");
			return Transition.NOK;
		}
		boolean sentToERP = false;
		try {
			sentToERP = getOrderOutboundIntegrationService().createOrder(order);
			getModelService().save(order);
			getModelService().refresh(order);
		} catch (final B2BAdvanceWebServiceFailureException fsWSFEx) {
			LOG.error("Error sending order to ERP", fsWSFEx);
		}

		if (sentToERP) {
			// If webservice Send Order SuccessFuly to ERP
			setOrderStatus(order, OrderStatus.SENT_TO_ERP);
			return Transition.OK;
		} else {
			// If webservice Fails to Send Order to ERP
			setOrderStatus(order, OrderStatus.ERROR_SENDING_TO_ERP);
			return Transition.NOK;
		}
	}

	protected B2BAdvanceOrderOutboundIntegrationService getOrderOutboundIntegrationService() {
		return orderOutboundIntegrationService;
	}

	@Required
	public void setOrderOutboundIntegrationService(
			final B2BAdvanceOrderOutboundIntegrationService orderOutboundIntegrationService) {
		this.orderOutboundIntegrationService = orderOutboundIntegrationService;
	}
}
