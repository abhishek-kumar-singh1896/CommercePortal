/**
 *
 */
package com.gallagher.facades.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.user.CustomerModel;

import javax.annotation.Resource;

import com.gallagher.core.dtos.GallagherAccessToken;
import com.gallagher.core.enums.BU;
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


	@Override
	public void createGuestUserForAnonymousCheckout(final String email, final String name) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("email", email);
		final CustomerModel guestCustomer = getModelService().create(CustomerModel.class);
		final String guid = generateGUID();

		//takes care of localizing the name based on the site language
		guestCustomer.setUid(guid + "|" + email);
		guestCustomer.setName(name);
		guestCustomer.setType(CustomerType.valueOf(CustomerType.GUEST.getCode()));
		guestCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		guestCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		guestCustomer.setBusinessUnit(BU.AM);

		getCustomerAccountService().registerGuestForAnonymousCheckout(guestCustomer, guid);
		updateCartWithGuestForAnonymousCheckout(getCustomerConverter().convert(guestCustomer));
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
