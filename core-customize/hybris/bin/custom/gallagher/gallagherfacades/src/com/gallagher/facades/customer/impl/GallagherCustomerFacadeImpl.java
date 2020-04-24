/**
 *
 */
package com.gallagher.facades.customer.impl;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commerceservices.enums.SiteChannel;

import javax.annotation.Resource;

import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.services.GallagherCustomerService;
import com.gallagher.facades.customer.GallagherCustomerFacade;


/**
 * Implementation of {@link GallagherCustomerFacade}
 *
 * @author Abhishek
 */
public class GallagherCustomerFacadeImpl extends DefaultCustomerFacade implements GallagherCustomerFacade
{
	@Resource(name = "gallagherCustomerService")
	private GallagherCustomerService gallagherCustomerService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCommerceCustomer(final GallagherAccessToken token, final SiteChannel channel)
	{
		gallagherCustomerService.updateCommerceCustomer(token, channel);
	}

}
