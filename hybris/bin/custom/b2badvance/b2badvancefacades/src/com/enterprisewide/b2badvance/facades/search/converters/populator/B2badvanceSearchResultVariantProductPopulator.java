/**
 *
 */
package com.enterprisewide.b2badvance.facades.search.converters.populator;

import de.hybris.platform.commercefacades.product.data.DiscountData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.Collections;


/**
 *
 */
public class B2badvanceSearchResultVariantProductPopulator extends SearchResultVariantProductPopulator
{

	// Static variable to hold the value of variable name for Part Number
	public static final String PARTNUMBER = "partNumber";

	/*
	 * Overriding the populate method to populate the value of custom attributes along with other values
	 *
	 * @param source- the SearchResultValueData source object to set
	 *
	 * @param target- the ProductData target object to be set
	 */
	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		if (source.getValues() != null)
		{
			final String totalDiscountsString = this.<String> getValue(source, "totalDiscounts");
			if (totalDiscountsString != null)
			{
				target.setTotalDiscounts(Collections.singletonList(createTotalDiscounts(totalDiscountsString)));
			}

			if (source.getValues().get(PARTNUMBER) != null)
			{
				target.setPartNumber((String) source.getValues().get(PARTNUMBER));

			}


		}
	}

	/**
	 * @param totalDiscountsString
	 * @return
	 */
	protected DiscountData createTotalDiscounts(final String totalDiscountsString)
	{
		final DiscountData discountData = createDiscountData();
		discountData.setValue(totalDiscountsString);
		return discountData;
	}

	protected DiscountData createDiscountData()
	{
		return new DiscountData();
	}

}
