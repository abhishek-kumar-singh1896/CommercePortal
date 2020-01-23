/**
 *
 */
package com.gallagher.core.events;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;

import org.apache.log4j.Logger;


/**
 * Registration event, implementation of {@link AbstractCommerceUserEvent}
 */
public class GallagherRegisterEvent extends AbstractCommerceUserEvent<BaseSiteModel> implements ClusterAwareEvent
{
	private static final Logger LOG = Logger.getLogger(GallagherRegisterEvent.class);

	@Override
	public boolean canPublish(final PublishEventContext publishEventContext)
	{
		LOG.info(String.format("GallagherRegisterEvent triggered from Node: %d", publishEventContext.getSourceNodeId()));
		return publishEventContext.getSourceNodeId() == publishEventContext.getTargetNodeId();
	}
}
