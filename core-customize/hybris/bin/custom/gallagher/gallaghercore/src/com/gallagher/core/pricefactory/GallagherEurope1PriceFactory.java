/**
 *
 */
package com.gallagher.core.pricefactory;

import de.hybris.platform.catalog.jalo.CatalogAwareEurope1PriceFactory;
import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.europe1.constants.GeneratedEurope1Constants.TC;
import de.hybris.platform.europe1.jalo.AbstractDiscountRow;
import de.hybris.platform.europe1.jalo.DiscountRow;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder.QueryWithParams;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DateRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gallagher.core.jalo.GallagherDiscountRow;
import com.gallagher.core.pdt.query.builder.GallagherPDTRowsQueryBuilder;


/**
 *
 * The Price Factory for fetching Sales Area based price rows.
 *
 * @author Nagarro-Dev
 *
 */
public class GallagherEurope1PriceFactory extends CatalogAwareEurope1PriceFactory
{
	private static final Logger LOG = Logger.getLogger(GallagherEurope1PriceFactory.class);

	private static final String CURRENT_SESSION_SALES_AREA = "currentSessionSalesArea";
	private static final String CURRENT_SESSION_CUSTOMER_GROUP = "currentSessionCustomerGroup";
	private static final String SEPARATOR = "_";

	@Resource(name = "modelService")
	private ModelService modelService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Collection<PriceRow> queryPriceRows4Price(final SessionContext ctx, final Product product,
			final EnumerationValue productGroup, final User user, final EnumerationValue userGroup, final Date date,
			final Currency currency, final boolean giveAwayMode)
	{
		final PK productPk = product == null ? null : product.getPK();
		final PK productGroupPk = productGroup == null ? null : productGroup.getPK();
		final PK userPk = user == null ? null : user.getPK();
		final PK userGroupPk = userGroup == null ? null : userGroup.getPK();
		final String productId = this.extractProductId(ctx, product);

		final String salesArea = getSalesArea(ctx);
		final String customerGroup = getCustomerGroup(ctx);

		//		LOG.info("Customer group in session during price ::" + customerGroup);
		if (StringUtils.isBlank(salesArea))
		{
			//			LOG.info("Sales area is blank calling OOTB method for price");
			final Collection<PriceRow> result = super.queryPriceRows4Price(ctx, product, productGroup, user, userGroup, date,
					currency, giveAwayMode);
			return result;
		}
		else
		{
			final GallagherPDTRowsQueryBuilder builder = this.getPDTRowsQueryBuilderFor(TC.PRICEROW);
			final QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withAnySalesArea().withAnyCustomerGroup()
					.withProduct(productPk).withProductId(productId).withProductGroup(productGroupPk).withUser(userPk)
					.withUserGroup(userGroupPk).withSalesArea(salesArea).withCustomerGroup(customerGroup).build();



			return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), PriceRow.class)
					.getResult();
		}
	}


	@Override
	protected Collection<? extends AbstractDiscountRow> queryDiscounts4Price(final SessionContext ctx, final Product product,
			final EnumerationValue productGroup, final User user, final EnumerationValue userGroup)
	{

		final boolean global = product == null && productGroup == null;
		final String discountRowTypeCode = global ? TC.GLOBALDISCOUNTROW : TC.DISCOUNTROW;

		final PK productPk = product == null ? null : product.getPK();
		final PK productGroupPk = productGroup == null ? null : productGroup.getPK();
		final PK userPk = user == null ? null : user.getPK();
		final PK userGroupPk = userGroup == null ? null : userGroup.getPK();
		final String productId = this.extractProductId(ctx, product);

		final String salesArea = getSalesArea(ctx);
		final String customerGroup = getCustomerGroup(ctx);

		//		LOG.info("Customer group in session during price ::" + customerGroup);
		if (StringUtils.isBlank(salesArea) || !salesArea.contains(SEPARATOR))
		{
			//			LOG.info("Sales area is blank calling OOTB method for discount");
			final Collection<? extends AbstractDiscountRow> result = super.queryDiscounts4Price(ctx, product, productGroup, user,
					userGroup);
			return result;
		}

		final int index = salesArea.lastIndexOf(SEPARATOR);
		final String salesAreaSubString = salesArea.substring(0, index);

		final GallagherPDTRowsQueryBuilder builder = this.getPDTRowsQueryBuilderFor(discountRowTypeCode);
		final QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withAnySalesArea().withAnyCustomerGroup()
				.withProduct(productPk).withProductId(productId).withProductGroup(productGroupPk).withUser(userPk)
				.withUserGroup(userGroupPk).withSalesArea(salesAreaSubString).withCustomerGroup(customerGroup).build();

		return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), DiscountRow.class)
				.getResult();


	}

	@Override
	public List getDiscountValues(final AbstractOrderEntry entry) throws JaloPriceFactoryException
	{
		final SessionContext ctx = this.getSession().getSessionContext();
		final AbstractOrder order = entry.getOrder(ctx);
		return Europe1Tools.createDiscountValueList(
				this.matchDiscountRows(entry.getProduct(ctx), this.getPDG(ctx, entry), order.getUser(ctx), this.getUDG(ctx, entry),
						order.getCurrency(ctx), order.getDate(ctx), -1, entry.getQuantity(ctx), order.getCurrency(ctx)));
	}


	public List matchDiscountRows(final Product product, final EnumerationValue productGroup, final User user,
			final EnumerationValue userGroup, final Currency curr, final Date date, final int maxCount, final Long qtd,
			final Currency currency) throws JaloPriceFactoryException
	{
		if (user == null && userGroup == null)
		{
			throw new JaloPriceFactoryException("cannot match discounts without user and user group - at least one must be present",
					0);
		}
		else if (curr == null)
		{
			throw new JaloPriceFactoryException("cannot match price without currency", 0);
		}
		else if (date == null)
		{
			throw new JaloPriceFactoryException("cannot match price without date", 0);
		}
		else
		{
			final Collection<? extends AbstractDiscountRow> rows = this.queryDiscounts4Price(this.getSession().getSessionContext(),
					product, productGroup, user, userGroup);
			if (!rows.isEmpty())
			{
				final List<GallagherDiscountRow> ret = (List<GallagherDiscountRow>) filterDiscountRows4Price(rows, qtd, date);
				if (ret.size() > 1)
				{
					ret.sort(new GallagherEurope1PriceFactory.DiscountRowMatchComparator());
					final List<GallagherDiscountRow> discList = new ArrayList<>();
					discList.add(ret.get(0));
					//					LOG.info("total discount rows matched " + discList.size());
					return discList;
				}

				return ret;
			}
			else
			{
				return Collections.EMPTY_LIST;
			}
		}
	}


	protected List<? extends AbstractDiscountRow> filterDiscountRows4Price(final Collection<? extends AbstractDiscountRow> rows,
			final Long _quantity, final Date date)
	{
		if (rows.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			final List<? extends AbstractDiscountRow> ret = new ArrayList<>(rows);
			final ListIterator it = ret.listIterator();
			final long quantity = _quantity == 0L ? 1L : _quantity;

			while (true)
			{
				GallagherDiscountRow discRow;
				while (it.hasNext())
				{
					discRow = (GallagherDiscountRow) it.next();
					if (quantity < Long.valueOf(discRow.getQuantityAsPrimitive()))
					{
						//						LOG.info("removing discount row as it doesnt match quantity");
						it.remove();
					}
					else
					{
						final DateRange dataRange = discRow.getDateRange();
						if (dataRange != null && !dataRange.encloses(date))
						{
							it.remove();
						}
					}
				}
				return ret;
			}
		}
	}

	@Override
	public List matchDiscountRows(final Product product, final EnumerationValue productGroup, final User user,
			final EnumerationValue userGroup, final Currency curr, final Date date, final int maxCount)
			throws JaloPriceFactoryException
	{
		if (user == null && userGroup == null)
		{
			throw new JaloPriceFactoryException("cannot match discounts without user and user group - at least one must be present",
					0);
		}
		else if (curr == null)
		{
			throw new JaloPriceFactoryException("cannot match price without currency", 0);
		}
		else if (date == null)
		{
			throw new JaloPriceFactoryException("cannot match price without date", 0);
		}
		else
		{
			final Collection<? extends AbstractDiscountRow> rows = this.queryDiscounts4Price(this.getSession().getSessionContext(),
					product, productGroup, user, userGroup);
			if (!rows.isEmpty())
			{
				final List<GallagherDiscountRow> ret = (List<GallagherDiscountRow>) filterDiscountRows4Price(rows, date);
				if (ret.size() > 1)
				{
					ret.sort(new GallagherEurope1PriceFactory.DiscountRowMatchComparator());
					return getDiscountRows(ret);
				}

				return getDiscountRows(ret);
			}
			else
			{
				return Collections.EMPTY_LIST;
			}
		}
	}

	private List<GallagherDiscountRow> getDiscountRows(final List<GallagherDiscountRow> ret)
	{
		final List<GallagherDiscountRow> discList = new ArrayList<>();
		final Optional<GallagherDiscountRow> rowOptional = ret.stream().filter(r -> r.getMinQuantity() <= 1).findFirst();
		final GallagherDiscountRow row = rowOptional.isPresent() ? rowOptional.get() : null;
		if (row == null)
		{
			return Collections.EMPTY_LIST;
		}
		discList.add(row);
		return discList;
	}

	protected class DiscountRowMatchComparator implements Comparator<GallagherDiscountRow>
	{
		protected DiscountRowMatchComparator()
		{
		}

		public int compare(final GallagherDiscountRow row1, final GallagherDiscountRow row2)
		{
			final int product1 = row1.getDiscount().getPriority();
			final int product2 = row2.getDiscount().getPriority();
			if (product1 != product2)
			{
				return product1 - product2;
			}
			else
			{
				final boolean c1Set = row1.getCurrency() != null;
				final boolean c2Set = row2.getCurrency() != null;
				if (c1Set != c2Set)
				{
					return c1Set ? -1 : 1;
				}
				else
				{
					// match value
					final int matchValue1 = row1.getMatchValue().intValue();
					final int matchValue2 = row2.getMatchValue().intValue();
					if (matchValue1 != matchValue2)
					{
						// DESC -> row is better if match value is greater !
						return matchValue2 - matchValue1;
					}
					else
					{
						// quantity value
						final long qty1 = row1.getMinQuantity();
						final long qty2 = row2.getMinQuantity();
						if (qty2 != qty1)
						{
							// DESC -> row is better if qty value is greater !
							return (int) (qty2 - qty1);
						}
						else
						{
							return row1.getPK().compareTo(row2.getPK());
						}
					}
				}
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GallagherPDTRowsQueryBuilder getPDTRowsQueryBuilderFor(final String type)
	{
		return GallagherPDTRowsQueryBuilder.getGallagherBuilder(type);
	}

	/**
	 * Method to get store from session context
	 *
	 * @param ctx
	 *           the session context
	 * @return Sales Area
	 */
	public String getSalesArea(final SessionContext ctx)
	{
		return (String) ctx.getAttributes().get(CURRENT_SESSION_SALES_AREA);
	}

	/**
	 * Method to get store from session context
	 *
	 * @param ctx
	 *           the session context
	 * @return Customer Group
	 */
	public String getCustomerGroup(final SessionContext ctx)
	{
		return (String) ctx.getAttributes().get(CURRENT_SESSION_CUSTOMER_GROUP);
	}
}
