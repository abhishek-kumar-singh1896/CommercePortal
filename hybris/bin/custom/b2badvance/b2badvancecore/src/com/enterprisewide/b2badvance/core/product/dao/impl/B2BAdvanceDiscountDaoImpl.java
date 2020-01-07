package com.enterprisewide.b2badvance.core.product.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisewide.b2badvance.core.product.dao.B2BAdvanceDiscountDao;


/**
 * Class contains all the data access methods for extended functionalities for Discounts.
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceDiscountDaoImpl extends DefaultGenericDao<DiscountRowModel> implements B2BAdvanceDiscountDao
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvanceDiscountDaoImpl.class);

	public static final String FOUND_PRICE_ROW_S = "Found {} discount row(s)";

	/** The Constant FIND_DISCOUNTROW_FOR_DATE */
	private static final String FIND_DISCOUNTROW_FOR_DATE = new StringBuilder().append("SELECT {discount.")
			.append(DiscountRowModel.PK).append("} FROM {").append(DiscountRowModel._TYPECODE)
			.append(" as discount} where {discount.").append(DiscountRowModel.CATALOGVERSION).append("}=?")
			.append(CatalogVersionModel._TYPECODE).append(" and {discount.").append(DiscountRowModel.PRODUCT).append("}=?")
			.append(DiscountRowModel.PRODUCT).append(" and {discount.").append(DiscountRowModel.DISCOUNT).append("}=?")
			.append(DiscountRowModel.DISCOUNT).toString().intern();

	/** The Constant FIND_DISCOUNTROW_FOR_DATE_AND_UG. */
	private static final String FIND_DISCOUNTROW_FOR_DATE_AND_USERGROUP = new StringBuilder(FIND_DISCOUNTROW_FOR_DATE)
			.append(" and {discount.").append(DiscountRowModel.UG).append("}=?").append(DiscountRowModel.UG).toString().intern();

	/** The Constant FIND_DISCOUNTROW_FOR_DATE_STORE. */
	private static final String FIND_DISCOUNTROW_FOR_DATE_WITHOUT_USERGROUP = new StringBuilder(FIND_DISCOUNTROW_FOR_DATE)
			.append(" and {discount.").append(DiscountRowModel.UG).append("} is null").toString().intern();

	/** The date format. */
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Instantiates a new discount dao impl.
	 *
	 * @param typecode
	 *           the typecode
	 */
	public B2BAdvanceDiscountDaoImpl(final String typecode)
	{
		super(typecode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DiscountRowModel> getDiscountRowForDate(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion)
	{
		LOG.debug("Entering getDiscountRowForDate()");


		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CatalogVersionModel._TYPECODE, catalogVersion);
		params.put(DiscountRowModel.PRODUCT, product);
		params.put(DiscountRowModel.DISCOUNT, discountModel);

		final StringBuilder query = new StringBuilder(FIND_DISCOUNTROW_FOR_DATE_WITHOUT_USERGROUP);
		addDateAndCurrencyCondition(startDate, endDate, currency, params, query);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
		final SearchResult<DiscountRowModel> searchResult = getFlexibleSearchService().search(searchQuery);
		final List<DiscountRowModel> priceList = searchResult.getResult();
		LOG.debug(FOUND_PRICE_ROW_S, Integer.valueOf(priceList.size()));
		return priceList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DiscountRowModel> getDiscountRowForDateAndUG(final ProductModel product, final DiscountModel discountModel,
			final CurrencyModel currency, final Date startDate, final Date endDate, final UserDiscountGroup userDiscountGroup,
			final CatalogVersionModel catalogVersion)
	{
		LOG.debug("Entering getDiscountRowForDateAndUG()");

		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CatalogVersionModel._TYPECODE, catalogVersion);
		params.put(DiscountRowModel.PRODUCT, product);
		params.put(DiscountRowModel.UG, userDiscountGroup);
		params.put(DiscountRowModel.DISCOUNT, discountModel);

		final StringBuilder query = new StringBuilder(FIND_DISCOUNTROW_FOR_DATE_AND_USERGROUP);

		addDateAndCurrencyCondition(startDate, endDate, currency, params, query);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
		final SearchResult<DiscountRowModel> searchResult = getFlexibleSearchService().search(searchQuery);
		final List<DiscountRowModel> priceList = searchResult.getResult();
		LOG.debug(FOUND_PRICE_ROW_S, Integer.valueOf(priceList.size()));
		return priceList;
	}

	/**
	 * Adds date condition in query
	 *
	 * @param startDate
	 *           of the discount
	 * @param endDate
	 *           of the discount
	 * @param currency
	 * @param params
	 *           of query
	 * @param query
	 *           to be appended with dates
	 */
	private void addDateAndCurrencyCondition(final Date startDate, final Date endDate, final CurrencyModel currency,
			final Map<String, Object> params, final StringBuilder query)
	{
		if (currency != null)
		{
			params.put(DiscountRowModel.CURRENCY, currency);
			query.append(" and {discount.").append(DiscountRowModel.CURRENCY).append("}=?").append(DiscountRowModel.CURRENCY);
		}
		if (startDate != null)
		{
			final String formattedStartDate = dateFormat.format(startDate);
			params.put(DiscountRowModel.STARTTIME, formattedStartDate);
			query.append(" and {discount.").append(DiscountRowModel.STARTTIME).append("}=?").append(DiscountRowModel.STARTTIME);
		}
		if (endDate != null)
		{
			final String formattedEndDate = dateFormat.format(endDate);
			params.put(DiscountRowModel.ENDTIME, formattedEndDate);
			query.append(" and {discount.").append(DiscountRowModel.ENDTIME).append("}=?").append(DiscountRowModel.ENDTIME);
		}
	}
}
