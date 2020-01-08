package com.enterprisewide.b2badvance.core.product.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Date;
import java.util.List;


/**
 * The Interface B2BAdvancePriceDao. Provides data access operations for prices
 *
 * @author Enterprise Wide
 */
public interface B2BAdvancePriceDao
{

	/**
	 * Returns the price for specific product for specific dates
	 *
	 * @param product
	 *           for which price needs to be fetched
	 * @param unit
	 * @param currency
	 * @param startDate
	 *           of the price
	 * @param endDate
	 *           of the price
	 * @param catalogVersion
	 *           of the price row
	 * @return the list of price rows matching criteria
	 */
	List<PriceRowModel> getPriceRowForDate(final ProductModel product, CurrencyModel currency, UnitModel unit,
			final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion);

	/**
	 * Returns the price for specific product for specific dates and UPG
	 *
	 * @param product
	 *           for which price needs to be fetched
	 * @param unit
	 * @param currency
	 * @param startDate
	 *           of the price
	 * @param endDate
	 *           of the price
	 * @param userPriceGroup
	 *           of the price row
	 * @param catalogVersion
	 *           of the price row
	 * @return the list of price rows matching criteria
	 */
	List<PriceRowModel> getPriceRowForDateAndUG(final ProductModel product, CurrencyModel currency, UnitModel unit,
			final Date startDate, final Date endDate, final UserPriceGroup userPriceGroup, final CatalogVersionModel catalogVersion);

}
