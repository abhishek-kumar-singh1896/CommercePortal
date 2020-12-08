package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SaporderexchangeConstants;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultSalesConditionsContributor;
import de.hybris.platform.util.TaxValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gallagher.constants.GallagherSalesConditionCsvColumns;


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
				final String[] taxCodeArray = next.getCode().split(":");
				row.put(OrderCsvColumns.ORDER_ID, order.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getEntryNumber());
				row.put(SalesConditionCsvColumns.CONDITION_CODE, taxCodeArray[0].trim());
				if (taxCodeArray.length >= 2)
				{
					row.put(SalesConditionCsvColumns.CONDITION_VALUE, taxCodeArray[1].trim());
				}
				//row.put(SalesConditionCsvColumns.CONDITION_COUNTER, getConditionCounterTax());
				row.put(GallagherSalesConditionCsvColumns.CONDITION_TOTAL_AMOUNT, next.getValue());

				if (next.isAbsolute())
				{
					row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
					//row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
					//row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
					//row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
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

	@Override
	protected void createGrossPriceRow(final OrderModel order, final List<Map<String, Object>> result,
			final AbstractOrderEntryModel entry)
	{
		final Map<String, Object> row = new HashMap<>();
		row.put(OrderCsvColumns.ORDER_ID, order.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, entry.getEntryNumber());
		row.put(SalesConditionCsvColumns.CONDITION_CODE, getGrossPrice());
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, entry.getBasePrice());
		row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
		row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, getConditionCounterGrossPrice());
		row.put(GallagherSalesConditionCsvColumns.CONDITION_TOTAL_AMOUNT, entry.getTotalPrice());
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", order.getCode());
		result.add(row);
	}

	@Override
	protected void createPaymentCostRow(final OrderModel order, final List<Map<String, Object>> result)
	{
		if (order.getPaymentCost() != null && order.getPaymentCost() > 0)
		{
			if (null != order.getUnit())
			{
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, order.getCode());
				row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, SaporderexchangeConstants.HEADER_ENTRY);
				String conditionCode = null;
				if (null != order.getUnit().getSalesArea() && order.getUnit().getSalesArea().contains("_"))
				{
					conditionCode = order.getUnit().getSalesArea();
					conditionCode = conditionCode.substring(conditionCode.indexOf("_") + 1, conditionCode.lastIndexOf("_"));
				}
				row.put(SalesConditionCsvColumns.CONDITION_CODE, conditionCode);
				row.put(SalesConditionCsvColumns.CONDITION_VALUE, order.getPaymentCost());
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.CONDITION_COUNTER, getConditionCounterPaymentCost());
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
				getBatchIdAttributes().forEach(row::putIfAbsent);
				row.put("dh_batchId", order.getCode());
				result.add(row);
			}
			else
			{
				super.createPaymentCostRow(order, result);
			}
		}
	}

	@Override
	protected void createDeliveryCostRow(final OrderModel order, final List<Map<String, Object>> result)
	{
		if (order.getDeliveryCost() != null && order.getDeliveryCost() > 0)
		{
			super.createDeliveryCostRow(order, result);
		}
	}

}
