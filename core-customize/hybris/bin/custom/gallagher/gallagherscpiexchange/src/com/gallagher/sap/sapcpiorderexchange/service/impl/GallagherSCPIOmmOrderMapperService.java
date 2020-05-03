package com.gallagher.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundPriceComponentModel;
import de.hybris.platform.sap.sapcpiorderexchange.service.impl.SapCpiOmmOrderMapperService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Custom SAP CPI OMM Order Mapper Service
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOmmOrderMapperService extends SapCpiOmmOrderMapperService
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<SAPCpiOutboundPriceComponentModel> mapOrderPrices(
			final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents)
	{

		final List<SAPCpiOutboundPriceComponentModel> sapCpiOutboundPriceComponents = new ArrayList<>();

		sapCpiOrderPriceComponents.forEach(price -> {

			final SAPCpiOutboundPriceComponentModel sapCpiOutboundPriceComponent = new SAPCpiOutboundPriceComponentModel();
			sapCpiOutboundPriceComponent.setOrderId(price.getOrderId());
			sapCpiOutboundPriceComponent.setEntryNumber(price.getEntryNumber());
			sapCpiOutboundPriceComponent.setValue(price.getValue());
			sapCpiOutboundPriceComponent.setUnit(price.getUnit());
			sapCpiOutboundPriceComponent.setAbsolute(price.getAbsolute());
			sapCpiOutboundPriceComponent.setConditionCode(price.getConditionCode());
			sapCpiOutboundPriceComponent.setTaxJurisdictionCode(price.getTaxJurisdictionCode());
			sapCpiOutboundPriceComponent.setConditionCounter(price.getConditionCounter());
			sapCpiOutboundPriceComponent.setCurrencyIsoCode(price.getCurrencyIsoCode());
			sapCpiOutboundPriceComponent.setPriceQuantity(price.getPriceQuantity());

			sapCpiOutboundPriceComponents.add(sapCpiOutboundPriceComponent);

		});

		return new HashSet<>(sapCpiOutboundPriceComponents);

	}
}
