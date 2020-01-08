package com.enterprisewide.b2badvance.core.product.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;

import java.util.Date;
import java.util.List;


/**
 * The Interface B2BAdvanceDiscountDao. Provides data access operations for discounts
 *
 * @author Enterprise Wide
 */
public interface B2BAdvanceDiscountDao
{

	/**
	 * Returns the discount for specific product for specific dates
	 *
	 * @param product
	 *           for which discount needs to be fetched
	 * @param discountModel
	 *           discount model
	 * @param currency
	 * @param startDate
	 *           of the discount
	 * @param endDate
	 *           of the discount
	 * @param catalogVersion
	 *           of the discount row
	 * @return the list of discount rows matching criteria
	 */
	List<DiscountRowModel> getDiscountRowForDate(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion);


	/**
	 * Returns the discount for specific product for specific dates and UPG
	 *
	 * @param product
	 *           for which discount needs to be fetched
	 * @param discountModel
	 *           discount model
	 * @param currency
	 * @param startDate
	 *           of the discount
	 * @param endDate
	 *           of the discount
	 * @param userDiscountGroup
	 *           of the discount row
	 * @param catalogVersion
	 *           of the discount row
	 * @return the list of discount rows matching criteria
	 */
	List<DiscountRowModel> getDiscountRowForDateAndUG(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final UserDiscountGroup userDiscountGroup,
			final CatalogVersionModel catalogVersion);

}
