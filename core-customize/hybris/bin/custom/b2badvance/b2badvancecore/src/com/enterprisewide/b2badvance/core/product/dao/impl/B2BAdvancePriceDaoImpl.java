package com.enterprisewide.b2badvance.core.product.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
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

import com.enterprisewide.b2badvance.core.product.dao.B2BAdvancePriceDao;


/**
 * Class contains all the data access methods for extended functionalities for Price.
 *
 * @author Enterprise Wide
 */
public class B2BAdvancePriceDaoImpl extends DefaultGenericDao<PriceRowModel> implements B2BAdvancePriceDao
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(B2BAdvancePriceDaoImpl.class);

	public static final String FOUND_PRICE_ROW_S = "Found {} price row(s)";

	/** The Constant FIND_PRICEROW_FOR_DATE */
	private static final String FIND_PRICEROW_FOR_DATE = new StringBuilder().append("SELECT {price.").append(PriceRowModel.PK)
			.append("} FROM {").append(PriceRowModel._TYPECODE).append(" as price} where {price.")
			.append(PriceRowModel.CATALOGVERSION).append("}=?").append(CatalogVersionModel._TYPECODE).append(" and {price.")
			.append(PriceRowModel.PRODUCT).append("}=?").append(PriceRowModel.PRODUCT).append(" and {price.")
			.append(PriceRowModel.CURRENCY).append("}=?").append(PriceRowModel.CURRENCY).append(" and {price.")
			.append(PriceRowModel.UNIT).append("}=?").append(PriceRowModel.UNIT).toString().intern();

	/** The Constant FIND_PRICEROW_FOR_DATE_AND_UG. */
	private static final String FIND_PRICEROW_FOR_DATE_AND_USERGROUP = new StringBuilder(FIND_PRICEROW_FOR_DATE)
			.append(" and {price.").append(PriceRowModel.UG).append("}=?").append(PriceRowModel.UG).toString().intern();

	/** The Constant FIND_PRICEROW_FOR_DATE_STORE. */
	private static final String FIND_PRICEROW_FOR_DATE_WITHOUT_USERGROUP = new StringBuilder(FIND_PRICEROW_FOR_DATE)
			.append(" and {price.").append(PriceRowModel.UG).append("} is null").toString().intern();

	/** The date format. */
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Instantiates a new price dao impl.
	 *
	 * @param typecode
	 *           the typecode
	 */
	public B2BAdvancePriceDaoImpl(final String typecode)
	{
		super(typecode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PriceRowModel> getPriceRowForDate(final ProductModel product, final CurrencyModel currency, final UnitModel unit,
			final Date startDate, final Date endDate, final CatalogVersionModel catalogVersion)
	{
		LOG.debug("Entering getPriceRowForDate()");


		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CatalogVersionModel._TYPECODE, catalogVersion);
		params.put(PriceRowModel.PRODUCT, product);
		params.put(PriceRowModel.UNIT, unit);
		params.put(PriceRowModel.CURRENCY, currency);

		final StringBuilder query = new StringBuilder(FIND_PRICEROW_FOR_DATE_WITHOUT_USERGROUP);
		addDateCondition(startDate, endDate, params, query);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
		final SearchResult<PriceRowModel> searchResult = getFlexibleSearchService().search(searchQuery);
		final List<PriceRowModel> priceList = searchResult.getResult();
		LOG.debug(FOUND_PRICE_ROW_S, Integer.valueOf(priceList.size()));
		return priceList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PriceRowModel> getPriceRowForDateAndUG(final ProductModel product, final CurrencyModel currency,
			final UnitModel unit, final Date startDate, final Date endDate, final UserPriceGroup userPriceGroup,
			final CatalogVersionModel catalogVersion)
	{
		LOG.debug("Entering getPriceRowForDateAndUG()");

		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put(CatalogVersionModel._TYPECODE, catalogVersion);
		params.put(PriceRowModel.PRODUCT, product);
		params.put(PriceRowModel.UG, userPriceGroup);
		params.put(PriceRowModel.UNIT, unit);
		params.put(PriceRowModel.CURRENCY, currency);

		final StringBuilder query = new StringBuilder(FIND_PRICEROW_FOR_DATE_AND_USERGROUP);
		addDateCondition(startDate, endDate, params, query);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
		final SearchResult<PriceRowModel> searchResult = getFlexibleSearchService().search(searchQuery);
		final List<PriceRowModel> priceList = searchResult.getResult();
		LOG.debug(FOUND_PRICE_ROW_S, Integer.valueOf(priceList.size()));
		return priceList;
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @param params
	 * @param query
	 */
	private void addDateCondition(final Date startDate, final Date endDate, final Map<String, Object> params,
			final StringBuilder query)
	{
		if (startDate != null)
		{
			final String formattedStartDate = dateFormat.format(startDate);
			params.put(PriceRowModel.STARTTIME, formattedStartDate);
			query.append(" and {price.").append(PriceRowModel.STARTTIME).append("}=?").append(PriceRowModel.STARTTIME);
		}
		if (endDate != null)
		{
			final String formattedEndDate = dateFormat.format(endDate);
			params.put(PriceRowModel.ENDTIME, formattedEndDate);
			query.append(" and {price.").append(PriceRowModel.ENDTIME).append("}=?").append(PriceRowModel.ENDTIME);
		}
	}



}
