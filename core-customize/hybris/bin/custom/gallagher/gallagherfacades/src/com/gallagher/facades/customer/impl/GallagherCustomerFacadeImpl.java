/**
 *
 */
package com.gallagher.facades.customer.impl;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gallagher.facades.customer.GallagherCustomerFacade#changeUid(java.lang.String)
	 */

	@Override
	public void changeUid(final String newUid) throws DuplicateUidException
	{

		getGallagherCustomerService().changeUid(newUid);


	}



	public GallagherCustomerService getGallagherCustomerService()
	{
		return gallagherCustomerService;
	}

	public void setGallagherCustomerService(final GallagherCustomerService gallagherCustomerService)
	{
		this.gallagherCustomerService = gallagherCustomerService;
	}

}
