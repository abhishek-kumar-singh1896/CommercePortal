package com.gallagher.core.events;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.Map;


/**
 * @author ankituniyal
 *
 *         Custom B2B Customer update event, implementation of {@link AbstractCommerceUserEvent}
 */
public class GallagherB2BCustomerUpdateEvent extends AbstractCommerceUserEvent<BaseSiteModel> {

	private Map<String, String> modifiedAttributesMap;

	/**
	 * @return the modifiedAttributesMap
	 */
	public Map<String, String> getModifiedAttributesMap()
	{
		return modifiedAttributesMap;
	}

	/**
	 * @param modifiedAttributesMap
	 *           the modifiedAttributesMap to set
	 */
	public void setModifiedAttributesMap(final Map<String, String> modifiedAttributesMap)
	{
		this.modifiedAttributesMap = modifiedAttributesMap;
	}


}
