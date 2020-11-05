package com.gallagher.facades.customerticket.impl;

import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.customerticket.DefaultCustomerTicketingFacade;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.gallagher.facades.customerticket.GallagherTicketFacade;


/**
 * Custom implementation of {@link TicketFacade}
 *
 * @author Alvin Yap
 */
public class GallagherCustomerTicketingFacadeImpl extends DefaultCustomerTicketingFacade implements GallagherTicketFacade
{
	private static final Logger LOG = Logger.getLogger(GallagherCustomerTicketingFacadeImpl.class);

	private BaseStoreService baseStoreService;

	@Override
	public List<TicketCategory> getTicketCategories()
	{
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if (null == baseStore)
		{
			return Arrays.asList(TicketCategory.values());
		}

		final List<TicketCategory> categories = new ArrayList<>();

		if (null != baseStore.getTicketSales() && baseStore.getTicketSales().booleanValue())
		{
			categories.add(TicketCategory.SECURITY_SALES);
		}

		if (null != baseStore.getTicketTechnicalSupport() && baseStore.getTicketTechnicalSupport())
		{
			categories.add(TicketCategory.SECURITY_TECHNICAL_SUPPORT);
		}

		return categories;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

}
