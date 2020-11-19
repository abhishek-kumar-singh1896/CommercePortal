/**
 *
 */
package com.gallagher.core.pdt.query.builder;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder;

import com.gallagher.core.pdt.query.builder.impl.GallagherPDTRowsQueryBuilderImpl;


/**
 * Query builder for Sales Area based Price Rows.
 *
 * @author Nagarro-Dev
 *
 */
public interface GallagherPDTRowsQueryBuilder extends PDTRowsQueryBuilder
{

	static GallagherPDTRowsQueryBuilder getGallagherBuilder(final String type)
	{
		return new GallagherPDTRowsQueryBuilderImpl(type);
	}

	GallagherPDTRowsQueryBuilder withAnySalesArea();

	GallagherPDTRowsQueryBuilder withSalesArea(final String arg0);

	GallagherPDTRowsQueryBuilder withAnyCustomerGroup();

	GallagherPDTRowsQueryBuilder withCustomerGroup(final String arg0);

	@Override
	GallagherPDTRowsQueryBuilder withAnyProduct();

	@Override
	GallagherPDTRowsQueryBuilder withProduct(PK arg0);

	@Override
	GallagherPDTRowsQueryBuilder withProductGroup(PK arg0);

	@Override
	GallagherPDTRowsQueryBuilder withProductId(String arg0);

	@Override
	GallagherPDTRowsQueryBuilder withAnyUser();

	@Override
	GallagherPDTRowsQueryBuilder withUser(PK arg0);

	@Override
	GallagherPDTRowsQueryBuilder withUserGroup(PK arg0);

}
