package com.enterprisewide.b2badvance.core.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Date;


/**
 * Provides operations for product prices
 *
 * @author Enterprise Wide
 */
public interface B2BAdvancePriceService
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
	 * @return the price row
	 */
	PriceRowModel getPriceRowForDate(final ProductModel product, CurrencyModel currency, UnitModel unit, final Date startDate,
			final Date endDate, final CatalogVersionModel catalogVersion);

	/**
	 * Returns the price for specific product for specific dates and user group
	 *
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
	 * @return the price row
	 */
	PriceRowModel getPriceRowForDateAndUG(final ProductModel product, final CurrencyModel currency, final UnitModel unit,
			final Date startDate, final Date endDate, final UserPriceGroup userPriceGroup, final CatalogVersionModel catalogVersion);

	/**
	 * Returns discount row for product for specific date
	 *
	 * @param product
	 *           for which discount needs to be fetched
	 * @param discountModel
	 *           discount model
	 * @param currency
	 *           of discount
	 * @param startDate
	 *           of the discount
	 * @param endDate
	 *           of the discount
	 * @param catalogVersion
	 *           of discount row
	 * @return the discount row
	 */
	DiscountRowModel getDiscountRowForDate(ProductModel product, DiscountModel discountModel, CurrencyModel currency,
			Date startDate, Date endDate, CatalogVersionModel catalogVersion);

	/**
	 * Returns discount row for product for specific date and user group
	 *
	 * @param product
	 *           for which discount needs to be fetched
	 * @param discountModel
	 *           discount model
	 * @param currency
	 *           of discount
	 * @param startDate
	 *           of the discount
	 * @param endDate
	 *           of the discount
	 * @param userDiscountGroup
	 *           for the discount row
	 * @param catalogVersion
	 *           of discount row
	 * @return the discount row
	 */
	DiscountRowModel getDiscountRowForDateAndUG(ProductModel product, DiscountModel discountModel, CurrencyModel currency,
			Date startDate, Date endDate, UserDiscountGroup userDiscountGroup, CatalogVersionModel catalogVersion);
}
