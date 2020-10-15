/**
 *
 */
package com.gallagher.core.pricefactory;

import de.hybris.platform.catalog.jalo.CatalogAwareEurope1PriceFactory;
import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.constants.GeneratedEurope1Constants.TC;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder.QueryWithParams;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

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

		final GallagherPDTRowsQueryBuilder builder = this.getPDTRowsQueryBuilderFor(TC.PRICEROW);
		final QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withAnySalesArea().withProduct(productPk)
				.withProductId(productId).withProductGroup(productGroupPk).withUser(userPk).withUserGroup(userGroupPk)
				.withSalesArea(salesArea).build();

		return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), PriceRow.class)
				.getResult();

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
