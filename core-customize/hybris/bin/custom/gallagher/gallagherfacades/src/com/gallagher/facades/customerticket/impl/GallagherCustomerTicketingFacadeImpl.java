package com.gallagher.facades.customerticket.impl;

import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.customerticket.DefaultCustomerTicketingFacade;
import de.hybris.platform.customerticketingfacades.data.TicketAssociatedData;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.customerticketingfacades.strategies.TicketAssociationStrategies;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private List<TicketAssociationStrategies> gallagherAssociationStrategies;

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

	@Override
	public Map<String, List<TicketAssociatedData>> getAssociatedToObjects()
	{
		final Map<String, List<TicketAssociatedData>> associatedObjects = new HashMap<String, List<TicketAssociatedData>>();
		for (final TicketAssociationStrategies ticketAssocitedStartegy : getGallagherAssociationStrategies())
		{
			associatedObjects.putAll(ticketAssocitedStartegy.getObjects(getUserService().getCurrentUser()));
		}
		return associatedObjects;
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

	/**
	 * @return the gallagherAssociationStrategies
	 */
	public List<TicketAssociationStrategies> getGallagherAssociationStrategies()
	{
		return gallagherAssociationStrategies;
	}

	/**
	 * @param gallagherAssociationStrategies
	 *           the gallagherAssociationStrategies to set
	 */
	public void setGallagherAssociationStrategies(final List<TicketAssociationStrategies> gallagherAssociationStrategies)
	{
		this.gallagherAssociationStrategies = gallagherAssociationStrategies;
	}


}
