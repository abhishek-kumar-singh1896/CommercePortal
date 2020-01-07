package com.enterprisewide.b2badvance.core.workflow;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class ApproveUpdateActionJob extends AbstractUpdateProductActionJob
{

    private static final Logger LOG = Logger.getLogger(ApproveUpdateActionJob.class);

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action)
    {
        final ProductModel product = getAttachedProduct(action);

        LOG.info("Product " + product.getCode() + " approved. Confirmation email will be sent.");

       /* if (product.getApprovalStatus())
        {
            player.setConfirmed(true);
            getModelService().save(player);
        }

        getMailService().sendConfirmationMail(player);*/

        for (final WorkflowDecisionModel decision : action.getDecisions())
        {
            return decision;
        }
        return null;
    }


}
