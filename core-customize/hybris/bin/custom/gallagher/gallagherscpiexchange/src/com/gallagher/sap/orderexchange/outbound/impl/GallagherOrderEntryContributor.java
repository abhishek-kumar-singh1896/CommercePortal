package com.gallagher.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultOrderEntryContributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;

import com.gallagher.constants.GallagherOrderEntryCsvColumns;


/**
 * Builds the Row map for the CSV files for the Order Entry
 *
 * @author Vikram Bishnoi
 */
public class GallagherOrderEntryContributor extends DefaultOrderEntryContributor
{
	private final static Logger LOG = Logger.getLogger(GallagherOrderEntryContributor.class);

	@Override
	protected String determineItemShortText(final AbstractOrderEntryModel item, final String language)
	{
		final String shortText = item.getProduct().getName(LocaleUtils.toLocale(language));
		return shortText == null ? "" : shortText;
	}

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final AbstractOrderEntryModel entry : entries)
		{
			final Map<String, Object> row = new HashMap<>();
			if (StringUtils.isNotEmpty(entry.getProductSpecificDetailsSubHeading())
					&& StringUtils.isNotEmpty(entry.getDeliveryInstruction()))
			{
				row.put(GallagherOrderEntryCsvColumns.ADDITION_PRODUCT_DETAIL,
						entry.getProductSpecificDetailsSubHeading() + entry.getDeliveryInstruction());
			}
			else
			{
				row.put(GallagherOrderEntryCsvColumns.ADDITION_PRODUCT_DETAIL, "");
			}

			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(OrderEntryCsvColumns.ENTRY_NUMBER, entry.getEntryNumber());
			row.put(OrderEntryCsvColumns.QUANTITY, entry.getQuantity());
			row.put(OrderEntryCsvColumns.PRODUCT_CODE, entry.getProduct().getCode());
			final UnitModel unit = entry.getUnit();
			if (unit != null)
			{
				row.put(OrderEntryCsvColumns.ENTRY_UNIT_CODE, unit.getCode());
			}
			else
			{
				LOG.warn("Could not determine unit code for product " + entry.getProduct().getCode() + "as entry "
						+ entry.getEntryNumber() + "of order " + order.getCode());
			}
			row.put(OrderEntryCsvColumns.EXTERNAL_PRODUCT_CONFIGURATION, getProductConfigurationData(entry));
			String language = order.getLanguage().getIsocode();
			String shortText = determineItemShortText(entry, language);

			if (shortText.isEmpty())
			{
				final List<LanguageModel> fallbackLanguages = order.getLanguage().getFallbackLanguages();
				if (!fallbackLanguages.isEmpty())
				{
					language = fallbackLanguages.get(0).getIsocode();
					shortText = determineItemShortText(entry, language);
				}
			}
			row.put(OrderEntryCsvColumns.PRODUCT_NAME, shortText);

			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", order.getCode());

			result.add(row);
		}

		return result;
	}

}
