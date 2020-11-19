/**
 *
 */
package com.gallagher.core.pdt.query.builder.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.gallagher.core.pdt.query.builder.GallagherPDTRowsQueryBuilder;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;


/**
 *
 * Provides implementation of {@link GallagherPDTRowsQueryBuilder}
 *
 * @author Nagarro-Dev
 *
 */
public class GallagherPDTRowsQueryBuilderImpl implements GallagherPDTRowsQueryBuilder
{
	private final String type;
	private boolean anyProduct;
	private PK productPk;
	private PK productGroupPk;
	private String productId;
	private boolean anyUser;
	private PK userPk;
	private PK userGroupPk;
	private boolean anySalesArea;
	private String salesArea;
	private boolean anyCustomerGroup;
	private String customerGroup;

	public GallagherPDTRowsQueryBuilderImpl(final String type)
	{
		this.type = Objects.requireNonNull(type);
	}

	@Override
	public GallagherPDTRowsQueryBuilder withAnyProduct()
	{
		this.anyProduct = true;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withProduct(final PK productPk)
	{
		this.productPk = productPk;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withProductGroup(final PK productGroupPk)
	{
		this.productGroupPk = productGroupPk;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withProductId(final String productId)
	{
		this.productId = productId;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withAnyUser()
	{
		this.anyUser = true;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withUser(final PK userPk)
	{
		this.userPk = userPk;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withUserGroup(final PK userGroupPk)
	{
		this.userGroupPk = userGroupPk;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withAnySalesArea()
	{
		this.anySalesArea = true;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withSalesArea(final String salesArea)
	{
		this.salesArea = salesArea;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withAnyCustomerGroup()
	{
		this.anyCustomerGroup = true;
		return this;
	}

	@Override
	public GallagherPDTRowsQueryBuilder withCustomerGroup(final String customerGroup)
	{
		this.customerGroup = customerGroup;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryWithParams build()
	{
		final StringBuilder query = new StringBuilder();
		final Builder params = ImmutableMap.builder();
		final Map productParams = this.getProductRelatedParameters();
		final Map userParams = this.getUserRelatedParameters();
		final Map salesAreaParams = this.getSalesAreaRelatedParameters();
		final Map customerGroupParams = this.getCustomerGroupRelatedParameters();
		final boolean addPricesByProductId = this.productId != null;
		boolean isUnion = false;
		final boolean matchByProduct = !productParams.isEmpty();
		final boolean matchByUser = !userParams.isEmpty();
		final boolean matchBySalesArea = !salesAreaParams.isEmpty();
		final boolean matchByCustomerGroup = !customerGroupParams.isEmpty();

		if (!matchByProduct && !matchByUser && !addPricesByProductId && !matchBySalesArea && !matchByCustomerGroup)
		{
			return new QueryWithParams("select {PK} from {" + this.type + "}", Collections.emptyMap(), Collections.emptyList());
		}
		else
		{
			if (matchByProduct || matchByUser || matchBySalesArea || matchByCustomerGroup)
			{

				query.append("select {PK} from {").append(this.type).append("} where ");
				if (matchByProduct)
				{

					query.append("{").append("productMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(productParams.keySet())).append(")");
					params.putAll(productParams);
					if (matchByUser || matchBySalesArea)
					{
						query.append(" and ");
					}
				}

				if (matchByUser)
				{

					query.append("{").append("userMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(userParams.keySet())).append(")");
					params.putAll(userParams);
					if (matchBySalesArea)
					{
						query.append(" and ");
					}
				}

				if (matchBySalesArea)
				{

					query.append("{").append("salesAreaMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(salesAreaParams.keySet())).append(")");
					params.putAll(salesAreaParams);
					if (matchByCustomerGroup)
					{
						query.append(" and ");
					}
				}

				if (matchByCustomerGroup)
				{

					query.append("{").append("customerGroupMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(customerGroupParams.keySet())).append(")");
					params.putAll(customerGroupParams);
				}
			}

			if (addPricesByProductId)
			{

				if (matchByProduct || matchByUser || matchBySalesArea || matchByCustomerGroup)
				{

					query.append("}} UNION {{");
					isUnion = true;
				}
				query.append("select {PK} from {").append(this.type).append("} where {");
				query.append("productMatchQualifier").append("}=?matchByProductId and {");
				query.append("productId").append("}=?").append("productId");
				params.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
				params.put("productId", this.productId);
				if (matchByUser)
				{

					query.append(" and {").append("userMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(userParams.keySet())).append(")");
				}
				if (matchBySalesArea)
				{

					query.append(" and {").append("salesAreaMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(salesAreaParams.keySet())).append(")");
				}
				if (matchByCustomerGroup)
				{

					query.append(" and {").append("customerGroupMatchQualifier").append("} in (?");
					query.append(Joiner.on(", ?").join(customerGroupParams.keySet())).append(")");
				}
			}

			StringBuilder resultQuery;
			if (isUnion)
			{
				resultQuery = (new StringBuilder("select x.PK from ({{")).append(query).append("}}) x");
			}
			else
			{
				resultQuery = query;
			}

			return new QueryWithParams(resultQuery.toString(), params.build(), Collections.emptyList());
		}
	}

	private Map<String, Object> getProductRelatedParameters()
	{
		final Builder params = ImmutableMap.builder();

		if (this.anyProduct)
		{
			params.put("anyProduct", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		}
		if (this.productPk != null)
		{
			params.put("product", this.productPk.getLong());
		}
		if (this.productGroupPk != null)
		{
			params.put("productGroup", this.productGroupPk.getLong());
		}

		return params.build();
	}


	private Map<String, Object> getUserRelatedParameters()
	{
		final Builder params = ImmutableMap.builder();

		if (this.anyUser)
		{
			params.put("anyUser", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
		}
		if (this.userPk != null)
		{
			params.put("user", this.userPk.getLong());
		}
		if (this.userGroupPk != null)
		{
			params.put("userGroup", this.userGroupPk.getLong());
		}

		return params.build();
	}

	private Map<String, Object> getSalesAreaRelatedParameters()
	{
		final Builder params = ImmutableMap.builder();

		if (this.anySalesArea)
		{
			params.put("anySalesArea", String.valueOf(0));
		}
		if (this.salesArea != null)
		{
			params.put("salesArea", this.salesArea);
		}

		return params.build();
	}


	private Map<String, Object> getCustomerGroupRelatedParameters()
	{
		final Builder params = ImmutableMap.builder();

		if (this.anyCustomerGroup)
		{
			params.put("anyCustomerGroup", String.valueOf(0));
		}
		if (this.customerGroup != null)
		{
			params.put("customerGroup", this.customerGroup);
		}

		return params.build();
	}
}
