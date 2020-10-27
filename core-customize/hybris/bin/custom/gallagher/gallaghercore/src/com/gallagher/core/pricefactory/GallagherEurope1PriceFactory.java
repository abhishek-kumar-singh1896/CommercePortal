/**
 *
 */
package com.gallagher.core.pricefactory;

import de.hybris.platform.catalog.jalo.CatalogAwareEurope1PriceFactory;
import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.constants.GeneratedEurope1Constants.TC;
import de.hybris.platform.europe1.jalo.AbstractDiscountRow;
import de.hybris.platform.europe1.jalo.DiscountRow;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder.QueryWithParams;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

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
	private static final String CURRENT_SESSION_SALES_AREA = "currentSessionSalesArea";

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

		if (StringUtils.isBlank(salesArea))
		{
			final Collection<PriceRow> result = super.queryPriceRows4Price(ctx, product, productGroup, user, userGroup, date,
					currency, giveAwayMode);
			return result;
		}
		else
		{
			final GallagherPDTRowsQueryBuilder builder = this.getPDTRowsQueryBuilderFor(TC.PRICEROW);
			final QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withAnySalesArea().withProduct(productPk)
					.withProductId(productId).withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk)
					.withSalesArea(salesArea).build();



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

		final GallagherPDTRowsQueryBuilder builder = this.getPDTRowsQueryBuilderFor(discountRowTypeCode);
		final QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withAnySalesArea().withProduct(productPk)
				.withProductId(productId).withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk)
				.withSalesArea(salesArea).build();

		return FlexibleSearch.getInstance()
				.search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), DiscountRow.class)
				.getResult();


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
					final List<GallagherDiscountRow> discList = new ArrayList<>();
					discList.add(ret.get(0));
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
						return row1.getPK().compareTo(row2.getPK());
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
}
