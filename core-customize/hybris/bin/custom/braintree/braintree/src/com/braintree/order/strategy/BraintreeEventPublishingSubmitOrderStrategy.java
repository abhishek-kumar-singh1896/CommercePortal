package com.braintree.order.strategy;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.order.strategies.impl.EventPublishingSubmitOrderStrategy;
import de.hybris.platform.servicelayer.model.ModelService;


public class BraintreeEventPublishingSubmitOrderStrategy extends EventPublishingSubmitOrderStrategy implements SubmitOrderStrategy {

    private ModelService modelService;

    @Override
    public void submitOrder(OrderModel order) {
        super.submitOrder(order);
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
