package com.enterprisewide.b2badvance.core.workflow;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

public class RejectUpdateActionJob extends AbstractUpdateProductActionJob{

    private static final Logger LOG = Logger.getLogger(RejectUpdateActionJob.class);

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action)
    {
        final ProductModel product = getAttachedProduct(action);

        LOG.info("Product " + product.getCode() + " rejected.");



        for (final WorkflowDecisionModel decision : action.getDecisions())
        {
            return decision;
        }
        return null;
    }

}
