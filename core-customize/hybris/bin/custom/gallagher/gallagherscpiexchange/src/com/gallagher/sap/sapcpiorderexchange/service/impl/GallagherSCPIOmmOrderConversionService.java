package com.gallagher.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiorderexchange.service.impl.SapCpiOmmOrderConversionService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.constants.GallagherSalesConditionCsvColumns;


/**
 * Custom SapCpiOmmOrderConversionService
 *
 * @author Vikram Bishnoi
 */
public class GallagherSCPIOmmOrderConversionService extends SapCpiOmmOrderConversionService
{

	private static final Logger LOG = LoggerFactory.getLogger(GallagherSCPIOmmOrderConversionService.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<SapCpiOrderPriceComponent> mapOrderPrices(final OrderModel orderModel)
	{

		final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents = new ArrayList<>();

		getSapSalesConditionsContributor().createRows(orderModel).forEach(row -> {

			final SapCpiOrderPriceComponent sapCpiOrderPriceComponent = new SapCpiOrderPriceComponent();

			sapCpiOrderPriceComponent.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
			sapCpiOrderPriceComponent.setEntryNumber(mapAttribute(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, row));
			sapCpiOrderPriceComponent.setConditionCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CODE, row));
			sapCpiOrderPriceComponent
					.setTaxJurisdictionCode(mapAttribute(GallagherSalesConditionCsvColumns.TAX_JURISDICTION_CODE, row));
			sapCpiOrderPriceComponent.setConditionCounter(mapAttribute(SalesConditionCsvColumns.CONDITION_COUNTER, row));
			sapCpiOrderPriceComponent.setCurrencyIsoCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, row));
			sapCpiOrderPriceComponent.setPriceQuantity(mapAttribute(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, row));
			sapCpiOrderPriceComponent.setUnit(mapAttribute(SalesConditionCsvColumns.CONDITION_UNIT_CODE, row));
			sapCpiOrderPriceComponent.setValue(mapAttribute(SalesConditionCsvColumns.CONDITION_VALUE, row));
			sapCpiOrderPriceComponent.setAbsolute(mapAttribute(SalesConditionCsvColumns.ABSOLUTE, row));

			sapCpiOrderPriceComponents.add(sapCpiOrderPriceComponent);

		});

		return sapCpiOrderPriceComponents;

	}
}
