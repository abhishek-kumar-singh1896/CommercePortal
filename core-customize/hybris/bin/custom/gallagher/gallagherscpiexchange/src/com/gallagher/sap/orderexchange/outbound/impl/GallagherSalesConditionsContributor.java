package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultSalesConditionsContributor;
import de.hybris.platform.util.TaxValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Builds the Row map for the CSV files for the Sales Conditions in an Order
 *
 * @author Vikram Bishnoi
 */
public class GallagherSalesConditionsContributor extends DefaultSalesConditionsContributor
{

	private static final Logger LOGGER = LoggerFactory.getLogger(GallagherSalesConditionsContributor.class);

	@Override
	protected void createTaxRows(final OrderModel order, final List<Map<String, Object>> result,
			final AbstractOrderEntryModel entry)
	{
		final Iterator<TaxValue> taxIterator = entry.getTaxValues().iterator();
		while (taxIterator.hasNext())
		{
			final TaxValue next = taxIterator.next();
			/* Add only if tax is non zero which is a case for tax free regions like Virgin Islands etc. */
			if (next.getValue() > 0)
			{
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, order.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getEntryNumber());
				row.put(SalesConditionCsvColumns.CONDITION_CODE, next.getCode().split(":")[0].trim());
				row.put(SalesConditionCsvColumns.CONDITION_VALUE, next.getValue());
				row.put(SalesConditionCsvColumns.CONDITION_COUNTER, getConditionCounterTax());

				if (next.isAbsolute())
				{
					row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
					row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
					row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
					row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
				}
				else
				{
					row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.FALSE);
				}

				getBatchIdAttributes().forEach(row::putIfAbsent);
				row.put("dh_batchId", order.getCode());

				result.add(row);
				//break; // Consider multiple taxes for order entry
			}
		}
	}

}
