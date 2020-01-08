package com.enterprisewide.b2badvance.core.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static java.lang.String.format;

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
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.enterprisewide.b2badvance.core.product.B2BAdvancePriceService;
import com.enterprisewide.b2badvance.core.product.dao.B2BAdvanceDiscountDao;
import com.enterprisewide.b2badvance.core.product.dao.B2BAdvancePriceDao;


/**
 * Service implementation of {@link B2BAdvancePriceService}
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePriceServiceImpl implements B2BAdvancePriceService
{

	private B2BAdvancePriceDao priceDao;

	private B2BAdvanceDiscountDao discountDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PriceRowModel getPriceRowForDate(final ProductModel product, final CurrencyModel currency, final UnitModel unit,
			final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion)
	{
		final List<PriceRowModel> prices = getPriceDao().getPriceRowForDate(product, currency, unit, startDate, endDate,
				catalogVersion);

		validateIfSingleResult(prices, format("Prices for product '%s' not found!", product.getCode()),
				format("Product prices '%s' is not unique, %d products prices found with same criteria!", product.getCode(),
						Integer.valueOf(prices.size())));
		return prices.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PriceRowModel getPriceRowForDateAndUG(final ProductModel product, final CurrencyModel currency, final UnitModel unit,
			final Date startDate, final Date endDate, final UserPriceGroup userPriceGroup, final CatalogVersionModel catalogVersion)
	{
		final List<PriceRowModel> prices = getPriceDao().getPriceRowForDateAndUG(product, currency, unit, startDate, endDate,
				userPriceGroup, catalogVersion);
		validateIfSingleResult(prices, format("Prices for Price Group '%s' not found!", userPriceGroup.getCode()),
				format("Product prices '%s' is not unique, %d products prices found with same criteria!", userPriceGroup.getCode(),
						Integer.valueOf(prices.size())));
		return prices.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DiscountRowModel getDiscountRowForDate(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion)
	{
		final List<DiscountRowModel> dicounts = getDiscountDao().getDiscountRowForDate(product, discountModel, currency, startDate,
				endDate, catalogVersion);
		validateIfSingleResult(dicounts, format("Discounts for product '%s' not found!", product.getCode()),
				format("Product discounts '%s' is not unique, %d products discounts found with same criteria!", product.getCode(),
						Integer.valueOf(dicounts.size())));
		return dicounts.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DiscountRowModel getDiscountRowForDateAndUG(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final UserDiscountGroup userDiscountGroup,
			final CatalogVersionModel catalogVersion)
	{
		final List<DiscountRowModel> dicounts = getDiscountDao().getDiscountRowForDateAndUG(product, discountModel, currency,
				startDate, endDate, userDiscountGroup, catalogVersion);
		validateIfSingleResult(dicounts, format("Discounts for Discount Group '%s' not found!", userDiscountGroup.getCode()),
				format("Product discounts '%s' is not unique, %d products discounts found with same criteria!",
						userDiscountGroup.getCode(), Integer.valueOf(dicounts.size())));
		return dicounts.get(0);
	}

	protected B2BAdvancePriceDao getPriceDao()
	{
		return priceDao;
	}

	@Required
	public void setPriceDao(final B2BAdvancePriceDao priceDao)
	{
		this.priceDao = priceDao;
	}

	protected B2BAdvanceDiscountDao getDiscountDao()
	{
		return discountDao;
	}

	@Required
	public void setDiscountDao(final B2BAdvanceDiscountDao discountDao)
	{
		this.discountDao = discountDao;
	}
}
