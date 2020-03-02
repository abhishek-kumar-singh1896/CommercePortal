/**
 *
 */
package com.gallagher.facades.customer.impl;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;

import javax.annotation.Resource;

import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.services.GallagherCustomerService;
import com.gallagher.facades.customer.GallagherCustomerFacade;


/**
 * Gallagher facade for passing Access token
 *
 * to service layer from UI
 *
 * @author abhishek
 */
public class GallagherCustomerFacadeImpl extends DefaultCustomerFacade implements GallagherCustomerFacade
{
	@Resource(name = "gallagherCustomerService")
	private GallagherCustomerService gallagherCustomerService;

	@Override
	public void updateCommerceCustomer(final GallagherAccessToken token)
	{
		gallagherCustomerService.updateCommerceCustomer(token);
	}

}
